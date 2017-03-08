package me.stupidme.cooker.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/5.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    private List<BookBean> mDataSet;

    public BookRecyclerAdapter(List<BookBean> list) {
        mDataSet = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id.setText("ID: " + mDataSet.get(position).getDeviceId());
        holder.name.setText("Name: " + mDataSet.get(position).getDeviceName());
        holder.place.setText("Place: " + mDataSet.get(position).getDevicePlace());
        holder.count.setText("Count: " + mDataSet.get(position).getPeopleCount());
        holder.weight.setText("Weight: " + mDataSet.get(position).getRiceWeight());
        holder.taste.setText("Taste: " + mDataSet.get(position).getTaste());
        holder.status.setText("Status: " + mDataSet.get(position).getDeviceStatus());
        holder.time.setText("TIme: " + mDataSet.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public List<BookBean> getDataSet() {
        return mDataSet;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        TextView place;
        TextView weight;
        TextView count;
        TextView taste;
        TextView status;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.book_id);
            name = (TextView) itemView.findViewById(R.id.book_name);
            place = (TextView) itemView.findViewById(R.id.book_place);
            weight = (TextView) itemView.findViewById(R.id.book_weight);
            count = (TextView) itemView.findViewById(R.id.book_people_count);
            taste = (TextView) itemView.findViewById(R.id.book_taste);
            status = (TextView) itemView.findViewById(R.id.book_status);
            time = (TextView) itemView.findViewById(R.id.book_time);
        }
    }
}
