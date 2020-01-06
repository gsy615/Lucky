package com.gz.lucky.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.gz.lucky.R;
import com.gz.lucky.bean.RewardBean;

import java.util.List;

public class ShowDialog extends Dialog {

    private Context mContext;
    private TextView mTextView;
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
        mTextView = findViewById(R.id.show_dialog_tv);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < mData.size(); i++) {
            content.append(mData.get(i).getName()).append(mData.get(i).getInfo()).append("  ");
            if (i % 6 == 0 && i != 0) {
                content.append("\n\r");
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
