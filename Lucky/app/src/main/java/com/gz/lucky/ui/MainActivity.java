package com.gz.lucky.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.lucky.R;
import com.gz.lucky.adapter.AutoPollCheckAdapter;
import com.gz.lucky.adapter.AutoPollRewardAdapter;
import com.gz.lucky.bean.CheckBean;
import com.gz.lucky.bean.RewardBean;
import com.gz.lucky.util.SharedPreferencesUtil;
import com.gz.lucky.view.AutoPollCheckRecyclerView;
import com.gz.lucky.view.AutoPollRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoPollCheckRecyclerView mCheckRecyclerView;
    private AutoPollRecyclerView mRewardRecyclerView;
    private List<CheckBean> mCheckInfoList;
    private List<RewardBean> mRewardInfoList;
    private AutoPollRewardAdapter mRewardAdapter;
    private AutoPollCheckAdapter mCheckAdapter;
    private Button mStartBtn;
    private TextView mCheckTv;
    private LinearLayout mSettingLayout;
    private FrameLayout mRewardLayout;
    private RadioGroup mRewardRg;
    private RadioGroup mMemberRg;
    private ImageView mRewardTypeTitleIm;
    private Button mClearAllBtn;
    private int mRewardType = 0;
    private int mMemberType = 0;
    public static Handler mhandler = null;
    private List<CheckBean> mAllMemberInfoList;
    private String[] mRewardTypeStr;
    private String[] mMemberTypeStr;
    private TextView[] mRewardTypeTvs;
    private TextView[] mMemberTypeTvs;
    private RadioButton[] mRewardTypeRBs;
    private RadioButton[] mMemberTypeRBs;
    public static int mMaxScroolSize = 10;

    @SuppressLint({"NewApi", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉顶部标题
        Objects.requireNonNull(getSupportActionBar()).hide();
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mhandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0) {
                    updateRewardUI();
                }
                super.handleMessage(msg);
            }
        };

        initView();
        initMember();
        initReward();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        mCheckInfoList = new ArrayList<>();
        mRewardInfoList = new ArrayList<>();
        mAllMemberInfoList = new ArrayList<>();

        mCheckRecyclerView = findViewById(R.id.check_rv);
        mCheckRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRewardRecyclerView = findViewById(R.id.reward_rv);
        mRewardRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mStartBtn = findViewById(R.id.start_btn);
        mStartBtn.setOnClickListener(this);
        mCheckTv = findViewById(R.id.check_tv);
        mCheckTv.setVisibility(View.INVISIBLE);
        findViewById(R.id.setting_btn).setOnClickListener(this);
        mSettingLayout = findViewById(R.id.setting_ll);
        mClearAllBtn = findViewById(R.id.clearall_btn);
        mClearAllBtn.setOnClickListener(this);
        mRewardRg = findViewById(R.id.reward_rg);
        mMemberRg = findViewById(R.id.num_rg);
        mRewardTypeTitleIm = findViewById(R.id.reward_type_im);
        mRewardLayout = findViewById(R.id.reward_ll);

        final Button switchOffBtn = findViewById(R.id.switch_btn_off);
        final Button switchOnBtn = findViewById(R.id.switch_btn_on);
        switchOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRewardLayout.setVisibility(View.VISIBLE);
                switchOffBtn.setVisibility(View.INVISIBLE);
                switchOnBtn.setVisibility(View.VISIBLE);
            }
        });

        switchOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRewardLayout.setVisibility(View.INVISIBLE);
                switchOffBtn.setVisibility(View.VISIBLE);
                switchOnBtn.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.reward_title_im).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShowDialog(MainActivity.this, R.style.show_dialog, mRewardInfoList).show();
            }
        });

        LinearLayout mOverRewardNumLayout = findViewById(R.id.over_reward_num_ll);
        LinearLayout mOverMemberNumLayout = findViewById(R.id.over_member_num_ll);
        mRewardTypeStr = this.getResources().getStringArray(R.array.reward_type);
        mMemberTypeStr = this.getResources().getStringArray(R.array.member_type);
        mRewardTypeTitleIm.setImageDrawable(getResources().getDrawable(R.drawable.winning_5));
        mRewardTypeTvs = new TextView[mRewardTypeStr.length];
        for (int i = 0; i < mRewardTypeStr.length; i++) {
            mRewardTypeTvs[i] = createTv(mRewardTypeStr[i]);
            mOverRewardNumLayout.addView(mRewardTypeTvs[i]);
        }
        mMemberTypeTvs = new TextView[mMemberTypeStr.length];
        for (int i = 0; i < mMemberTypeStr.length; i++) {
            mMemberTypeTvs[i] = createTv(mMemberTypeStr[i]);
            mOverMemberNumLayout.addView(mMemberTypeTvs[i]);
        }

        initRewardRadioGroup();
        initMemberRadioGroup();
    }

    private TextView createTv(String content) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, 0);
        params.weight = 1.0f;
        params.setMargins(5, 5, 5, 5);
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(R.color.text_color1));
        textView.setText(content);
        return textView;
    }

    private void initRewardRadioGroup() {
        int radioId = 1991;
        mRewardTypeRBs = new RadioButton[mRewardTypeStr.length];
        for (int i = 0; i < mRewardTypeStr.length; i++) {
            mRewardTypeRBs[i] = createRb(radioId + i, mRewardTypeStr[i], i == 0);
            mRewardRg.addView(mRewardTypeRBs[i]);
        }
        mRewardRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                mRewardType = id - 1991;
                switch (mRewardType) {
                    case 0:
                        mRewardTypeTitleIm.setImageDrawable(getResources().getDrawable(R.drawable.winning_5));
                        break;
                    case 1:
                        mRewardTypeTitleIm.setImageDrawable(getResources().getDrawable(R.drawable.winning_4));
                        break;
                    case 2:
                        mRewardTypeTitleIm.setImageDrawable(getResources().getDrawable(R.drawable.winning_3));
                        break;
                    case 3:
                        mRewardTypeTitleIm.setImageDrawable(getResources().getDrawable(R.drawable.winning_2));
                        break;
                    case 4:
                        mRewardTypeTitleIm.setImageDrawable(getResources().getDrawable(R.drawable.winning_1));
                        break;
                    default:
                        mRewardTypeTitleIm.setImageDrawable(getResources().getDrawable(R.drawable.winning_5));
                        break;
                }

            }
        });
    }

    private void initMemberRadioGroup() {
        int radioId = 2020;
        mMemberTypeRBs = new RadioButton[mMemberTypeStr.length];
        for (int i = 0; i < mMemberTypeStr.length; i++) {
            mMemberTypeRBs[i] = createRb(radioId + i, mMemberTypeStr[i], i == 0);
            mMemberRg.addView(mMemberTypeRBs[i]);
        }
        mMemberRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                mMemberType = id - 2020;
                updateMember();
            }
        });
    }

    private RadioButton createRb(int id, String content, boolean isChecked) {
        RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button_item, null);
        radioButton.setId(id);
        radioButton.setText(content);
        radioButton.setTextColor(getResources().getColor(R.color.text_color1));
        radioButton.setButtonDrawable(getResources().getDrawable(R.drawable.radio_btn_selector));
        radioButton.setChecked(isChecked);
        radioButton.setBackground(null);
        return radioButton;
    }

    private void initMember() {
        try {
            JSONObject jsonObject = getJSONObject();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            mAllMemberInfoList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String name = json.getString("name");
                String icon = json.getString("icon");
                String id = json.getString("id");
                mAllMemberInfoList.add(new CheckBean(name, icon, id, false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateMember();
    }

    public void initReward() {
        mRewardInfoList.clear();
        if (mRewardAdapter == null) {
            mRewardAdapter = new AutoPollRewardAdapter(getApplicationContext(), mRewardInfoList);
            mRewardRecyclerView.setAdapter(mRewardAdapter);
        }
        reverdInfo();
        if (mRewardInfoList.size() > mMaxScroolSize) {
            mRewardRecyclerView.start();
        } else {
            mRewardRecyclerView.stop();
            mRewardRecyclerView.scrollToPosition(0);
            mRewardAdapter.notifyDataSetChanged();
        }

        //更新设置中奖人数信息
        updateSettingUI();
    }

    private void reverdInfo() {
        Set<String> rewardSet = new HashSet<>();

        rewardSet = SharedPreferencesUtil.getSharedPreferencesSet(this, "rewardSet", rewardSet);
        if (rewardSet.size() > 0) {
            String[] rewardArr = new String[rewardSet.size()];
            for (String str : rewardSet) {
                String[] sets = str.split("_");
                rewardArr[Integer.parseInt(sets[4])] = str;
            }
            for (String str : rewardArr) {
                String[] sets = str.split("_");
                updateRewardData(sets[0], sets[1], sets[2], 0);
            }
        }
    }

    private void updateMember() {
        if (mMemberType == 0) {
            mCheckInfoList.clear();
            for (int i = 0; i < mAllMemberInfoList.size(); i++) {
                if (!mAllMemberInfoList.get(i).isReward()) {
                    mCheckInfoList.add(mAllMemberInfoList.get(i));
                }
            }
        } else {
            mCheckInfoList.clear();
            mCheckInfoList.addAll(mAllMemberInfoList);
            //乱序
            Collections.shuffle(mCheckInfoList);
        }
        if (mCheckAdapter == null) {
            mCheckAdapter = new AutoPollCheckAdapter(getApplicationContext(), mCheckInfoList);
            mCheckRecyclerView.setAdapter(mCheckAdapter);
        }
    }

    public void updateRewardUI() {
        if (mCheckRecyclerView != null && mCheckRecyclerView.getChildCount() > 0) {
            int currentPosition = ((RecyclerView.LayoutParams) mCheckRecyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
            if (mCheckInfoList.size() > 0) {
                int index = currentPosition % mCheckInfoList.size();
                CheckBean checkBean = mCheckInfoList.get(index);
                //头像下面显示中奖人信息
                mCheckTv.setText(checkBean.getName());
                mCheckTv.setVisibility(View.VISIBLE);

                updateRewardData(checkBean.getName(), mRewardTypeStr[mRewardType], checkBean.getId(), System.currentTimeMillis());
            }
        }
        mStartBtn.setEnabled(true);
        setBtnEnabled(true);
    }

    public void updateRewardData(String name, String RewardType, String id, long time) {
        //更新奖励展示UI
        mRewardInfoList.add(new RewardBean(name, RewardType, id, time));
        mRewardAdapter.notifyItemChanged(mRewardInfoList.size() - 1);
        if (mRewardInfoList.size() > mMaxScroolSize) {
            mRewardRecyclerView.start();
            mRewardAdapter.notifyDataSetChanged();
        }

        if (time != 0) {
            //存储中奖信息到本地数据永久化
            Set<String> rewardSet = new HashSet<>();
            for (int i = 0; i < mRewardInfoList.size(); i++) {
                RewardBean rewardBean = mRewardInfoList.get(i);
                rewardSet.add(rewardBean.getName() + "_" + rewardBean.getInfo() + "_" + rewardBean.getId() + "_" + rewardBean.getTime() + "_" + i);
            }
            SharedPreferencesUtil.editorSharedPreferences(this, "rewardSet", rewardSet);
        }

        //更新全员获奖信息
        for (int i = 0; i < mAllMemberInfoList.size(); i++) {
            if (mAllMemberInfoList.get(i).getId().equals(id)) {
                mAllMemberInfoList.get(i).setReward(true);
                break;
            }
        }

        //删除奖池中中奖人信息
        if (mMemberType == 0) {
            Iterator<CheckBean> iterator = mCheckInfoList.iterator();
            while (iterator.hasNext()) {
                CheckBean value = iterator.next();
                if (value.getId().equals(id)) {
                    iterator.remove();
                }
            }
        }

        //更新设置中奖人数信息
        updateSettingUI();
    }

    public void setBtnEnabled(boolean flag) {
        mSettingLayout.setEnabled(flag);
        mRewardRg.setEnabled(flag);
        mMemberRg.setEnabled(flag);
        mClearAllBtn.setEnabled(flag);
        for (RadioButton mRewardTypeRB : mRewardTypeRBs) {
            mRewardTypeRB.setEnabled(flag);
        }
        for (RadioButton mMemberTypeRB : mMemberTypeRBs) {
            mMemberTypeRB.setEnabled(flag);
        }
    }

    private void updateSettingUI() {
        String over = getResources().getString(R.string.over_reward);
        String num = getResources().getString(R.string.num);

        //设置奖项人数
        int[] numType = new int[mRewardTypeStr.length];
        for (int i = 0; i < mRewardInfoList.size(); i++) {
            String type = mRewardInfoList.get(i).getInfo();
            for (int j = 0; j < mRewardTypeStr.length; j++) {
                if(type.equals(mRewardTypeStr[j])) {
                    numType[j]++;
                }
            }
        }
        for (int i = 0; i < numType.length; i++) {
            String content = over + numType[i] + num;
            mRewardTypeTvs[i].setText(content);
        }

        //设置成员人数
        int flag = 0;
        for (int i = 0; i < mAllMemberInfoList.size(); i++) {
            if (!mAllMemberInfoList.get(i).isReward()) {
                flag++;
            }
        }
        String noneStr = mMemberTypeStr[0] + flag + num;
        String allStr = mMemberTypeStr[1] + mAllMemberInfoList.size() + num;
        mMemberTypeTvs[0].setText(noneStr);
        mMemberTypeTvs[1].setText(allStr);
    }

    long startTime = 0;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_btn) {
            if (!mCheckRecyclerView.getIsRuning()) {
                if (mCheckInfoList.size() == 0) {
                    Toast.makeText(this, "请修改奖池人员名单，全员已中奖", Toast.LENGTH_SHORT).show();
                    return;
                }
                startTime = System.currentTimeMillis();
                mCheckRecyclerView.start();
                mStartBtn.setBackgroundResource(R.drawable.stop_btn_selector);
                mCheckTv.setVisibility(View.INVISIBLE);
                //乱序
                Collections.shuffle(mCheckInfoList);
                mCheckAdapter.notifyDataSetChanged();
                setBtnEnabled(false);
            } else {
                if (System.currentTimeMillis() - startTime > 100) {
                    mCheckRecyclerView.stopReward();
                    mStartBtn.setBackgroundResource(R.drawable.start_btn_selector);
                    mStartBtn.setEnabled(false);
                }
            }
            mSettingLayout.setVisibility(View.INVISIBLE);
        }
        if (view.getId() == R.id.setting_btn) {
            mSettingLayout.setVisibility((mSettingLayout.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE);
        }
        if (view.getId() == R.id.clearall_btn) {
            createClearDialog();
        }
    }

    private JSONObject getJSONObject() {
        StringBuilder stringBuilder = new StringBuilder();
        JSONObject jsonObject = null;
        try {
            AssetManager assetManager = getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("info.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void createClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("您确定要清空中奖信息么？清空以后数据将永久丢失");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAllData();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearAllData() {
        Set<String> set = new HashSet<>();
        SharedPreferencesUtil.editorSharedPreferences(this, "rewardSet", set);
        initMember();
        initReward();
        mRewardAdapter.notifyDataSetChanged();
    }
}
