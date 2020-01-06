package com.gz.lucky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gz.lucky.bean.CheckBean;
import com.gz.lucky.R;

import java.util.List;

public class AutoPollCheckAdapter extends RecyclerView.Adapter<AutoPollCheckAdapter.BaseViewHolder> {
    private final Context mContext;
    private final List<CheckBean> mData;

    public AutoPollCheckAdapter(Context context, List<CheckBean> list) {
        this.mContext = context;
        this.mData = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.auto_check_list_item, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (mData != null && mData.size() != 0) {
            CheckBean datasBean = mData.get(position % mData.size());
            int icon = mContext.getResources().getIdentifier(datasBean.getImage(), "drawable", mContext.getPackageName());
            holder.icon.setImageResource(icon);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        BaseViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
