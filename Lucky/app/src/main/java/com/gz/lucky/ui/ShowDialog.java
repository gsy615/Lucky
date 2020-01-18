package com.gz.lucky.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gz.lucky.R;
import com.gz.lucky.bean.RewardBean;

import java.util.List;

public class ShowDialog extends Dialog {

    private Context mContext;
    private List<RewardBean> mData;

    public ShowDialog(Context context, int theme, List<RewardBean> data) {
        super(context, theme);
        mContext = context;
        mData = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_dialog);
        TextView mTextView = findViewById(R.id.show_dialog_tv);

        String[] rewardTypeStr = mContext.getResources().getStringArray(R.array.reward_type);
        StringBuilder[] contents = new StringBuilder[rewardTypeStr.length];
        int[] indexs = new int[rewardTypeStr.length];
        for (int i = 0; i < mData.size(); i++) {
            for (int j = 0; j < rewardTypeStr.length; j++) {
                if (rewardTypeStr[j].equals(mData.get(i).getInfo())) {
                    if (contents[j] == null) {
                        contents[j] = new StringBuilder();
                        contents[j].append(rewardTypeStr[j]).append("\n\r");
                    }
                    contents[j].append(mData.get(i).getName()).append("  ");
                    indexs[j]++;
                    if (indexs[j] % 10 == 0) {
                        contents[j].append("\n\r");
                    }
                }
            }

        }
        StringBuilder content = new StringBuilder();
        for (int i = contents.length - 1; i >= 0; i--) {
            if (contents[i] != null) {
                content.append(contents[i]).append("\n\r\n\r");
            }
        }
        mTextView.setText(content.toString());
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
