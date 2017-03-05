package me.stupidme.cooker.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/5.
 * CookerFragment中RecyclerView的适配器
 */

public class CookerRecyclerAdapter extends RecyclerView.Adapter<CookerRecyclerAdapter.ViewHolder> {

    private List<CookerBean> mDataSet;

    public CookerRecyclerAdapter(List<CookerBean> list) {
        mDataSet = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cooker, parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mDataSet.get(position).getName());
        holder.location.setText(mDataSet.get(position).getLocation());
        holder.status.setText(mDataSet.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public List<CookerBean> getDataSet() {
        return mDataSet;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView name;
        TextView location;
        TextView status;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            name = (TextView) itemView.findViewById(R.id.cooker_name);
            location = (TextView) itemView.findViewById(R.id.cooker_location);
            status = (TextView) itemView.findViewById(R.id.cooker_status);
        }
    }
}
