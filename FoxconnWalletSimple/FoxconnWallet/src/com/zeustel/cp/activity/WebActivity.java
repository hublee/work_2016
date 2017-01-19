package com.zeustel.cp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.bean.JSLogic;
import com.zeustel.cp.intf.IJs;
import com.zeustel.cp.paybill.PaybillActivity;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.cp.utils.paybilling.IabHelper;
import com.zeustel.cp.utils.paybilling.IabResult;
import com.zeustel.cp.utils.paybilling.Inventory;
import com.zeustel.cp.utils.paybilling.Purchase;
import com.zeustel.foxconn.cp_sdk.R;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends Activity implements IJs {
    public static final String PARMAS_KEY_UID = "userId";
    public static final String PARMAS_KEY_SERVER = "server";
    private static WebView webView;
    private AlertDialog dialog;// 弹窗

    public static String generateUrl(String url, String uid, String server) {
        return url + "?" + PARMAS_KEY_UID + "=" + uid + "&" + PARMAS_KEY_SERVER + "=" + server;
    }

    public static String generateUrl(String url, String uid) {
        return url + "?" + PARMAS_KEY_UID + "=" + uid;
    }

    private ArrayList<String> loadHistoryUrls = new ArrayList<String>();
    private HashMap<String, String> heads;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.view_userinfo);
        Intent intent = getIntent();
        @SuppressWarnings("unchecked") HashMap<String, String> heads = (HashMap<String, String>) intent.getSerializableExtra("heads");
        String url = intent.getStringExtra("url");
        url += "&username=";
        url += Tools.getUserInfo() == null ? "" : Tools.getUserInfo().getUserName();//ZSSDK.getDefault().getUsers()==null?"":ZSSDK.getDefault().getUsers().getUserName();
        Log.e("onCreate", "url : " + url);
        initView(url, heads);
    }

    @SuppressLint("NewApi")
    private void initView(String url, HashMap<String, String> heads) {
        // TODO Auto-generated method stub
        webView = (WebView) findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        JSLogic logic = new JSLogic(this, this);
        webView.addJavascriptInterface(logic, "Logic");
        this.url = url;
        this.heads = heads;
        if (heads != null) {
            webView.loadUrl(url, heads);
        } else {
            webView.loadUrl(url);
        }

        // 设置可以弹窗
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                createDialog(message);
                return true;
            }

            @Override
            public boolean onJsTimeout() {
                Log.e("onJsTimeout", "onJsTimeout");
                return super.onJsTimeout();
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {

                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("onPageStarted", "onPageStarted " + url);
                String msdk = SdkUtils.getServerInfo().getMsdk();
                int lastIndexOf = msdk.lastIndexOf("/");
                msdk = msdk.substring(0, lastIndexOf);

                Log.e("onPageStarted", "getServerInfo " + msdk);
                if (url.startsWith(msdk) && url.endsWith(".apk")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    Log.e("onPageStarted", "startActivity " + url);
                    startActivity(intent);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("onPageFinished", "onPageFinished : " + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("onReceivedError", "onReceivedError errorCode : " + errorCode);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e("onReceivedError", "onReceivedError errorCode : " + error);
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e("onReceivedError", "onReceivedSslError errorCode : " + error);
                super.onReceivedSslError(view, handler, error);
            }

        });

        //============================================= PAY START ============================================================
        if(url.contains("/ACCOUNT.html")){ //跳到用户中心时初始化谷歌应用内支付
            String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgta6vxmvm3kd8GWj6POMrwC/YveJL1rnlWPEQM5KNqLwEuJhFiU7nksjgtmM2ir1B3IedZ9GrH5+R3R0QPZLuQCwYml0odxRU0IWBhx3elP6KxBIdJxR/4/1kVQldbAhqtSzbl5+GNrs4tdPmo27NBZ2zAp7KQ3j0w8zn5E8dZu9K5qHHAivEAzWEOIBRpmwVXdYdDX7ILAlCoqlGNL1mnR9jkGBG7+vRa3u2gQTT4sMkcC+unsah/UwXcnqNEagBP8Mg/fUCQBP91nVpyl6gQ+3c5mXXw8ai3sOOS8MatrZka97cNALPj/C2qZGF3UFVwCSK1+s8crhUfwx5Mjb0wIDAQAB";
            // Some sanity checks to see if the developer (that's you!) really followed the
            // instructions to run this sample (don't put these checks on your app!)
            if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
                throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
            }
            if (getPackageName().startsWith("com.example")) {
                throw new RuntimeException("Please change the sample's package name! See README.");
            }

            // Create the helper, passing it our context and the public key to verify signatures with
            Log.d(TAG, "Creating IAB helper.");
            mHelper = new IabHelper(this, base64EncodedPublicKey);

            // enable debug logging (for a production application, you should set this to false).
            mHelper.enableDebugLogging(true);

            // Start setup. This is asynchronous and the specified listener
            // will be called once setup completes.
            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        complain("Problem setting up in-app billing: " + result);
                        return;
                    }

                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null) return;

                    // IAB is fully set up. Now, let's get an inventory of stuff we own.
                    Log.d(TAG, "Setup successful. Querying inventory.");
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            });
        }
        //============================================= PAY END ============================================================
    }

