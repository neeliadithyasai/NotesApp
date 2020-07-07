package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapterSubject extends RecyclerView.Adapter< adapterSubject.ViewHolder> {

    LayoutInflater inflater;
    List<subject> subjectlist =new ArrayList<>();

    adapterSubject(Context context, List<subject> subjectlist){
        this.inflater = LayoutInflater.from(context);
        this.subjectlist = subjectlist;

    }
    @NonNull
    @Override
    public adapterSubject.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_subjectcell,parent,false);
        return new ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull adapterSubject.ViewHolder holder, int position) {
        String title = subjectlist.get(position).getSubjectID();
        holder.subtitle.setText(title);


    }

    @Override
    public int getItemCount() {
        return  subjectlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subtitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subtitle = itemView.findViewById(R.id.subjectTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(),MainActivity.class);
                    i.putExtra("subname",String.valueOf(subjectlist.get(getAdapterPosition()).getSubjectID()));
                    v.getContext().startActivity(i);

                    Toast.makeText(v.getContext(),String.valueOf(subjectlist.get(getAdapterPosition()).getSubjectID()), Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}
