package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Orders extends AppCompatActivity {

    private TextView txtVehicleNo, txtSalesRep;
    private Spinner spinnerRoute, spinnerCusName;
    private DatePicker saleDate;
    private Button viewItem, loadCustomers;
    private ArrayList routList,cusList;
    DatabaseReference databaseReference;
    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        viewItem = findViewById(R.id.viewItem);
        txtSalesRep = findViewById(R.id.txt_salesRep);
        txtVehicleNo = findViewById(R.id.txt_vehicleNo);
        spinnerRoute = findViewById(R.id.spinner_route);
        spinnerCusName = findViewById(R.id.spinner_customerName);
        saleDate = findViewById(R.id.datePicker_saleDate);
        loadCustomers = findViewById(R.id.btn_loadCustomer);

        Intent intent = getIntent();

        setSpinnerRoute();
        String mobId = intent.getStringExtra("MOB_ID");
        setVhNoSalesRep(txtVehicleNo,txtSalesRep,mobId);

        Log.d("TAG","This is mobile id == "+mobId);


        loadCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String route = String.valueOf(spinnerRoute.getSelectedItem());
                setSpinnerCusCode(route);
            }
        });


        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spinnerCusName != null && spinnerCusName.getSelectedItem() !=null ) {
                    String cusCode = String.valueOf(spinnerCusName.getSelectedItem());
                    int day = saleDate.getDayOfMonth();
                    int month = saleDate.getMonth() + 1;
                    int year = saleDate.getYear();
                    String date = day + "/" + month + "/" + year;
                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                    databaseReference.child("RegisteredMobile").child(mobId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            DataSnapshot dataSnapshot = task.getResult();
                            String empId = String.valueOf(dataSnapshot.child("empId").getValue());
                            setDetails(date, empId, cusCode,mobId);
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Please select a customer",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void setSpinnerRoute(){

        routList = new ArrayList();

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference().child("Routs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                routList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String loc = ds.child("locaName").getValue().toString();
                    routList.add(loc);
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,routList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRoute.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = String.valueOf(routList.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void setVhNoSalesRep(TextView txtVehicleNo, TextView txtSalesRep, String mobId){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference().child("RegisteredMobile");
        databaseReference.child(mobId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String vhNo = dataSnapshot.child("vhNo").getValue().toString();
                //String id = dataSnapshot.child("empId").getValue().toString();
                String name = dataSnapshot.child("empName").getValue().toString();

                txtVehicleNo.setText(vhNo);
                txtSalesRep.setText(name);
            }
        });
    }

    public void setSpinnerCusCode(String route){
        cusList = new ArrayList();

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference().child("Customers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cusList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String loc = String.valueOf(ds.child("cusLoc").getValue());
                    if(loc.equals(route)){
                        String cusName = String.valueOf(ds.child("cusName").getValue());
                        String cusCode = String.valueOf(ds.child("cusCode").getValue());
                        cusList.add(cusCode);
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,cusList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCusName.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerCusName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = String.valueOf(cusList.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setDetails(String date,String repId,String cusCode,String mobId){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference();
        databaseReference.child("Customers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String cusName = String.valueOf(dataSnapshot.child(cusCode).child("cusName").getValue());
                String cusRoute = String.valueOf(dataSnapshot.child(cusCode).child("cusLoc").getValue());

                databaseReference.child("RegisteredMobile").child(mobId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String empName = String.valueOf(dataSnapshot.child("empName").getValue());
                        String vhNo = String.valueOf(dataSnapshot.child("vhNo").getValue());

                        databaseReference.child("Invoice").child(repId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                DataSnapshot dataSnapshot = task.getResult();
                                if(task.getResult().exists()){
                                    String invCode = repId +"-"+String.format("%06d",dataSnapshot.getChildrenCount()+1);

                                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference("Invoice");

                                    InvItemClass items = new InvItemClass("0.00","0","0","0","0","0.00","0","0.00","0","0.00","0.00");
                                    InvoiceDetails invoiceDetails = new InvoiceDetails(vhNo,empName,repId,invCode,date,cusRoute,cusName,cusCode,"1","0.00","0.00","0.00","0.00","0.00");
                                    databaseReference.child(repId).child(invCode).setValue(invoiceDetails);

                                    Intent intent = new Intent(getApplicationContext(),MakeOrders.class);
                                    intent.putExtra("VH_NO",vhNo);
                                    intent.putExtra("EMP_ID",repId);
                                    intent.putExtra("INV_NO",invCode);
                                    intent.putExtra("INV_DATE",date);
                                    intent.putExtra("CUS_CODE",cusCode);
                                    intent.putExtra("MOB_ID",mobId);
                                    startActivity(intent);
                                }else{
                                    String invCode = repId +"-"+String.format("%06d",1);

                                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                                            .getReference("Invoice");
                                    InvoiceDetails invoiceDetails = new InvoiceDetails(vhNo,empName,repId,invCode,date,cusRoute,cusName,cusCode,"1","0.00","0.00","0.00","0.00","0.00");
                                    databaseReference.child(repId).child(invCode).setValue(invoiceDetails);

                                    Intent intent = new Intent(getApplicationContext(),MakeOrders.class);
                                    intent.putExtra("VH_NO",vhNo);
                                    intent.putExtra("EMP_ID",repId);
                                    intent.putExtra("INV_NO",invCode);
                                    intent.putExtra("INV_DATE",date);
                                    intent.putExtra("MOB_ID",mobId);
                                    startActivity(intent);
                                }

                            }
                        });

                    }
                });

            }
        });

    }


}