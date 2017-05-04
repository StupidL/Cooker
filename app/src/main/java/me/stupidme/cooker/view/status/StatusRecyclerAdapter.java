package me.stupidme.cooker.view.status;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.presenter.StatusPresenter;
import me.stupidme.cooker.view.custom.ArcView;

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
        holder.cookerIdText.setText(String.valueOf(book.getCookerId()));
        holder.cookerNameText.setText(book.getCookerName());
        holder.cookerLocationText.setText(book.getCookerLocation());
        holder.bookIdText.setText(String.valueOf(book.getBookId()));
        holder.riceWeightText.setText(String.valueOf(book.getRiceWeight()));
        holder.peopleCountText.setText(String.valueOf(book.getPeopleCount()));
        holder.tasteText.setText(book.getTaste());
        holder.timeText.setText(book.getTime() + "");
        holder.arcView.setArcAngleStart(-90);
        holder.fab.setOnClickListener(v -> mPresenter.cancelBook(book.getBookId()));

        //TODO set sweep angle fro arc view

        holder.arcView.setArcAngleSweep(220);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setPresenter(StatusPresenter presenter) {
        this.mPresenter = presenter;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ArcView arcView;
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
            arcView = (ArcView) itemView.findViewById(R.id.status_arc_view);
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
