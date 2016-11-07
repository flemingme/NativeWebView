package tw.com.chainsea.learnwebview;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * LearnWebView
 * Created by Fleming on 2016/9/26.
 */

public class Loading {

    private static Loading mLoading;
    private Context mContext;
    private ProgressDialog mDialog;

    private Loading(Context context) {
        mContext = context;
    }

    public static Loading getInstance(Context context) {
        if (mLoading == null) {
            mLoading = new Loading(context);
        }
        return mLoading;
    }

    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void showDialog(int currentPgs) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setTitle("加载中……");
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setProgress(currentPgs);
            mDialog.show();
        } else {
            mDialog.setProgress(currentPgs);
        }
    }
}
