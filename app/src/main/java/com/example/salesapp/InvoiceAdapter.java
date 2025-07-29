package com.example.salesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceViewHolder> {

    Context context;
    List<AddedItems> addedItemsList;

    public InvoiceAdapter(Context context, List<AddedItems> addedItemsList) {
        this.context = context;
        this.addedItemsList = addedItemsList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceViewHolder(LayoutInflater.from(context).inflate(R.layout.table_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        holder.no.setText(addedItemsList.get(position).getNo());
        holder.description.setText(addedItemsList.get(position).getDescription());
        holder.rate.setText(addedItemsList.get(position).getRate());
        holder.fIssue.setText(addedItemsList.get(position).getfIssue());
        holder.qty.setText(addedItemsList.get(position).getQty());
        holder.value.setText(addedItemsList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return addedItemsList.size();
    }
}
