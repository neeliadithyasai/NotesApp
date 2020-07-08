package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapterNotes extends RecyclerView.Adapter< adapterNotes.ViewHolder> implements Filterable {

    LayoutInflater inflater;
    List<notes> noteslist =new ArrayList<>();
    List<notes> noteslistfull =new ArrayList<>();



    adapterNotes(Context context, List<notes> noteslist){
        this.inflater = LayoutInflater.from(context);
        this.noteslist = noteslist;
        noteslistfull = new ArrayList<notes>(noteslist);

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

    @Override
    public Filter getFilter() {
        return notesfilter;
    }

    private Filter notesfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<notes> filteredList = new ArrayList<notes>( );
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(noteslistfull);
            }else {
                String filterpattern = constraint.toString().toLowerCase().trim();

                for(notes item :noteslistfull){
                    if(item.getTitle().toLowerCase().contains(filterpattern)){
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
            noteslist.clear();
            Log.d("searchresults", String.valueOf(results.values));

            noteslist.addAll((List) results.values);
            notifyDataSetChanged();


        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time,date,title;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.ntime);
            date = itemView.findViewById(R.id.ndate);
            title = itemView.findViewById(R.id.subjectTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Intent i = new Intent(v.getContext(),notesDetails.class);
                i.putExtra("id",String.valueOf(noteslist.get(getAdapterPosition()).getId()));
                i.putExtra("sname",String.valueOf(noteslist.get(getAdapterPosition()).getSubjectname()));
                v.getContext().startActivity(i);

                    Toast.makeText(v.getContext(),String.valueOf(noteslist.get(getAdapterPosition()).getId()), Toast.LENGTH_LONG).show();

                }
            });

        }
    }

}
