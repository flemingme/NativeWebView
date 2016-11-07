package tw.com.chainsea.learnwebview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * LearnWebView
 * tw.com.chainsea.learnwebview.WebViewFragment
 * Created by Fleming on 2016/9/26.
 */

public class WebViewFragment extends Fragment {

    private static String sUrl;
    private WebView mWebView;

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
        View view = inflater.inflate(R.layout.web_fragment, container, false);
        displayWebView(view);
        return view;
    }

    private void displayWebView(View view) {
        mWebView = (WebView) view.findViewById(R.id.webView);
        if (mWebView != null) mWebView.loadUrl(sUrl);
        // 覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebVIew中打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                //返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器去打开
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知
        });
        //启用支持JavaScript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置加载进来的页面自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress 1-100之间的整数
                if (newProgress == 100) {
                    //网页加载完毕，关闭ProgressDialog
                    Loading.getInstance(getActivity()).hideDialog();
                } else {
                    //网页正在加载,打开ProgressDialog
                   Loading.getInstance(getActivity()).showDialog(newProgress);
                }
            }
        });
    }

    public boolean getBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();//返回上一页面
            return true;
        } else {
            return false;
        }
    }

    public void release() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
    }

}
