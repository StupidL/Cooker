package me.stupidme.cooker.view.book;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.util.ImageUtil;

/**
 * Created by StupidL on 2017/3/5.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    private List<BookBean> mDataSet;

    private OnItemClickListener mListener;

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
        BookBean bookBean = mDataSet.get(position);
        String bookId = bookBean.getBookId().toString().toUpperCase().substring(1, 6);
        String cookerId = bookBean.getCookerId().toString().toUpperCase();
        String name = bookBean.getCookerName().toUpperCase();
        String location = bookBean.getCookerLocation().toUpperCase();
        String count = String.valueOf(bookBean.getPeopleCount()).toUpperCase();
        String weight = String.valueOf(bookBean.getRiceWeight()).toUpperCase();
        String taste = bookBean.getTaste().toUpperCase();
        String status = bookBean.getCookerStatus().toUpperCase();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(bookBean.getTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String time = String.valueOf(hour) + ":" + (minute < 10 ? "0" + minute : minute);

        holder.id.setText(bookId);
        holder.cookerId.setText(cookerId);
        holder.name.setText(name);
        holder.place.setText(location);
        holder.count.setText(count);
        holder.weight.setText(weight);
        holder.taste.setText(taste);
        holder.status.setText(status);
        holder.time.setText(time);

        Glide.with(holder.itemView.getContext())
                .load(ImageUtil.nextImageResId())
                .placeholder(ImageUtil.placeHolder())
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onClick(holder.getAdapterPosition(), bookBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public List<BookBean> getDataSet() {
        return mDataSet;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView cookerId;
        TextView name;
        TextView place;
        TextView weight;
        TextView count;
        TextView taste;
        TextView status;
        TextView time;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.book_id);
            cookerId = (TextView) itemView.findViewById(R.id.book_cooker_id);
            name = (TextView) itemView.findViewById(R.id.book_name);
            place = (TextView) itemView.findViewById(R.id.book_place);
            weight = (TextView) itemView.findViewById(R.id.book_weight);
            count = (TextView) itemView.findViewById(R.id.book_people_count);
            taste = (TextView) itemView.findViewById(R.id.book_taste);
            status = (TextView) itemView.findViewById(R.id.book_status);
            time = (TextView) itemView.findViewById(R.id.book_time);
            image = (ImageView) itemView.findViewById(R.id.book_image);
        }
    }

    public interface OnItemClickListener {

        void onClick(int position, BookBean bookBean);
    }
}
