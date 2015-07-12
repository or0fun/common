package com.fang.common.base;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.fang.common.R;

/**
 * Created by benren.fj on 6/21/15.
 */
public class BaseListActivity extends ListActivity {
    protected Context mContext;
    private static Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        Global.context = this;
        overridePendingTransition(R.anim.right_in, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Global.context = this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.right_out);
    }

    /**
     * 显示 Toast
     * @param str
     */
    public void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mToast) {
                    mToast = Toast.makeText(mContext, str,
                            Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(str);
                }
                mToast.show();
            }
        });
    }

}
