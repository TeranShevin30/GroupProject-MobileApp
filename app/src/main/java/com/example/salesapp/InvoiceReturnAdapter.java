package com.example.salesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceReturnAdapter extends RecyclerView.Adapter<InvoiceReturnViewHolder> {
    Context context;
    List<AddedItems> addedItemsList;

    public InvoiceReturnAdapter(Context context, List<AddedItems> addedItemsList) {
        this.context = context;
        this.addedItemsList = addedItemsList;
    }

    @NonNull
    @Override
    public InvoiceReturnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceReturnViewHolder(LayoutInflater.from(context).inflate(R.layout.return_table_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceReturnViewHolder holder, int position) {

        holder.no.setText(addedItemsList.get(position).getNo());
        holder.description.setText(addedItemsList.get(position).getDescription());
        holder.rate.setText(addedItemsList.get(position).getRate());
        holder.qty.setText(addedItemsList.get(position).getQty());
        holder.value.setText(addedItemsList.get(position).getValue());

    }


    @Override
    public int getItemCount() {
        return addedItemsList.size();
    }
}
