package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    DatabaseReference databaseReference;
    String mobId;

    TextInputEditText editNickName;

    AppCompatSpinner spinnerEmpId;
    ArrayList empIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("RegisteredMobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isExists = false;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String getAndroidId = dataSnapshot.child("uuId").getValue(String.class);
                    if(androidId.equals(getAndroidId)){
                        isExists = true;
                        mobId = dataSnapshot.getKey().toString();
                        String userCategory = dataSnapshot.child("userCategory").getValue(String.class);
                        if(userCategory.equals("admin")){
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("MOB_ID",mobId);
                            startActivity(intent);
                        }else if(userCategory.equals("salesrep")){
                            Intent intent = new Intent(getApplicationContext(),MainActivitySalesRep.class);
                            intent.putExtra("MOB_ID",mobId);
                            startActivity(intent);
                        }else{
                            exitAlertBox();
                            break;
                        }

                    }

                }

                if(!isExists){
                    Log.d("TAG",String.valueOf(!isExists));
                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                    databaseReference.child("RegisteredMobile").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            mobId = String.format("M"+"%03d",snapshot.getChildrenCount()+1);

                            test(mobId,androidId);

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

    }

    public void test(String mobId,String androidId) {
        View view = LayoutInflater.from(Login.this).inflate(R.layout.dialogbox_layout,null);
        spinnerEmpId = view.findViewById(R.id.spinner_empId);
        editNickName = view.findViewById(R.id.edit_nickName);

        empIdList = new ArrayList();

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("SalesRef").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String empId = dataSnapshot.child("empId").getValue(String.class);
                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                    databaseReference.child("RegisteredMobile").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean isExist = false;
                            for (DataSnapshot ds:snapshot.getChildren()){
                                String getEmpId = ds.child("empId").getValue(String.class);
                                if(empId.equals(getEmpId)){
                                    isExist = true;
                                    break;
                                }
                            }
                            if(!isExist){
                                empIdList.add(empId);
                            }
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,empIdList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerEmpId.setAdapter(adapter);
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

        spinnerEmpId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String empId = (String) empIdList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle("Register");
        alertbox.setView(view);
        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MobileDetails mobileDetails = new MobileDetails("0","0","0","0","0",mobId,"0","0",androidId,"0");

                databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                databaseReference.child("RegisteredMobile").child(mobId).setValue(mobileDetails);

                String empId = String.valueOf(spinnerEmpId.getSelectedItem());
                String nickName = editNickName.getText().toString();

                databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                databaseReference.child("SalesRef").child(empId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot ds = task.getResult();
                        String empName = ds.child("empName").getValue(String.class);

                        HashMap update = new HashMap();
                        update.put("empId",empId);
                        update.put("nickName",nickName);
                        update.put("empName",empName);
                        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                        databaseReference.child("RegisteredMobile").child(mobId).updateChildren(update);
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);

                    }
                });


            }
        });
        alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {System.exit(0);}
        });

        AlertDialog dialog = alertbox.create();
        dialog.show(); // Show the dialog first

        Button button = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);


    }


    public void exitAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

        builder.setTitle("Alert");
        builder.setMessage("Contact Admin");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
        builder.show();
    }



}