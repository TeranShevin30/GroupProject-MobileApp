package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.util.HashMap;

public class AssignSalesRep extends AppCompatActivity {

    private EditText contactNo;
    private Spinner spinner_salesRep,spinner_vehicleNo;
    private Button btnAssign;
    private RadioButton radioAdmin, radioSalesRep;
    DatabaseReference databaseReference;
    ArrayList nickNamesList,salesRepsList,vehicleNoList;
    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_sales_rep);

        spinner_salesRep = findViewById(R.id.spinner_salesRep);
        spinner_vehicleNo = findViewById(R.id.spinner_vehicalNo);
        btnAssign = findViewById(R.id.btnAssign);
        radioAdmin = findViewById(R.id.radio_admin);
        radioSalesRep = findViewById(R.id.radio_SalesRep);
        contactNo = findViewById(R.id.edit_contactNo);

        setSpinner_salesRep();
        setSpinner_vehicleNo();

        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String empId = spinner_salesRep.getSelectedItem().toString();
                String vehicleNo = spinner_vehicleNo.getSelectedItem().toString();
                String contact = String.valueOf(contactNo.getText());

                updateDetails(empId,vehicleNo,contact);
                Intent intent = new Intent(getApplicationContext(), AssignSalesRep.class);
                startActivity(intent);

            }
        });

    }

    public void setSpinner_salesRep(){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).
                getReference().child("SalesRef");
        salesRepsList = new ArrayList();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                salesRepsList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String salesRep = ds.child("empId").getValue().toString();
                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                    databaseReference.child("RegisteredMobile").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds2:snapshot.getChildren()){
                                String getEmpId = ds2.child("empId").getValue(String.class);
                                if(salesRep.equals(getEmpId)){
                                    salesRepsList.add(salesRep);
                                    break;
                                }
                            }
                            ArrayAdapter adapter = new ArrayAdapter(AssignSalesRep.this, android.R.layout.simple_spinner_dropdown_item,salesRepsList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_salesRep.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner_salesRep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItems = salesRepsList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setSpinner_vehicleNo(){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference().child("Vehicles");
        vehicleNoList = new ArrayList();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vehicleNoList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String vehicleNo = ds.child("vhNumber").getValue().toString();
                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                    databaseReference.child("RegisteredMobile").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean status1 = false;
                            for(DataSnapshot ds1:snapshot.getChildren()){
                                String getVhNo = ds1.child("vhNo").getValue(String.class);
                                if(vehicleNo.equals(getVhNo)){
                                    status1 = true;
                                    break;
                                }
                            }
                            if(!status1) {
                                vehicleNoList.add(vehicleNo);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(AssignSalesRep.this, android.R.layout.simple_spinner_dropdown_item,vehicleNoList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_vehicleNo.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    ArrayAdapter adapter = new ArrayAdapter(AssignSalesRep.this, android.R.layout.simple_spinner_dropdown_item,vehicleNoList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_vehicleNo.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner_vehicleNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = vehicleNoList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public String userCategory(){
        if(radioAdmin.isChecked()){
            return "admin";
        }
        if (radioSalesRep.isChecked()){
            return "salesrep";
        }
        return "";
    }

    public void updateDetails(String empId,String vehicleNo, String contact){
        String userCategory = userCategory();

        HashMap update = new HashMap<>();
        update.put("userCategory",userCategory);
        update.put("vhNo",vehicleNo);
        update.put("contactNo",contact);

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("RegisteredMobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String getMobId = ds.child("mobileId").getValue(String.class);
                    String getEmpId = ds.child("empId").getValue(String.class);
                    if(empId.equals(getEmpId)){
                        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                                .getReference();
                        databaseReference.child("RegisteredMobile").child(getMobId).updateChildren(update).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(getApplicationContext(),"SalesRep Assigned Successfully",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





}