package com.example.notesapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.MainActivity;
import com.example.notesapp.R;
import com.example.notesapp.model.subject;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class adapterSubject extends RecyclerView.Adapter< adapterSubject.ViewHolder> implements Filterable  {

    LayoutInflater inflater;
    List<subject> subjectlist =new ArrayList<>();
    List<subject> subjectlistFull =new ArrayList<>();

    Context dcontext;
    public adapterSubject(Context context, List<subject> subjectlist,Context dcontext){
        this.inflater = LayoutInflater.from(context);
        this.subjectlist = subjectlist;
        subjectlistFull = new ArrayList<subject>(subjectlist);
        this.dcontext = dcontext;

    }
    @NonNull
    @Override
    public adapterSubject.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_subjectcell,parent,false);
        return new ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull final adapterSubject.ViewHolder holder, final int position) {
        String title = subjectlist.get(position).getSubjectID();
        holder.subtitle.setText(title);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final DatabaseReference notesdata = FirebaseDatabase.getInstance().getReference().child("Notes").child(String.valueOf(subjectlist.get(position).getSubjectID()));



                Log.d("position", String.valueOf(subjectlist.get(position).getSubjectID()));


                AlertDialog.Builder builder = new AlertDialog.Builder(dcontext);
                builder.setTitle("Are you sure to delete?");



                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                         subjectlist.remove(subjectlist.get(position));
                        notesdata.removeValue();

                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();



                return false;

            }
        });



    }

    @Override
    public int getItemCount() {
        return  subjectlist.size();
    }

    @Override
    public Filter getFilter() {
        return subjectfilter;
    }

    private Filter subjectfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<subject> filteredList = new ArrayList<subject>( );
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(subjectlistFull );
            }else {
                String filterpattern = constraint.toString().toLowerCase().trim();

                for(subject item :subjectlistFull){
                    if(item.getSubjectID().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            subjectlist.clear();


            subjectlist.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subtitle;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            subtitle = itemView.findViewById(R.id.subjectTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    i.putExtra("subname",String.valueOf(subjectlist.get(getAdapterPosition()).getSubjectID()));
                    v.getContext().startActivity(i);

                    Toast.makeText(v.getContext(),String.valueOf(subjectlist.get(getAdapterPosition()).getSubjectID()), Toast.LENGTH_LONG).show();

                }
            });


        }
    }
}
