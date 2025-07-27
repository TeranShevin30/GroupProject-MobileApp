package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddCustomers extends AppCompatActivity {

    private TextView cusCode;
    private EditText cusName, cusAddress, cusContact; //test
    private Spinner route;
    private Button createCus;
    ArrayList routeList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String txtCusCode;
    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customers);

        cusCode = findViewById(R.id.showCusCode);
        cusName = findViewById(R.id.edit_name);
        cusAddress = findViewById(R.id.edit_address);
        cusContact = findViewById(R.id.edit_contactNo);
        route = findViewById(R.id.spinner_route);
        createCus = findViewById(R.id.btn_createCustomer);

        setSpinnerRout();

        getCusCode();

        createCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                        .getReference("Customers");

                String txtCusName = cusName.getText().toString();
                String txtCusAddress = cusAddress.getText().toString();
                String txtCusContact = cusContact.getText().toString();
                String txtCusLoc = String.valueOf(route.getSelectedItem());

                if(TextUtils.isEmpty(txtCusAddress) || TextUtils.isEmpty(txtCusContact) || TextUtils.isEmpty(txtCusName)){
                    Toast.makeText(getApplicationContext(),"Required fields are empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    Customer customer = new Customer(txtCusAddress,"", txtCusCode, txtCusLoc, txtCusContact, txtCusName);
                    databaseReference.child(txtCusCode).setValue(customer);
                    Toast.makeText(getApplicationContext(),"Customer added successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AddCustomers.class));
                }
            }
        });

    }

    public void setSpinnerRout(){
        routeList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance(firebaseUrl);
        databaseReference = firebaseDatabase.getReference().child("Routs");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                routeList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    String loc = ds.child("locaName").getValue().toString();
                    routeList.add(loc);
                    ArrayAdapter adapter = new ArrayAdapter(AddCustomers.this, android.R.layout.simple_spinner_dropdown_item,routeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    route.setAdapter(adapter);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) routeList.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getCusCode(){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtCusCode=String.format("%04d",snapshot.child("Customers").getChildrenCount()+1);
                cusCode.setText(txtCusCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}