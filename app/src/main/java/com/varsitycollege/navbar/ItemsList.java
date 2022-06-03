package com.varsitycollege.navbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
public class ItemsList extends AppCompatActivity implements RecyclerViewItemInterface {

    RecyclerView recyclerView;
    ArrayList<Item> list;
    DatabaseReference databaseReference;
    ItemAdapter adapter;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(ItemsList.this, AddItems.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        recyclerView = findViewById(R.id.recycleview);

        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference().child("Item");


       // databaseReference = FirebaseDatabase.getInstance().getReference("items");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ItemsList.this, "An error has occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ItemsList.this, ItemsActivity.class);
        intent.putExtra("CATEGORY", list.get(position).getCategory());
        intent.putExtra("ITEM", list.get(position).getItem());
        intent.putExtra("DESCRIPTION", list.get(position).getItem());
        intent.putExtra("DATE", list.get(position).getDateChoosen());
        startActivity(intent);
    }
}