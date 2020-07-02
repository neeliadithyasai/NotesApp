package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class adapterNotes extends RecyclerView.Adapter< adapterNotes.ViewHolder> {

    LayoutInflater inflater;
    List<notes> noteslist =new ArrayList<>();



    adapterNotes(Context context, List<notes> noteslist){
        this.inflater = LayoutInflater.from(context);
        this.noteslist = noteslist;

    }

    @NonNull
    @Override
    public adapterNotes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_cell,parent,false);
        return new ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull adapterNotes.ViewHolder holder, int position) {
        String title = noteslist.get(position).getTitle();
        String date = noteslist.get(position).getDate();
        String time = noteslist.get(position).getTime();
        holder.date.setText(date);
        holder.time.setText(time);
        holder.title.setText(title);





    }

    @Override
    public int getItemCount() {
        return noteslist.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time,date,title;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.ntime);
            date = itemView.findViewById(R.id.ndate);
            title = itemView.findViewById(R.id.ntitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"item Clicked", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

}
