package com.varsitycollege.navbar;

import android.content.Context;
import android.icu.util.ULocale;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        Context context;
        ArrayList<User> list;
        private final RecyclerViewInterface recyclerViewInterface;
        public MyAdapter(Context context, ArrayList<User> list, RecyclerViewInterface recyclerViewInterface){
            this.context = context;
            this.list = list;
            this.recyclerViewInterface = recyclerViewInterface;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.activity_categoryentry,parent, false);
            return new MyViewHolder(v, recyclerViewInterface);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            User user = list.get(position);
            holder.name.setText(user.getCategoryName());
            holder.goal.setText(user.getCategoryGoal());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView name, goal;
            public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
                super(itemView);
                name = itemView.findViewById(R.id.textname);
                goal = itemView.findViewById(R.id.textgoal);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(recyclerViewInterface != null){
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                recyclerViewInterface.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }
    }

