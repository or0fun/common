package com.fang.common.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fang.common.R;
import com.fang.common.util.StringUtil;

public class CustomSwitchPreference extends LinearLayout {
	
	protected final String TAG = "CustomSwitchPreference";
	protected Context mContext;
	
	protected String mTitleString;
	protected float mTitleTextSize;
	protected int mTitleTextColor;
	
	protected String mSummaryString;
	protected float mSummaryTextSize;
	protected int mSummaryTextColor;
	
	protected String mSwitchOn;
	protected String mKeyString;

	protected View mItemView;
	
	protected TextView mTitleTextView;
	protected TextView mSummaryTextView;
	protected CustomSlideSwitch mSlideSwitch;
	
	protected boolean mIsOn;
	
	public CustomSwitchPreference(Context context) {
		super(context);
		mContext = context;
	}

	public CustomSwitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(context, attrs);
        freshView();
	}

	public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init(context, attrs);
        freshView();
	}

    /**
     * 为开关控件设置状态改变监听函数
     *
     * @param listener
     *            参见 {@link com.fang.common.controls.CustomSlideSwitch.OnSwitchChangedListener}
     */
    public void setOnSwitchChangedListener(
            CustomSlideSwitch.OnSwitchChangedListener listener) {
        if (null != mSlideSwitch) {
            mSlideSwitch.setOnSwitchChangedListener(listener);
        }
    }

	/**
	 * 初始化
	 */
	protected void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSwitchPreference);  
        
        mTitleString = ta.getString(R.styleable.CustomSwitchPreference_title);  
        mTitleTextSize = ta.getFloat(R.styleable.CustomSwitchPreference_titleTextSize, getResources().getDimension(R.dimen.titleTextSize));
        mTitleTextColor = ta.getColor(R.styleable.CustomSwitchPreference_titleTextColor, getResources().getColor(R.color.black));

        mSummaryString = ta.getString(R.styleable.CustomSwitchPreference_summary);  
        mSummaryTextSize = ta.getFloat(R.styleable.CustomSwitchPreference_summaryTextSize, getResources().getDimension(R.dimen.subTextSize));
        mSummaryTextColor = ta.getColor(R.styleable.CustomSwitchPreference_summaryTextColor, getResources().getColor(R.color.hint));
        
        mKeyString = ta.getString(R.styleable.CustomSwitchPreference_key);
        
        mSwitchOn = ta.getString(R.styleable.CustomSwitchPreference_defaultValue);
        mIsOn = "true".equals(mSwitchOn) ? true:false;
        
        ta.recycle();
	}
	/**
	 * 初始化
	 */
	protected void freshView() {
		if (null != mItemView) {
			removeView(mItemView);
		}
		mItemView = LayoutInflater.from(mContext).inflate(R.layout.setting_item, null);
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTitleTextView = (TextView)mItemView.findViewById(R.id.title);
        mTitleTextView.setText(mTitleString);
        mSummaryTextView = (TextView)mItemView.findViewById(R.id.summary);
        if (StringUtil.isEmpty(mSummaryString)) {
        	mSummaryTextView.setVisibility(View.GONE);
		}else {
			mSummaryTextView.setText(mSummaryString);
		}
        mSlideSwitch = (CustomSlideSwitch)mItemView.findViewById(R.id.slideSwitch);
        mSlideSwitch.setKey(mKeyString);
        addView(mItemView, params);
	}
}
