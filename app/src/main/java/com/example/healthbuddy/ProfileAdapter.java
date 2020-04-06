package com.example.healthbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    private ArrayList<String> labels;
    private ArrayList<String> data;
    private Context context;

    public ProfileAdapter(ArrayList<String> labels, ArrayList<String> data, Context context) {
        this.labels = labels;
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.profile_info,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.label.setText(labels.get(i));
        viewHolder.data.setText(data.get(i));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView label, data;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.labelInfo);
            data = (TextView) itemView.findViewById(R.id.dataInfo);
        }
    }
}