//==================================== PAY START ==========================================================
    static final String TAG_PAY = "PAY-TEST";
    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    static final String SKU_PREMIUM = "premium";
    // Does the user have the premium upgrade?
    boolean mIsPremium = false;
    static final String SKU_GAS = "gas";
    // SKU for our subscription (infinite gas)
    static final String SKU_INFINITE_GAS = "infinite_gas";
    // The helper object
    IabHelper mHelper;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;
            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }
            Log.d(TAG, "Query inventory was successful.");
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
//                setWaitScreen(false);
                return;
            }
            /*if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }*/

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_GAS)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
            else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                mIsPremium = true;
//                updateUi();
//                setWaitScreen(false);
            }
            else if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing to infinite gas!");
//                mSubscribedToInfiniteGas = true;
//                mTank = TANK_MAX;
//                updateUi();
//                setWaitScreen(false);
            }
        }
    };


    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** PayTest Error: " + message);
        alert("Error: " + message);
    }
    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

//========================================== PAY END =======================================================

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            loadHistory();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void back() {
        Log.i(TAG,  "back (line 187): ----------");
        loadHistory();
    }

    @Override
    public void close() {
        this.finish();
    }

    public static final String TAG = WebActivity.class.getSimpleName();

    @Override
    public void copy(String val) {
        Log.i(TAG, "copy (line 205): val : " + val);
        if (!TextUtils.isEmpty(val)) {
            ClipboardManager cbm = (ClipboardManager) getApplicationContext().getSystemService(Activity.CLIPBOARD_SERVICE);
            cbm.setPrimaryClip(ClipData.newPlainText(getPackageName(), val));
        }
    }
    @Override
    public void deal(int code, String msg) {

        switch (code) {
            case Constants.TOSHOP:
                toShop();
                break;
            case Constants.TOLOGIN:
                toLogin();
                break;
            case Constants.TOREFRESH:
                toRefresh();
                break;
            case Constants.TOALERT:
                showMessage(msg);
                break;
        }
    }

    @Override
    public void pay() {
        //发起支付请求窗口
        mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST,
                mPurchaseFinishedListener, "");
    }

    /**
     * 跳转到商城
     */
    private void toShop() {
        SdkUtils.openApp(this, Constants.PACKAGE_NAME_JFT);
    }

    /**
     * 清除数据，登录页面
     */
    private void toLogin() {
        ZSSDK.getDefault().reCheck();
        finish();
    }

    /**
     * 刷新
     */
    private void toRefresh() {
        webView.reload();
    }

    private void createDialog(String message) {
        Tools.tips(this, message);
    }

    public void showMessage(String titleAndMsg) {
        if (titleAndMsg == null || titleAndMsg == "") {
            return;
        }
        String title = titleAndMsg.split("&&")[0];
        String content = titleAndMsg.split("&&")[1];
        final String jsFunction = titleAndMsg.split("&&")[2];

        String type = titleAndMsg.split("&&")[3];

        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.getWindow().setContentView(Tools.getResourse(this, "layout", "view_dialog"));
        if (title != null && title != "")
            ((TextView) dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_msg"))).setText(title);

        if (content != null && content != "")
            ((TextView) dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_content"))).setText(content);

        dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_cancel")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHandler.sendEmptyMessage(3);
            }
        });

        dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_ok")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message msg = new Message();
                msg.what = 1;
                msg.obj = jsFunction;
                mHandler.sendMessage(msg);
            }
        });

        if (type != null && type != "") {
            int flag = Integer.parseInt(type);
            ImageView tipImg = (ImageView) dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_tipsimg"));
            if (flag % 3 == 0) {
                tipImg.setVisibility(View.GONE);
            } else if (flag % 3 == 1) {
                tipImg.setImageDrawable(getResources().getDrawable(Tools.getResourse(this, "drawable", "cp_sdk_success")));
            } else {
                tipImg.setImageDrawable(getResources().getDrawable(Tools.getResourse(this, "drawable", "cp_sdk_error")));
            }
            int topImgFlag = Integer.parseInt(titleAndMsg.split("&&")[4]);

            ImageView topImg = (ImageView) dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_topimg"));
            if (topImgFlag % 3 == 0) {
                topImg.setImageResource(R.drawable.cp_sdk_dialog_top_card);
            } else if (topImgFlag % 3 == 1) {
                topImg.setImageResource(R.drawable.cp_sdk_exchange_top);
            } else {
                topImg.setImageResource(R.drawable.cp_sdk_dialog_top_addr);
            }
        }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                webView.loadUrl("javascript:" + msg.obj.toString());
                dialog.dismiss();
                return;
            }

            if (msg.what == 3) {
                dialog.dismiss();
            }
        }

        ;
    };

    /**
     * 手动加载历史
     */
    private void loadHistory() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            close();
        }
    }

}
