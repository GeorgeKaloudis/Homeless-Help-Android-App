package com.example.homelessapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelessapp.DTO.PhoneNumber;
import com.example.homelessapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    Context context;

    ArrayList<PhoneNumber> list;
    FirebaseDatabase database;
    DatabaseReference ref;

    public MyAdapter(Context context, ArrayList<PhoneNumber> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.calls_cardview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PhoneNumber phoneNumber = list.get(position);
        holder.name.setText(phoneNumber.getName());
        holder.tel.setText(phoneNumber.getTel());
        holder.makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.tel.getText()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView name,tel;
        ImageButton makeCall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.serviceName);
            tel = itemView.findViewById(R.id.phoneNumber);
            makeCall = itemView.findViewById(R.id.makeCall);
        }
    }

}
