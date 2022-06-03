package com.varsitycollege.navbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    Context context;
    ArrayList<Item> list;

    private final RecyclerViewItemInterface recyclerViewItemInterface;

    public ItemAdapter(Context context, ArrayList<Item> list, RecyclerViewItemInterface recyclerViewItemInterface){
        this.context = context;
        this.list = list;
        this.recyclerViewItemInterface = recyclerViewItemInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_entry,parent, false);
        return new MyViewHolder(v, recyclerViewItemInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        Item items = list.get(position);
        holder.category.setText(items.getCategory());
        holder.item.setText(items.getItem());
        holder.description.setText(items.getDescriptionItem());
        holder.date.setText(items.getDateChoosen());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView category, item, description, date;
        public MyViewHolder(@NonNull View itemView, RecyclerViewItemInterface recyclerViewItemInterface) {
            super(itemView);
            category = itemView.findViewById(R.id.textname);
            item = itemView.findViewById(R.id.textgoal);
            description = itemView.findViewById(R.id.itemdesc);
            date = itemView.findViewById(R.id.itemdate);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if(recyclerViewItemInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewItemInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
