package me.stupidme.cooker.view.about;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/5/16.
 */

public class AboutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseAboutBean> mDataSet;

    public AboutRecyclerAdapter(List<BaseAboutBean> data) {
        mDataSet = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == BaseAboutBean.TYPE_TITLE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_about_title, parent, false);
            viewHolder = new TitleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_about_card, parent, false);
            viewHolder = new CardViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = mDataSet.get(position).getType();
        switch (type) {
            case BaseAboutBean.TYPE_TITLE_ITEM:
                TitleViewHolder viewHolder = (TitleViewHolder) holder;
                AboutGroupItem titleBean = (AboutGroupItem) mDataSet.get(position);
                viewHolder.mGroupTitle.setText(titleBean.getData());
                break;
            case BaseAboutBean.TYPE_CARD_ITEM:
                CardViewHolder cardViewHolder = (CardViewHolder) holder;
                AboutCardItem aboutCardItem = (AboutCardItem) mDataSet.get(position);
                cardViewHolder.mCardTitle.setText(aboutCardItem.getData().getTitle());
                cardViewHolder.mCardContent.setText(aboutCardItem.getData().getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).mType;
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView mGroupTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mGroupTitle = (TextView) itemView.findViewById(R.id.about_group_title);
        }
    }

    private static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView mCardTitle;
        TextView mCardContent;

        public CardViewHolder(View itemView) {
            super(itemView);
            mCardTitle = (TextView) itemView.findViewById(R.id.about_card_title);
            mCardContent = (TextView) itemView.findViewById(R.id.about_card_content);
        }
    }
}
