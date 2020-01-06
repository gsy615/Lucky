package com.gz.lucky.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gz.lucky.R;
import com.gz.lucky.bean.RewardBean;
import com.gz.lucky.ui.MainActivity;

import java.util.List;

public class AutoPollRewardAdapter extends RecyclerView.Adapter<AutoPollRewardAdapter.BaseViewHolder> {
    private final Context mContext;
    private final List<RewardBean> mData;

    public AutoPollRewardAdapter(Context context, List<RewardBean> list) {
        this.mContext = context;
        this.mData = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.auto_reward_list_item, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (mData != null && mData.size() != 0) {
            if (position > mData.size() - 1 && mData.size() <= MainActivity.mMaxScroolSize) {
                holder.info.setText("");
            } else {
                RewardBean datasBean = mData.get(position % mData.size());
                String congratulations = mContext.getResources().getString(R.string.congratulations);
                String get = mContext.getResources().getString(R.string.get);
                String infoStr = congratulations + "  " + datasBean.getName() + "  " + get + datasBean.getInfo();
                holder.info.setText(infoStr);
            }
        } else {
            holder.info.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView info;

        BaseViewHolder(View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.info);
        }
    }
}
