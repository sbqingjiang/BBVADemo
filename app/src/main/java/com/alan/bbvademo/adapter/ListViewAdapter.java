package com.alan.bbvademo.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alan.bbvademo.R;
import com.alan.bbvademo.model.BBVA;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yinqingjiang on 8/13/17.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private Context mContext;
    private List<BBVA> list;

    public ListViewAdapter(Context mContext, List<BBVA> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BBVA data=list.get(position);
        holder.name.setText(data.getName());
        holder.address.setText(data.getAddress());
        Picasso.with(mContext).load(data.getIcon()).into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;
        private ImageView icon;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tv_name);
            address=itemView.findViewById(R.id.tv_address);
            icon=itemView.findViewById(R.id.image_icon);
        }
    }
}
