package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PrevInvoice extends AppCompatActivity {

    private DatePicker datePicker;
    private Spinner spinnerCusName, spinnerInvNo;
    private Button loadCus,search,viewInvoice;
    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_invoice);

        datePicker = findViewById(R.id.datePicker_prevInvoice);
        spinnerCusName = findViewById(R.id.spinner_customerName);
        spinnerInvNo = findViewById(R.id.spinner_invoiceNo);
        loadCus = findViewById(R.id.btn_loadCustomer);
        search = findViewById(R.id.btn_searchCustomer);
        viewInvoice = findViewById(R.id.btn_viewInvoice);

        Intent intent = getIntent();
        String mobId = intent.getStringExtra("MOB_ID");

        loadCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String date = day + "/" + month + "/" + year;
                setSpinnerCusName(date);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerCusName != null && spinnerCusName.getSelectedItem() != null) {
                    String cusCode = spinnerCusName.getSelectedItem().toString();
                    setSpinnerInvNo(cusCode);
                }else{
                    Toast.makeText(getApplicationContext(),"Please select a Customer",Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                if (spinnerInvNo != null && spinnerInvNo.getSelectedItem() != null) {
                    String invNo = spinnerInvNo.getSelectedItem().toString();
                    String empId = invNo.substring(0, 5);

                    databaseReference.child("Invoice").child(empId).child(invNo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            DataSnapshot ds = task.getResult();
                            Intent intent = new Intent(getApplicationContext(), InvoiceLayoutNew.class);

                            String invDate = ds.child("invDate").getValue(String.class);
                            String vhNo = ds.child("vehicleNo").getValue(String.class);
                            String cusCode = ds.child("cusCode").getValue(String.class);

                            intent.putExtra("VH_NO",vhNo);
                            intent.putExtra("EMP_ID",empId);
                            intent.putExtra("INV_NO",invNo);
                            intent.putExtra("INV_DATE",invDate);
                            intent.putExtra("MOB_ID",mobId);
                            intent.putExtra("CUS_CODE",cusCode);

                            startActivity(intent);

                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please select a Invoice No.",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void setSpinnerCusName(String date){
        ArrayList cusList = new ArrayList();
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    for (DataSnapshot ds1:ds.getChildren()){
                        String invDate = ds1.child("invDate").getValue(String.class);
                        if (invDate.equals(date)){
                            String cusCode = ds1.child("cusCode").getValue(String.class);
                            cusList.add(cusCode);
                        }
                    }
                }
                Set<String> set = new HashSet<>(cusList);
                cusList.clear();
                cusList.addAll(set);

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,cusList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCusName.setAdapter(adapter);
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

    public void setSpinnerInvNo(String cusCode){
        ArrayList invNumbers = new ArrayList();

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    for (DataSnapshot ds1:ds.getChildren()){
                        String getCusCode = ds1.child("cusCode").getValue(String.class);
                        if(getCusCode.equals(cusCode)){
                            String getInvNo = ds1.child("invNo").getValue(String.class);
                            invNumbers.add(getInvNo);
                        }
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,invNumbers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerInvNo.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerInvNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = String.valueOf(invNumbers.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}