package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MakeOrders extends AppCompatActivity {

    private TextView txtInvNo,txtInvDate,txtInStock,txtTotal,txtReturnValue,
            txtPreBal,txtInvTot,txtReturnDed,txtTotCredit,txtNetBal;
    private EditText editUnitPrice,editQty,editFIssue,editExchange,editReturn,editReturnPrice,editCashReceipt;
    private Spinner spinnerItems;
    private Button btnCancel,btnAdd,btnPreview;
    private ArrayList itemList;

    DatabaseReference databaseReference;
    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_orders);

        txtInvNo = findViewById(R.id.txt_invoiceNo);
        txtInvDate = findViewById(R.id.txt_invoiceDate);
        txtInStock = findViewById(R.id.txt_inStock);
        txtTotal  = findViewById(R.id.txt_tot);
        txtReturnValue  = findViewById(R.id.txt_returnValue);
        txtPreBal  = findViewById(R.id.txt_preBal);
        txtInvTot = findViewById(R.id.txt_invoiceTot);
        txtReturnDed  = findViewById(R.id.txt_returnDeduct);
        txtTotCredit  = findViewById(R.id.txt_totCredit);
        txtNetBal  = findViewById(R.id.txt_netBal);

        editUnitPrice  = findViewById(R.id.edit_unitPrice);
        editQty = findViewById(R.id.edit_qty);
        editFIssue  = findViewById(R.id.edit_FIssue);
        editExchange  = findViewById(R.id.edit_exchange);
        editReturn  = findViewById(R.id.edit_return);
        editReturnPrice = findViewById(R.id.edit_returnPrice);
        editCashReceipt  = findViewById(R.id.edit_cashReceipts);


        spinnerItems  = findViewById(R.id.spinner_description);

        btnCancel  = findViewById(R.id.btn_cancel);
        btnAdd  = findViewById(R.id.btn_add);
        btnPreview = findViewById(R.id.btn_preview);

        Intent intent = getIntent();

        String invNo = intent.getStringExtra("INV_NO");
        String invDate = intent.getStringExtra("INV_DATE");
        String empId = intent.getStringExtra("EMP_ID");
        String vhNo = intent.getStringExtra("VH_NO");
        String cusCode = intent.getStringExtra("CUS_CODE");
        String mobId = intent.getStringExtra("MOB_ID");
        txtInvNo.setText(invNo);
        txtInvDate.setText(invDate);

        setSpinnerItems(vhNo);

        setTxt(empId,invNo);

        setPreBal(cusCode,invNo,empId);


        editUnitPrice.addTextChangedListener(textWatcher);
        editQty.addTextChangedListener(textWatcher);
        editReturn.addTextChangedListener(textWatcher);
        editReturnPrice.addTextChangedListener(textWatcher);
        editCashReceipt.addTextChangedListener(textWatcher);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = spinnerItems.getSelectedItem().toString();


                databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                databaseReference.child("Items").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds: snapshot.getChildren()){
                            String iName = ds.child("ItemName").getValue().toString();
                            if(iName.equals(itemName)) {

                                String itemCode = ds.child("ItemCode").getValue().toString();

                                databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                                databaseReference.child("VhLoadin").child(vhNo).child(itemCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        DataSnapshot ds = task.getResult();
                                        String oldQty = ds.child("lQty").getValue().toString();
                                        String getFIssue = editFIssue.getText().toString();
                                        String getExchange = editExchange.getText().toString();
                                        String getCashReceipt = editCashReceipt.getText().toString().trim();
                                        String getReturnPrice = editReturnPrice.getText().toString();
                                        String getNoOfReturn = editReturn.getText().toString();
                                        String getUnitPrice = editUnitPrice.getText().toString();
                                        String qty = editQty.getText().toString();
                                        if(TextUtils.isEmpty(qty)){qty = "0";}
                                        if(TextUtils.isEmpty(getFIssue)){getFIssue = "0";}
                                        if(getNoOfReturn==null && getNoOfReturn.isEmpty()){getNoOfReturn="0.00";}
                                        if(getReturnPrice==null && getReturnPrice.isEmpty()){getReturnPrice="0.00";}
                                        if(getCashReceipt==null && getCashReceipt.isEmpty()){getCashReceipt="0.00";}

                                        if( Integer.parseInt(oldQty) >= (Integer.parseInt(qty)+ Integer.parseInt(getFIssue))){

                                            updateCashRec(empId,invNo);

                                            addValues(getCashReceipt,empId,invNo,getUnitPrice,qty,getNoOfReturn,getReturnPrice);

                                            getInStockIntoDatabase(vhNo,itemName,empId,invNo,getExchange,getFIssue,getInvTotal(getUnitPrice,qty),qty,getReturnPrice,getNoOfReturn,getReturnValue(getNoOfReturn,getReturnPrice),getUnitPrice);

                                            updateQty(itemName, qty, vhNo,getFIssue);

                                            checkNetBal(cusCode,empId,invNo,getNoOfReturn,getReturnPrice,qty,getUnitPrice,getCashReceipt,getNoOfReturn);

                                            Intent intent = new Intent(getApplicationContext(),MakeOrders.class);
                                            intent.putExtra("VH_NO",vhNo);
                                            intent.putExtra("EMP_ID",empId);
                                            intent.putExtra("INV_NO",invNo);
                                            intent.putExtra("INV_DATE",invDate);
                                            intent.putExtra("CUS_CODE",cusCode);

                                            editUnitPrice.setText("0.00");
                                            editQty.setText("0");
                                            editCashReceipt.setText("0.00");
                                            editExchange.setText("0.00");
                                            editReturn.setText("0");
                                            editFIssue.setText("0");
                                            editReturnPrice.setText("0.00");

                                            txtInStock.setText("");
                                            txtTotal.setText("");
                                            txtReturnValue.setText("");

                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Out of stock",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });



            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBox(empId,invNo,vhNo,invDate,mobId);
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),InvoiceLayout.class);
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

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            txtTotal.setText(getInvTotalTxt());
            txtReturnValue.setText(getReturnValueTxt());
            txtReturnDed.setText(getReturnValueTxt());
            txtInvTot.setText(getInvTotalTxt());

            Intent intent = getIntent();

            String invNo = intent.getStringExtra("INV_NO");
            String empId = intent.getStringExtra("EMP_ID");


        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public String getInvTotalTxt() {

        String unitPrice = editUnitPrice.getText().toString();
        String qty = editQty.getText().toString();

        if (!TextUtils.isEmpty(unitPrice) && !TextUtils.isEmpty(qty)) {
                float number1 = Float.parseFloat(unitPrice);
                float number2 = Float.parseFloat(qty);
                float result = number1 * number2;
                return String.valueOf(result);
        }
        return "0";
    }

    public String getInvTotal(String unitPrice,String qty) {
            float number1 = Float.parseFloat(unitPrice);
            float number2 = Float.parseFloat(qty);
            float result = number1 * number2;
            return String.valueOf(result);
    }

    public String getReturnValueTxt() {

        String noOfReturns = editReturn.getText().toString();
        String returnPrice = editReturnPrice.getText().toString();

        if (!TextUtils.isEmpty(noOfReturns) && !TextUtils.isEmpty(returnPrice)) {
            float number1 = Float.parseFloat(noOfReturns);
            float number2 = Float.parseFloat(returnPrice);
            float result = number1 * number2;
            return String.valueOf(result);
        }
        return "0";
    }

    public String getReturnValue(String noOfReturns,String returnPrice) {
        if(noOfReturns!=null && !noOfReturns.isEmpty()) {
            float number1 = Float.parseFloat(noOfReturns);
            float number2 = Float.parseFloat(returnPrice);
            float result = number1 * number2;
            return String.valueOf(result);
        }else{
            return "0.00";
        }
    }


    public void getNetBalance(String empId,String invId,String getNoOfReturns,String returnPrice,String qty,String unitPrice,String cashReceipt,String noOfReturns){
        String returnValue = String.valueOf(Float.parseFloat(getNoOfReturns)*Float.parseFloat(returnPrice));
        String invTot = String.valueOf(Float.parseFloat(unitPrice)*Float.parseFloat(qty));

        Log.d("TOTAL",invTot+"  "+returnValue);


        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                String preBal = snapshot.child("previousBal").getValue().toString();
                String getBal = snapshot.child("balance").getValue().toString();

                if (!TextUtils.isEmpty(noOfReturns) && !TextUtils.isEmpty(returnPrice) && !TextUtils.isEmpty(unitPrice) && !TextUtils.isEmpty(qty)) {
                    float preBalance = Float.parseFloat(preBal);
                    float inv = Float.parseFloat(invTot);
                    float ret = Float.parseFloat(returnValue);
                    float totCredit = preBalance+inv-ret;
                    txtTotCredit.setText(String.valueOf(totCredit));

                    float netBal = Float.parseFloat(getBal) + totCredit - Float.parseFloat(cashReceipt);
                    txtNetBal.setText(String.valueOf(netBal));

                    HashMap updateBal = new HashMap();
                    updateBal.put("balance",String.valueOf(netBal));

                    databaseReference.child("Invoice").child(empId).child(invId).updateChildren(updateBal);

                }
            }
        });
    }

    public void updateCusBalInCustomers(String empId,String InvNo){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String bal = dataSnapshot.child(InvNo).child("balance").getValue(String.class);
                String cusId = dataSnapshot.child(InvNo).child("cusCode").getValue(String.class);

                HashMap updateCusBal = new HashMap();
                updateCusBal.put("cusBal",bal);

                databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                databaseReference.child("Customers").child(cusId).updateChildren(updateCusBal);

            }
        });
    }


    public void setSpinnerItems(String vhNo){
        itemList = new ArrayList();

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference();
        databaseReference.child("VhLoadin").child(vhNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    String itemName = String.valueOf(ds.child("ItemName").getValue());

                    itemList.add(itemName);
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,itemList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerItems.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = itemList.get(i).toString();
                String getItem = spinnerItems.getSelectedItem().toString();
                setTxtInStock(getItem,vhNo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void setTxtInStock(String itemName,String vhNo){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl)
                .getReference();
        databaseReference.child("VhLoadin").child(vhNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("ItemName").getValue().toString().equals(itemName)){
                        String qty = ds.child("lQty").getValue().toString();
                        txtInStock.setText(qty);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addValues(String cashReceipt, String empId,String invNo,String getUnitPrice,String qty,String getNoOfReturn,String getReturnPrice){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invNo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String returnValue = dataSnapshot.child("returnedAmount").getValue(String.class);
                String invAmount = dataSnapshot.child("invAmount").getValue(String.class);

                String newReturnValue = String.valueOf(Float.parseFloat(returnValue)+Float.parseFloat(getReturnValue(getNoOfReturn,getReturnPrice)));
                String newInvAmount = String.valueOf(Float.parseFloat(invAmount)+Float.parseFloat(getInvTotal(getUnitPrice,qty)));

                        HashMap update = new HashMap();
                        update.put("returnedAmount", newReturnValue);
                        update.put("invAmount", newInvAmount);


                        databaseReference.child("Invoice").child(empId).child(invNo).updateChildren(update).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });


                }



        });
    }

    public void alertBox(String empId,String invId,String vhNo,String invDate,String mobId){
        AlertDialog.Builder builder = new AlertDialog.Builder(MakeOrders.this);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
            databaseReference.child("Invoice").child(empId).child(invId).child("invStatus").setValue("2");

            Intent intent = new Intent(getApplicationContext(), Orders.class);

            intent.putExtra("VH_NO",vhNo);
            intent.putExtra("EMP_ID",empId);
            intent.putExtra("INV_NO",invId);
            intent.putExtra("INV_DATE",invDate);
            intent.putExtra("MOB_ID",mobId);

            startActivity(intent);
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addItemValue(String itemName,String empId,String invNo,String exchange, String freeIssue, String inStock, String lineTot, String qty, String returnPrice, String returnVal, String returnValue, String unitPrice){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String iName = ds.child("ItemName").getValue().toString();
                    if(iName.equals(itemName)){

                        String iCode = ds.child("ItemCode").getValue().toString();

                        InvItemClass invItemClass = new InvItemClass(exchange,freeIssue,inStock,iCode,itemName,lineTot,qty,returnPrice,returnVal,returnValue,unitPrice);
                        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                        databaseReference.child("Invoice").child(empId).child(invNo).child("InvItems").child(iCode).setValue(invItemClass);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateQty(String itemName,String qty, String vhNo,String fIssue){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    String iName = ds.child("ItemName").getValue().toString();
                    if(iName.equals(itemName)) {

                        String itemCode = ds.child("ItemCode").getValue().toString();


                        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                        databaseReference.child("VhLoadin").child(vhNo).child(itemCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                DataSnapshot ds = task.getResult();
                                String oldQty = ds.child("lQty").getValue().toString();
                                if( Integer.parseInt(oldQty) >= (Integer.parseInt(qty)+ Integer.parseInt(fIssue)))
                                {
                                    String newQty = String.valueOf(Integer.parseInt(oldQty) - Integer.parseInt(qty) - Integer.parseInt(fIssue));

                                    databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                                    databaseReference.child("VhLoadin").child(vhNo).child(itemCode).child("lQty").setValue(newQty);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    public void getInStockIntoDatabase(String vhNo,String itemName,String empId,String invNo,String exchange, String freeIssue, String lineTot, String qty, String returnPrice, String returnVal, String returnValue, String unitPrice){

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    String iName = ds.child("ItemName").getValue().toString();
                    if(iName.equals(itemName)) {
                        String itemCode = ds.child("ItemCode").getValue().toString();

                        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
                        databaseReference.child("VhLoadin").child(vhNo).child(itemCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                DataSnapshot ds = task.getResult();
                                String itemStock = ds.child("lQty").getValue().toString();

                                addItemValue(itemName,empId,invNo,exchange,freeIssue,itemStock,lineTot,qty,returnPrice,returnVal,returnValue,unitPrice);

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setPreBal(String cusCode,String invNo,String empId){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();

        databaseReference.child("Invoice").child(empId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int count = (int) snapshot.getChildrenCount();
                    if(count>1){

                        databaseReference.child("Invoice").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String preBal = "0";
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                                        String cusId = dataSnapshot1.child("cusCode").getValue().toString();
                                        String getInvNo = dataSnapshot1.child("invNo").getValue().toString();
                                        String getInvStatus = dataSnapshot1.child("invStatus").getValue().toString();

                                        if (cusCode.equals(cusId) && !invNo.equals(getInvNo)) {
                                            preBal = dataSnapshot1.child("balance").getValue().toString();
                                            txtPreBal.setText(preBal);
                                        }
                                    }
                                }
                                HashMap updatePreBal = new HashMap();
                                updatePreBal.put("previousBal", preBal);
                                databaseReference.child("Invoice").child(empId).child(invNo).updateChildren(updatePreBal);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setTxtPreBal(String empId,String invNo){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invNo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds = task.getResult();
                String preBal = ds.child("previousBal").getValue().toString();
                txtPreBal.setText(preBal);
            }
        });
    }

    public void updateCashRec(String empId,String invNo){
        String getCashRec = editCashReceipt.getText().toString();
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invNo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String cash = dataSnapshot.child("cashReceipt").getValue().toString();

                float newCash = Float.parseFloat(cash)+Float.parseFloat(getCashRec);

                databaseReference.child("Invoice").child(empId).child(invNo).child("cashReceipt").setValue(String.valueOf(newCash));

              }
        });
    }


    public void getNetBalanceWithoutPreBal(String empId,String invId,String getNoOfReturns,String returnPrice,String qty,String unitPrice,String cashReceipt,String noOfReturns){

        String returnValue = String.valueOf(Float.parseFloat(getNoOfReturns)*Float.parseFloat(returnPrice));
        String lineTot = String.valueOf(Float.parseFloat(unitPrice)*Float.parseFloat(qty));


        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();

        databaseReference.child("Invoice").child(empId).child(invId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String invTot = dataSnapshot.child("invAmount").getValue(String.class);
                String ret1 = dataSnapshot.child("returnedAmount").getValue(String.class);

                databaseReference.child("Invoice").child(empId).child(invId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot snapshot = task.getResult();
                        String netBal = snapshot.child("balance").getValue().toString();

                        if (!TextUtils.isEmpty(noOfReturns) && !TextUtils.isEmpty(returnPrice) && !TextUtils.isEmpty(unitPrice) && !TextUtils.isEmpty(qty)) {
                            float lTot = Float.parseFloat(lineTot);
                            float inv = Float.parseFloat(invTot);
                            float ret = Float.parseFloat(returnValue);
                            float ret2 = Float.parseFloat(ret1);
                            float totCredit = inv+lTot-ret-ret2;
                            float totCredit2 = lTot-ret;
                            txtTotCredit.setText(String.valueOf(totCredit));

                            float newNetBal = totCredit2 - Float.parseFloat(cashReceipt)+ Float.parseFloat(netBal);
                            String bal = String.valueOf(newNetBal);
                            txtNetBal.setText(bal);

                            HashMap updateBal = new HashMap();
                            updateBal.put("balance",bal);
                            databaseReference.child("Invoice").child(empId).child(invId).updateChildren(updateBal);

                        }
                    }
                });

            }
        });
    }

    public void checkNetBal(String cusCode,String empId,String invId,String getNoOfReturns,String returnPrice,String qty,String unitPrice,String cashReceipt,String noOfReturns){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("InvItems"))
                    getNetBalanceWithoutPreBal(empId,invId,getNoOfReturns,returnPrice,qty,unitPrice,cashReceipt,noOfReturns);
                else
                    getNetBalance(empId,invId,getNoOfReturns,returnPrice,qty,unitPrice,cashReceipt,noOfReturns);
                    updateCusBalInCustomers(empId,invId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setTxt(String empId,String invNo){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invNo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String preBal = dataSnapshot.child("previousBal").getValue(String.class);
                String ret = dataSnapshot.child("returnedAmount").getValue(String.class);
                String invTot = dataSnapshot.child("invAmount").getValue(String.class);
                String netBal = dataSnapshot.child("balance").getValue(String.class);

                txtTotCredit.setText(String.valueOf (Float.parseFloat(preBal)+Float.parseFloat(invTot)-Float.parseFloat(ret)));
                txtInvTot.setText(invTot);
                txtNetBal.setText(netBal);
            }
        });
    }
}