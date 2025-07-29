package com.example.salesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceLayoutNew extends AppCompatActivity {

    TextView txtVhNo,txtInvNo,txtEmpName,txtCusName,txtCusRoute,txtInvDate,txtReturnItemTot,
            txtNetTot,txtPreBal,txtTotCredit,txtCashReceipt,txtCurrentBal,txtCusCode;
    Button save;
    DatabaseReference databaseReference;

    RecyclerView recyclerView,returnedRecyclerView;
    LinearLayout mainLinearLayout;
    Bitmap bitmap;

    private String firebaseUrl = "https://salesapp-6c1e0-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_layout_new);

        txtCusCode = findViewById(R.id.txt_cusCode);
        txtVhNo = findViewById(R.id.txt_vehicleNo);
        txtInvNo = findViewById(R.id.txt_invoiceNo);
        txtEmpName = findViewById(R.id.txt_empName);
        txtCusName = findViewById(R.id.txt_cusName);
        txtCusRoute = findViewById(R.id.txt_cusRoute);
        txtInvDate = findViewById(R.id.txt_invoiceDate);
        txtReturnItemTot = findViewById(R.id.txt_returnItemTot);
        txtNetTot = findViewById(R.id.txt_netTot);
        txtPreBal = findViewById(R.id.txt_preBal);
        txtTotCredit = findViewById(R.id.txt_totCredit);
        txtCashReceipt = findViewById(R.id.txt_receipt);
        txtCurrentBal = findViewById(R.id.txt_currentBal);
        save = findViewById(R.id.btn_save);
        mainLinearLayout = findViewById(R.id.mainLinearLayout);

        Intent intent = getIntent();
        String invNo = intent.getStringExtra("INV_NO");
        String invDate = intent.getStringExtra("INV_DATE");
        String empId = intent.getStringExtra("EMP_ID");
        String vhNo = intent.getStringExtra("VH_NO");
        String mobId = intent.getStringExtra("MOB_ID");
        String cusCode = intent.getStringExtra("CUS_CODE");

        List<AddedItems> addedItems = new ArrayList<AddedItems>();
        List<AddedItems> addedReturnItems = new ArrayList<AddedItems>();


        addItemsToArrayList((ArrayList) addedItems,empId,invNo);
        addReturnItemsToArrayList((ArrayList) addedReturnItems,empId,invNo);

        recyclerView = findViewById(R.id.addItems);
        returnedRecyclerView = findViewById(R.id.return_recyclerView);


        txtVhNo.setText(vhNo);
        txtInvNo.setText(invNo);
        txtInvDate.setText(invDate);

        setDetails(txtCusCode,empId,invNo,txtEmpName,txtCusName,txtCusRoute,txtReturnItemTot,txtNetTot,txtPreBal,txtTotCredit,txtCashReceipt,txtCurrentBal);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePdf(invNo);

            }
        });

    }

    private void savePdf(String invNo){
        bitmap = loadBitmapFromView(mainLinearLayout,mainLinearLayout.getWidth(),mainLinearLayout.getHeight());
        createPdf(invNo);
    }

    private void createPdf(String invNo) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;

        int convertHeight = (int)height, convertWidth = (int)width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080,1920,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap,1080,1920,true);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);

        File targetPdf = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = invNo+".pdf";
        File filepath = new File(targetPdf,fileName);
        try {
            document.writeTo(new FileOutputStream(filepath));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }
        document.close();
        Toast.makeText(getApplicationContext(),"Saved to "+targetPdf,Toast.LENGTH_SHORT).show();
    }

    private Bitmap loadBitmapFromView(LinearLayout layout,int width,int height) {
        bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        return bitmap;
    }


    public void setDetails(TextView txtCusCode, String empId,String invNo,TextView txtEmpName,TextView txtCusName,TextView txtCusRoute,TextView txtReturnItemTot,TextView txtNetTot,TextView txtPreBal,TextView txtTotCredit,TextView txtCashReceipt,TextView txtCurrentBal){

        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invNo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds = task.getResult();

                String empName = ds.child("repName").getValue().toString();
                String cusName = ds.child("cusName").getValue().toString();
                String cusRoute = ds.child("cusRoute").getValue().toString();
                String returnItemTot = ds.child("returnedAmount").getValue().toString();
                String netTot = ds.child("invAmount").getValue().toString();
                String preBal = ds.child("previousBal").getValue().toString();
                String totCredit = String.valueOf(Float.parseFloat(netTot)-Float.parseFloat(preBal));
                String cashReceipt = ds.child("cashReceipt").getValue().toString();
                String currentBal = ds.child("balance").getValue().toString();
                String cusCode = ds.child("cusCode").getValue().toString();

                txtEmpName.setText(empName);
                txtCusName.setText(cusName);
                txtCusRoute.setText(cusRoute);
                txtReturnItemTot.setText(returnItemTot);
                txtNetTot.setText(netTot);
                txtPreBal.setText(preBal);
                txtTotCredit.setText(totCredit);
                txtCashReceipt.setText(cashReceipt);
                txtCurrentBal.setText(currentBal);
                txtCusCode.setText(cusCode);

            }
        });

    }

    public void addItemsToArrayList(ArrayList addedItems,String empId,String invNo){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invNo).child("InvItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int i = 1;

                for (DataSnapshot ds:snapshot.getChildren()){
                    String description = ds.child("itemName").getValue(String.class);
                    String rate = ds.child("unitPrice").getValue(String.class);
                    String fIssue = ds.child("freeIssue").getValue(String.class);
                    String qty = ds.child("qty").getValue(String.class);
                    String value = ds.child("lineTot").getValue(String.class);

                    AddedItems items = new AddedItems(String.valueOf(i),description,rate,fIssue,qty,value);

                    addedItems.add(items);
                    i+=1;

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(new InvoiceAdapter(getApplicationContext(),addedItems));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addReturnItemsToArrayList(ArrayList addedItems,String empId,String invNo){
        databaseReference = FirebaseDatabase.getInstance(firebaseUrl).getReference();
        databaseReference.child("Invoice").child(empId).child(invNo).child("InvItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int i = 1;

                for (DataSnapshot ds:snapshot.getChildren()){

                    String returnVal = ds.child("returnVal").getValue(String.class);

                    if(!returnVal.equals("0")) {

                        String description = ds.child("itemName").getValue(String.class);
                        String rate = ds.child("returnPrice").getValue(String.class);
                        String value = ds.child("returnValue").getValue(String.class);

                        AddedItems items = new AddedItems(String.valueOf(i), description, rate, returnVal, value);

                        addedItems.add(items);
                        i += 1;
                    }

                }
                returnedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                returnedRecyclerView.setAdapter(new InvoiceReturnAdapter(getApplicationContext(),addedItems));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}