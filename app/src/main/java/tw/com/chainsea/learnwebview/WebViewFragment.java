package tw.com.chainsea.learnwebview;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * LearnWebView
 * Created by Fleming on 2016/9/26.
 */

public class WebViewFragment extends Fragment {

    private static String sUrl;
    private WebView mWebView;
    private ProgressDialog mLoading;
    private NetworkChangeReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private TextView netErrorView;

    public static WebViewFragment newInstance(String url) {
        sUrl = url;
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_web, container, false);
        mWebView = (WebView) root.findViewById(R.id.webView);
        netErrorView = (TextView) root.findViewById(R.id.iv_network_error);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        startWebview();

        mReceiver = new NetworkChangeReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, mIntentFilter);
    }

    private void startWebview() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            netErrorView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            displayWebView();
        } else {
            mWebView.setVisibility(View.GONE);
            netErrorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    private void displayWebView() {
        //启用支持JavaScript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置加载进来的页面自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebView.setWebViewClient(new WebViewClient() {
            //返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器去打开
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }

            //WebViewClient帮助WebView去处理一些页面控制和请求通知
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideLoading();
            }
        });

        if (mWebView != null) mWebView.loadUrl(sUrl);
    }

    public void showLoading() {
        if (mLoading == null) {
            mLoading = new ProgressDialog(getActivity());
            mLoading.setMessage("Loading...");
            mLoading.show();
        }
    }

    public void hideLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
    }

    public boolean tryToGoBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();//返回上一页面
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            startWebview();
        }
    }

}
