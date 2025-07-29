package com.example.salesapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InvoiceViewHolder extends RecyclerView.ViewHolder {

    TextView no,description,rate,fIssue,qty,value;
    public InvoiceViewHolder(@NonNull View itemView) {
        super(itemView);
        no = itemView.findViewById(R.id.txt_number);
        description = itemView.findViewById(R.id.txt_itemName);
        rate = itemView.findViewById(R.id.txt_unitPrice);
        fIssue = itemView.findViewById(R.id.txt_fIssue);
        qty = itemView.findViewById(R.id.txt_qty);
        value = itemView.findViewById(R.id.txt_total);
    }
}
