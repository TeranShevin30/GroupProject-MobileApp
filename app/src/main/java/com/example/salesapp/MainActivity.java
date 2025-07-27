package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private CardView prev_Invoice, orders, add_Customer, assign_sales_rep;
    private DatabaseReference databaseReference;
    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private TextView repName,userCategory;
    final static int REQUEST_CODE = 1232;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermission();

        prev_Invoice = (CardView) findViewById(R.id.prev_Invoice);
        orders = (CardView) findViewById(R.id.orders);
        add_Customer = (CardView) findViewById(R.id.add_Customer);
        assign_sales_rep = (CardView) findViewById(R.id.assign_sales_rep);
        repName = findViewById(R.id.repName);
        userCategory = findViewById(R.id.repPosition);

        Intent intent = getIntent();
        String mobId = intent.getStringExtra("MOB_ID");


        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("RegisteredMobile").child(mobId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String name = dataSnapshot.child("nickName").getValue(String.class);
                String category = dataSnapshot.child("userCategory").getValue(String.class);
                repName.setText(name);
                userCategory.setText(category);
            }
        });


        prev_Invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String mobId = intent.getStringExtra("MOB_ID");
                Intent intent1 = new Intent(getApplicationContext(),PrevInvoice.class);
                intent1.putExtra("MOB_ID",mobId);
                startActivity(intent1);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String mobId = intent.getStringExtra("MOB_ID");
                Intent intent1 = new Intent(getApplicationContext(),Orders.class);
                intent1.putExtra("MOB_ID",mobId);
                startActivity(intent1);
            }
        });

        add_Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String mobId = intent.getStringExtra("MOB_ID");
                Intent intent1 = new Intent(getApplicationContext(),AddCustomers.class);
                intent1.putExtra("MOB_ID",mobId);
                startActivity(intent1);
            }
        });

        assign_sales_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String mobId = intent.getStringExtra("MOB_ID");
                Intent intent1 = new Intent(getApplicationContext(),AssignSalesRep.class);
                intent1.putExtra("MOB_ID",mobId);
                startActivity(intent1);
            }
        });


    }

    private void askPermission(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
    }
}