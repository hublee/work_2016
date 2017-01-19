package com.zeustel.top9.fragments.html;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zeustel.top9.base.WrapOneFragment;

import java.lang.ref.WeakReference;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/6 11:50
 */
public class HtmlAnimFragment extends WrapOneFragment {
    private WebSettings mWebSettings;

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    public void startAnim() {

    }

    protected void initWeb(WebView webView) {
        if (webView != null) {
            mWebSettings = webView.getSettings();
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            mWebSettings.setBlockNetworkImage(true);
            mWebSettings.setDefaultTextEncodingName("UTF-8");
            webView.setWebViewClient(new ControlWebViewClient(mWebSettings));
        }
    }

    private static final class ControlWebViewClient extends WebViewClient {
        public ControlWebViewClient(WebSettings mWebSettings) {
            mWebSettingsRef = new WeakReference<WebSettings>(mWebSettings);
        }

        private final WeakReference<WebSettings> mWebSettingsRef;

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            setBlockNetworkImage();
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setBlockNetworkImage();
            super.onPageFinished(view, url);
        }

        private void setBlockNetworkImage() {
            WebSettings webSettings = mWebSettingsRef.get();
            if (webSettings != null) {
                webSettings.setBlockNetworkImage(false);
            }
        }
    }
}
