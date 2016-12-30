package tw.com.chainsea.learnwebview;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * LearnWebView
 * Created by Fleming on 2016/9/26.
 */

public class LoadingUtils {

    private static LoadingUtils sLoading;
    private Context mContext;
    private ProgressDialog mDialog;

    private LoadingUtils(Context context) {
        mContext = context;
    }

    public static LoadingUtils getInstance(Context context) {
        if (sLoading == null) {
            sLoading = new LoadingUtils(context);
        }
        return sLoading;
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
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setProgress(currentPgs);
            mDialog.show();
        } else {
            mDialog.setProgress(currentPgs);
        }
    }
}
