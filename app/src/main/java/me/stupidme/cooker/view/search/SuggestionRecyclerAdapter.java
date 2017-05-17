package me.stupidme.cooker.view.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/5/15.
 */

public class SuggestionRecyclerAdapter extends RecyclerView.Adapter<SuggestionRecyclerAdapter.ViewHolder> {

    private List<BaseSuggestion> mDataSet;

    private OnSearchItemClickListener mListener;

    public SuggestionRecyclerAdapter(List<BaseSuggestion> data) {
        mDataSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mContentTextView.setText(mDataSet.get(position).getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onSearchItemClick(mDataSet.get(holder.getAdapterPosition()).getType(),
                            mDataSet.get(holder.getAdapterPosition()).getContent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getType();
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mContentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContentTextView = (TextView) itemView.findViewById(R.id.search_item_content);
        }
    }

    public interface OnSearchItemClickListener {

        void onSearchItemClick(int type, String content);
    }
}
