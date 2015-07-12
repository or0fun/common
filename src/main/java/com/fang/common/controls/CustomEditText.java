package com.fang.common.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fang.common.R;
import com.fang.common.util.BaseUtil;

/**
 * Created by benren.fj on 6/9/15.
 */
public class CustomEditText extends LinearLayout {

    private EditText editText;
    private View clearBtn;

    private String hint;
    private boolean clearBtnVisiable;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    protected void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);

        hint = ta.getString(R.styleable.CustomEditText_hintText);
        clearBtnVisiable = ta.getBoolean(R.styleable.CustomEditText_clearBtn, true);

        ta.recycle();

        LayoutInflater.from(context).inflate(R.layout.custom_edit, this, true);

        editText = (EditText) findViewById(R.id.searchEdit);
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()) {
                    clearFocus();  //在滑动设备列表的时候，editview无法弹出软键盘
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    clearBtn.setVisibility(GONE);
                } else {
                    showClearBtn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                BaseUtil.showKeyBoard(editText, true);
            }
        });
        if (null != hint) {
            editText.setHint(hint);
        }
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public Editable getText() {
        return editText.getText();
    }

    public void addTextChangedListener(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    public void setOnKeyListener(OnKeyListener listener) {
        editText.setOnKeyListener(listener);
    }

    private void showClearBtn() {
        if (clearBtnVisiable) {
            clearBtn.setVisibility(VISIBLE);
        } else {
            clearBtn.setVisibility(GONE);
        }
    }

    public EditText getEditText() {
        return editText;
    }
}
