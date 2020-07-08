package com.example.notesapp.adapters;

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

import com.example.notesapp.MainActivity;
import com.example.notesapp.R;
import com.example.notesapp.model.subject;

import java.util.ArrayList;
import java.util.List;

public class adapterSubject extends RecyclerView.Adapter< adapterSubject.ViewHolder> implements Filterable  {

    LayoutInflater inflater;
    List<subject> subjectlist =new ArrayList<>();
    List<subject> subjectlistFull =new ArrayList<>();

    public adapterSubject(Context context, List<subject> subjectlist){
        this.inflater = LayoutInflater.from(context);
        this.subjectlist = subjectlist;
        subjectlistFull = new ArrayList<subject>(subjectlist);

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
            Log.d("searchresults", String.valueOf(results.values));

            subjectlist.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView subtitle;


        public ViewHolder(@NonNull View itemView) {
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
