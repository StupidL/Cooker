package me.stupidme.cooker.view.status;

import android.support.design.widget.FloatingActionButton;
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
import me.stupidme.cooker.presenter.StatusPresenter;
import me.stupidme.cooker.util.ImageUtil;

/**
 * Created by StupidL on 2017/4/5.
 */

public class StatusRecyclerAdapter extends RecyclerView.Adapter<StatusRecyclerAdapter.ViewHolder> {

    private List<BookBean> mDataSet;

    private StatusPresenter mPresenter;

    public StatusRecyclerAdapter(List<BookBean> list) {
        mDataSet = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookBean book = mDataSet.get(position);
        holder.cookerIdText.setText(String.valueOf(book.getCookerId()).substring(0, 6));
        holder.cookerNameText.setText(book.getCookerName());
        holder.cookerLocationText.setText(book.getCookerLocation());
        holder.bookIdText.setText(String.valueOf(book.getBookId()).substring(0, 6));
        holder.riceWeightText.setText(String.valueOf(book.getRiceWeight()));
        holder.peopleCountText.setText(String.valueOf(book.getPeopleCount()));
        holder.tasteText.setText(book.getTaste());
        holder.fab.setOnClickListener(v -> mPresenter.cancelBook(book.getBookId()));

        Glide.with(holder.itemView.getContext())
                .load(ImageUtil.nextImageResId())
                .into(holder.imageView);
        Long time = book.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String timeStr = hour + ":" + (minute < 10 ? minute + "0" : minute);
        holder.timeText.setText(timeStr);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setPresenter(StatusPresenter presenter) {
        this.mPresenter = presenter;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private FloatingActionButton fab;
        private TextView cookerIdText;
        private TextView cookerNameText;
        private TextView cookerLocationText;
        private TextView bookIdText;
        private TextView riceWeightText;
        private TextView peopleCountText;
        private TextView tasteText;
        private TextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.status_arc_view);
            fab = (FloatingActionButton) itemView.findViewById(R.id.status_fab);
            cookerIdText = (TextView) itemView.findViewById(R.id.status_cooker_id);
            cookerNameText = (TextView) itemView.findViewById(R.id.status_cooker_name);
            cookerLocationText = (TextView) itemView.findViewById(R.id.status_cooker_location);
            bookIdText = (TextView) itemView.findViewById(R.id.status_book_id);
            riceWeightText = (TextView) itemView.findViewById(R.id.status_book_rice_weight);
            peopleCountText = (TextView) itemView.findViewById(R.id.status_book_people_count);
            tasteText = (TextView) itemView.findViewById(R.id.status_book_taste);
            timeText = (TextView) itemView.findViewById(R.id.status_book_time);
        }
    }
}
