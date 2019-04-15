package com.iman.keepword.adpter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iman.keepword.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private Activity activity;
    private ArrayList<String> list;
    private LayoutInflater inflater;

    public HomeAdapter(Activity activity,ArrayList<String> list){
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HomeHolder(inflater.inflate(R.layout.row_home,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder homeHolder, int i) {
        homeHolder.text.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView text;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            logo = itemView.findViewById(R.id.logo);
        }
    }
}
