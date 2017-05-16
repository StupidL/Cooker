package me.stupidme.cooker.view.cooker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.util.ImageUtil;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cooker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mDataSet.get(position).getCookerName());
        holder.location.setText(mDataSet.get(position).getCookerLocation());
        holder.status.setText(mDataSet.get(position).getCookerStatus().toUpperCase());

        Glide.with(holder.itemView.getContext())
                .load(ImageUtil.nextImageResId())
                .placeholder(ImageUtil.placeHolder())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public List<CookerBean> getDataSet() {
        return mDataSet;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView location;
        TextView status;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.cooker_image);
            name = (TextView) itemView.findViewById(R.id.cooker_name);
            location = (TextView) itemView.findViewById(R.id.cooker_location);
            status = (TextView) itemView.findViewById(R.id.cooker_status);
        }
    }
}
