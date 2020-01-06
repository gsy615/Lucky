package com.gz.lucky.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gz.lucky.ui.MainActivity;

import java.lang.ref.WeakReference;

public class AutoPollCheckRecyclerView extends RecyclerView {
    private final long TIME_AUTO_POLL = 10;
    private final int speed = 5;
    AutoPollTask autoPollTask;
    private boolean running; //标示是否正在自动轮询
    private boolean canRun;//标示是否可以自动轮询,可在不需要的是否置false
    private static int scrallX = 0;
    private static int scrallY = 0;
    private static int maxScrall = 100;
    private boolean isCheckStop = false;
    private int totalScrallX = 0;
    private int totalScrallY = 0;
    private Context mContext;

    public boolean getIsRuning() {
        return running;
    }

    public AutoPollCheckRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        autoPollTask = new AutoPollTask(this);
    }

    private class AutoPollTask implements Runnable {
        private final WeakReference<AutoPollCheckRecyclerView> mReference;

        //使用弱引用持有外部类引用->防止内存泄漏
        AutoPollTask(AutoPollCheckRecyclerView reference) {
            this.mReference = new WeakReference<>(reference);
        }

        @Override
        public void run() {
            AutoPollCheckRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                if (scrallX < maxScrall && !isCheckStop) {
                    scrallX = scrallX + speed;
                }
                if (scrallY < maxScrall && !isCheckStop) {
                    scrallY = scrallY + speed;
                }
                totalScrallX += scrallX;
                totalScrallY += scrallY;
                recyclerView.scrollBy(scrallX, scrallY);
                recyclerView.postDelayed(recyclerView.autoPollTask, TIME_AUTO_POLL);
            }
        }
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (!running) {
            canRun = true;
            running = true;
            postDelayed(autoPollTask, TIME_AUTO_POLL);
        }
    }

    public void stop() {
        running = false;
        isCheckStop = false;
        removeCallbacks(autoPollTask);
        Message msg = Message.obtain();
        msg.what = 0;
        MainActivity.mhandler.sendMessage(msg);
    }

    private int isInPosition() {
        int itemHeight = 0;
        int itemWidth = 0;
        try {
            itemHeight = getChildAt(0).getHeight();
            itemWidth = getChildAt(0).getWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (totalScrallX % itemWidth == 0 && totalScrallY % itemHeight == 0) {
            return 0;
        } else {
            return (itemHeight - totalScrallY % itemHeight);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                break;
        }
        return false;
    }

    public void stopReward() {
        isCheckStop = true;
        int flag = isInPosition();
        if (flag != 0) {
            totalScrallX += flag;
            totalScrallY += flag;
            scrollBy(flag, flag);
        }
        stop();
    }
}
