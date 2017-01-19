package com.zeustel.cp.wallet;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.bean.AccountInfo;
import com.zeustel.cp.bean.Platform;
import com.zeustel.cp.intf.NextCallBack;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.SharedPreferencesUtils;
import com.zeustel.cp.utils.UserManager;
import com.zeustel.cp.wallet.fragments.BlankFragment;
import com.zeustel.cp.wallet.interfaces.OnAccountBoundListener;
import com.zeustel.cp.wallet.interfaces.OnExitListener;
import com.zeustel.cp.wallet.interfaces.OnGameStartListener;
import com.zeustel.cp.wallet.interfaces.OnLoginListener;
import com.zeustel.cp.wallet.interfaces.OnNextListener;
import com.zeustel.cp.wallet.interfaces.OnRegisterAccountListener;
import com.zeustel.cp.wallet.views.AccountBoundView;
import com.zeustel.cp.wallet.views.BoundSuccessView;
import com.zeustel.cp.wallet.views.ForgetEmailView;
import com.zeustel.cp.wallet.views.LoginMainView;
import com.zeustel.cp.wallet.views.LoginNormalView;
import com.zeustel.cp.wallet.views.LoginNormalView.OnLoginNormalListener;
import com.zeustel.cp.wallet.views.LoginOtherView;
import com.zeustel.cp.wallet.views.NewPasswordView;
import com.zeustel.cp.wallet.views.QuickLoginView;
import com.zeustel.cp.wallet.views.RegisterAffirmView;
import com.zeustel.cp.wallet.views.RegisterNormalView;
import com.zeustel.cp.wallet.views.RegisterOtherView;
import com.zeustel.cp.wallet.views.RegisterSuccessView;
import com.zeustel.foxconn.cp_sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WalletActivity extends AppCompatActivity implements OnExitListener, OnRegisterAccountListener, OnLoginListener, OnAccountBoundListener, OnGameStartListener, OnNextListener, OnLoginNormalListener, OnItemSelectedListener, GraphRequest.GraphJSONObjectCallback {
    //保存账户 前缀
    public static final String ACCOUNT_PREFIX = "ACCOUNT_";
    //facebook 回调管理
    CallbackManager callbackManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private BlankFragment quickLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wallet);
        facebookInit();
        ZSSDK.getDefault().getiCommand().updateContext(this);
        final BlankFragment loginMainFragment = BlankFragment.newInstance(LoginMainView.class);
        addFragment(loginMainFragment);
        loginMainFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
            @Override
            public void onViewCreated() {
                LoginMainView loginMainview = (LoginMainView) loginMainFragment.getView();
                if (loginMainview != null) {
                    loginMainview.setOnExitListener(WalletActivity.this);
                    loginMainview.setOnRegisterAccountListener(WalletActivity.this);
                    loginMainview.setOnLoginListener(WalletActivity.this);
                    final List<AccountInfo> accountInfosHistory = getAccountInfosHistory();
                    if (accountInfosHistory == null || accountInfosHistory.isEmpty()) {
                        loginMainview.hasHistory(false);
                    } else {
                        loginMainview.hasHistory(true);
                    }
                }
            }
        });
    }

    /**
     * 初始化facebook并设置监听
     */
    private void facebookInit() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookLogin();
            }

            @Override
            public void onCancel() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(WalletActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
    }

    /**
     * 登录
     */
    private void facebookLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        } else {
            requestFacebookPro(accessToken);
        }

    }

    /**
     * token获取用户信息
     *
     * @param accessToken
     */
    private void requestFacebookPro(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * 登录回调
     *
     * @param object
     * @param arg1
     */
    @Override
    public void onCompleted(JSONObject object, GraphResponse arg1) {
        if (arg1 != null && arg1.getError() != null) {
            if (arg1.getError().getErrorCode() == FacebookRequestError.INVALID_HTTP_STATUS_CODE) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                Toast.makeText(WalletActivity.this,"net error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (object == null) {
            return;
        }
        try {
            String id = null;
            String name = null;
            String email = null;

            if (object.has("id")) {
                id = object.getString("id");

            }
            if (object.has("name")) {
                name = object.getString("name");

            }
            if (object.has("email")) {
                email = object.getString("email");

            }

            if (TextUtils.isEmpty(name)) {
                LoginManager.getInstance().resolveError(this, arg1);
                return;
            }
            /**/
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setId(id);
            accountInfo.setAccount(name);
            accountInfo.setEmail(email);
            accountInfo.setInfoJson(object.toString());
            accountInfo.setAccountIcon(R.drawable.facebook_icon);
            accountInfo.setPlatform(Platform.FACEBOOK);
            accountBound(accountInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            LoginManager.getInstance().resolveError(this, arg1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onExit(View view) {
        if (view instanceof LoginMainView) {
            finish();
        } else {
            goBack();
        }
    }

    @Override
    public void onRegisterAccount(View view, final Object... args) {
        if (view instanceof LoginMainView) {
            final BlankFragment registNormalFragment = BlankFragment.newInstance(RegisterNormalView.class);
            replaceFragment(registNormalFragment);
            registNormalFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                @Override
                public void onViewCreated() {
                    RegisterNormalView registerNormalView = (RegisterNormalView) registNormalFragment.getView();
                    registerNormalView.setOnNextListener(WalletActivity.this);
                    registerNormalView.setOnExitListener(WalletActivity.this);
                }
            });
        } else if (view instanceof AccountBoundView) {
            final BlankFragment registOtherFragment = BlankFragment.newInstance(RegisterOtherView.class);
            replaceFragment(registOtherFragment);
            registOtherFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                @Override
                public void onViewCreated() {
                    final RegisterOtherView registerOtherView = (RegisterOtherView) registOtherFragment.getView();
                    AccountInfo accountInfo;
                    if (args != null && args[0] != null) {
                        accountInfo = (AccountInfo) args[0];
                        registerOtherView.setAccountInfo(accountInfo);
                    }
                    registerOtherView.setOnNextListener(WalletActivity.this);
                    registerOtherView.setOnExitListener(WalletActivity.this);
                }
            });
        }
    }

    private void addFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().add(R.id.wallet_container, fragment).commit();
    }

    private void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, true);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.wallet_container, fragment).commit();
    }

    private void goBack() {
        getFragmentManager().popBackStack();
    }


    /**
     * 获取账号登录历史
     */
    private List<AccountInfo> getAccountInfosHistory() {
        Gson gson = new Gson();
        final Map<String, ?> allPreferences = SharedPreferencesUtils.getAllPreferences(WalletActivity.this);
        if (allPreferences != null && !allPreferences.isEmpty()) {
            final Set<String> strings = allPreferences.keySet();
            if (strings != null && !strings.isEmpty()) {
                List<AccountInfo> accountInfos = new ArrayList<AccountInfo>();
                for (String key : strings) {
                    if (key.startsWith(ACCOUNT_PREFIX)) {
                        final String preferences = SharedPreferencesUtils.getPreferences(WalletActivity.this, key);
                        final AccountInfo accountInfo = gson.fromJson(preferences, AccountInfo.class);
                        accountInfos.add(accountInfo);
                    }
                }
                return accountInfos;
            }
        }
        return null;
    }

    @Override
    public void onLogin(View view, LoginType loginType, Object... args) {
        switch (loginType) {
            case LOGIN_QUICK:
                quickLoginFragment = BlankFragment.newInstance(QuickLoginView.class);
                replaceFragment(quickLoginFragment);
                quickLoginFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                    @Override
                    public void onViewCreated() {
                        final QuickLoginView quickLoginView = (QuickLoginView) quickLoginFragment.getView();
                        quickLoginView.prepare();
                        quickLoginView.setOnItemSelectedListener(WalletActivity.this);
                        quickLoginView.setOnAccountBoundListener(WalletActivity.this);
                        quickLoginView.setOnExitListener(WalletActivity.this);
                        quickLoginView.setOnGameStartListener(WalletActivity.this);
                        quickLoginView.setOnLoginListener(WalletActivity.this);
                        final List<AccountInfo> accountInfosHistory = getAccountInfosHistory();
                        quickLoginView.setAccountInfos(accountInfosHistory);
                    }
                });

                break;
            case LOGIN_MEMBER:
                if (view instanceof AccountBoundView) {
                    AccountInfo accountInfo;
                    if (args != null && args[0] != null) {
                        accountInfo = (AccountInfo) args[0];
                        toLoginOtherFragment(accountInfo);
                    }
                } else {
                    toLoginNormalFragment();
                }
                break;
            case LOGIN_FACEBOOK:
                facebookLogin();
                break;
            case LOGIN_MIGME:
                accountBound(null);
                break;
        }
    }

    /**
     * 跳转到绑定账号界面
     *
     * @param accountInfo
     */
    private void accountBound(final AccountInfo accountInfo) {

        ZSSDK.getDefault().validBind(accountInfo.getId(), accountInfo.getPlatform().platform(), new NextCallBack() {
            @Override
            public void callBack(int code, String msg) {
                switch (code) {
                    case ZSStatusCode.SUCCESS:
                        boolean isBind = false;
                        try {
                            final Integer integer = Integer.valueOf(msg);
                            isBind = integer == 1;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        final boolean isBind_ = isBind;
                        final BlankFragment accountBoundFragment = BlankFragment.newInstance(AccountBoundView.class);
                        replaceFragment(accountBoundFragment);
                        accountBoundFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                            @Override
                            public void onViewCreated() {
                                final AccountBoundView accountBoundView = (AccountBoundView) accountBoundFragment.getView();
                                accountBoundView.setAccountInfo(accountInfo);
                                accountBoundView.setBind(isBind_);
                                accountBoundView.setOnGameStartListener(WalletActivity.this);
                                accountBoundView.setOnExitListener(WalletActivity.this);
                                accountBoundView.setOnRegisterAccountListener(WalletActivity.this);
                                accountBoundView.setOnLoginListener(WalletActivity.this);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onAccountBound(View view, Object... args) {
        if (view instanceof QuickLoginView) {
            accountBound(null);
        }
    }

    private void gameStarted() {
        finish();
    }

    @Override
    public void onGameStartClick(final View view, Object... args) {
        if (view instanceof AccountBoundView || view instanceof BoundSuccessView) {
            if (args != null && args[0] != null) {
                AccountInfo accountInfo2 = (AccountInfo) args[0];
                thirdLogin(accountInfo2);
            }

        } else if (view instanceof QuickLoginView) {
            if (args != null && args[0] != null) {
                final AccountInfo accountInfo2 = (AccountInfo) args[0];
                ZSSDK.getDefault().tokenLogin(accountInfo2.getToken(), new NextCallBack() {
                    @Override
                    public void callBack(int code, String msg) {
                        switch (code) {
                            case ZSStatusCode.SUCCESS:
                                UserManager.getInstance(getApplicationContext()).setOnline(true);
                                SdkUtils.SetToken(accountInfo2.getToken());
                                gameStarted();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }

            //token登录
        } else {
            String email = null;
            String pwd = null;
            AccountInfo accountInfo = null;
            if (args != null && args.length >= 2) {
                email = args[0].toString();
                pwd = args[1].toString();
                if (args.length == 3) {
                    accountInfo = (AccountInfo) args[2];
                }
            }
            final AccountInfo accountInfo1 = accountInfo;
            if (email != null && pwd != null) {
                ZSSDK.getDefault().loginViaEmail(email, pwd, new NextCallBack() {
                    @Override
                    public void callBack(int code, String msg) {
                        // TODO Auto-generated method stub
                        // 处理结果
                        Log.e("loginViaEmail", "code:" + code + ",msg:" + msg);
                        switch (code) {
                            case ZSStatusCode.SUCCESS:
                                if (view instanceof LoginOtherView) {
                                    //绑定
                                    if (accountInfo1 != null && msg != null) {
                                        Gson gson = new Gson();
                                        final AccountInfo normalInfo = gson.fromJson(msg, AccountInfo.class);
                                        bindAccount(view, accountInfo1, normalInfo);
                                    }
                                } else {
                                    gameStarted();
                                }
                                break;
                            default:
                                break;
                        }
                    }


                });
            }
        }
    }


    /**
     * 第三方登录
     *
     * @param accountInfo
     */
    private void thirdLogin(final AccountInfo accountInfo) {
        final AccountInfo temp = accountInfo;
        ZSSDK.getDefault().thirdLogin(accountInfo.getId(), accountInfo.getPlatform().platform(), accountInfo.getInfoJson(), new NextCallBack() {
            @Override
            public void callBack(int code, String msg) {
                switch (code) {
                    case ZSStatusCode.SUCCESS:
                        Gson gson = new Gson();
                        final AccountInfo bindAccountInfo = gson.fromJson(msg, AccountInfo.class);
                        temp.setToken(bindAccountInfo.getToken());
                        temp.setBindAccount(bindAccountInfo);
                        SharedPreferencesUtils.updatePreferences(getApplicationContext(), WalletActivity.ACCOUNT_PREFIX + temp.getAccount(), gson.toJson(temp));
                        gameStarted();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void bindAccount(final View view, final AccountInfo accountInfo, final AccountInfo normalInfo) {
        ZSSDK.getDefault().bindThirdUser(accountInfo.getId(), accountInfo.getPlatform().platform(), normalInfo.getId(), new NextCallBack() {
            @Override
            public void callBack(int code, String msg) {
                switch (code) {
                    case ZSStatusCode.SUCCESS:
                        if (view instanceof RegisterOtherView) {
                            //绑定成功
                            final BlankFragment boundSuccessFragment = BlankFragment.newInstance(BoundSuccessView.class);
                            replaceFragment(boundSuccessFragment);
                            boundSuccessFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                                @Override
                                public void onViewCreated() {
                                    final BoundSuccessView boundSuccessView = (BoundSuccessView) boundSuccessFragment.getView();
                                    boundSuccessView.setAccountInfo(accountInfo);
                                    boundSuccessView.setMemberAccountInfo(normalInfo);
                                    boundSuccessView.setOnGameStartListener(WalletActivity.this);
                                    boundSuccessView.setOnExitListener(WalletActivity.this);
                                }
                            });
                        } else {
                            gameStarted();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onNextClick(View view, Object... args) {
        if (view instanceof RegisterNormalView) {
            if (args == null || args.length < 2) {
                return;
            }
            final String email = args[0].toString();
            final String pwd = args[1].toString();
            final BlankFragment registerAffirmFragment = BlankFragment.newInstance(RegisterAffirmView.class);
            replaceFragment(registerAffirmFragment);
            registerAffirmFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                @Override
                public void onViewCreated() {
                    final RegisterAffirmView registerAffirmView = (RegisterAffirmView) registerAffirmFragment.getView();
                    registerAffirmView.setEmail(email);
                    registerAffirmView.setPwd(pwd);
                    registerAffirmView.setOnNextListener(WalletActivity.this);
                    registerAffirmView.setOnExitListener(WalletActivity.this);
                }
            });
        } else if (view instanceof RegisterAffirmView) {
            if (args == null || args.length < 2) {
                return;
            }
            final String email = args[0].toString();
            final String pwd = args[1].toString();
            // 注册
            registNormail(view, email, pwd, null);

        } else if (view instanceof RegisterOtherView) {
            if (args == null || args.length < 3) {
                return;
            }
            String email = args[0].toString();
            String pwd = args[1].toString();
            AccountInfo accountInfo = (AccountInfo) args[2];
            registNormail(view, email, pwd, accountInfo);

        } else if (view instanceof ForgetEmailView) {
            if (args == null || args[0] == null) {
                return;
            }
            final String email = args[0].toString();
            ZSSDK.getDefault().getCodeViaMail(email, new NextCallBack() {
                @Override
                public void callBack(int code, String msg) {
                    // TODO Auto-generated method stub
                    switch (code) {
                        case ZSStatusCode.SUCCESS:
                            Log.e("getCodeViaMail", "SUCCESS ...");
                            final BlankFragment newPwdFragment = BlankFragment.newInstance(NewPasswordView.class);
                            replaceFragment(newPwdFragment);
                            newPwdFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                                @Override
                                public void onViewCreated() {
                                    final NewPasswordView newPasswordView = (NewPasswordView) newPwdFragment.getView();
                                    newPasswordView.setEmail(email);
                                    newPasswordView.setOnExitListener(WalletActivity.this);
                                    newPasswordView.setOnNextListener(WalletActivity.this);
                                }
                            });
                            break;
                        default:
                            Log.e("getCodeViaMail", "default ..." + msg);
                            Toast.makeText(WalletActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        } else if (view instanceof NewPasswordView) {
            if (args == null || args[0] == null || args[1] == null || args[2] == null) {
                return;
            }
            ZSSDK.getDefault().retrieveViaEmail(args[0].toString(), args[1].toString(), args[2].toString(), new NextCallBack() {
                @Override
                public void callBack(int code, String msg) {
                    // TODO Auto-generated method stub
                    switch (code) {
                        case ZSStatusCode.SUCCESS:
                            toLoginNormalFragment();
                            break;
                        default:
                            Toast.makeText(WalletActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

        }

    }

    private void registNormail(final View view, final String email, String pwd, final AccountInfo accountInfo) {
        ZSSDK.getDefault().registViaEmail(email, pwd, new NextCallBack() {
            @Override
            public void callBack(int code, String msg) {
                // TODO Auto-generated method stub
                // 处理结果
                switch (code) {
                    case ZSStatusCode.SUCCESS:
                        if (accountInfo == null) {
                            final BlankFragment registerSuccessFragment = BlankFragment.newInstance(RegisterSuccessView.class);
                            replaceFragment(registerSuccessFragment);
                            registerSuccessFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                                @Override
                                public void onViewCreated() {
                                    final RegisterSuccessView registerSuccessView = (RegisterSuccessView) registerSuccessFragment.getView();
                                    registerSuccessView.updateAccount(email);
                                    registerSuccessView.setOnGameStartListener(WalletActivity.this);
                                    registerSuccessView.setOnExitListener(WalletActivity.this);
                                }
                            });
                        } else {
                            Gson gson = new Gson();
                            bindAccount(view, accountInfo, gson.fromJson(msg, AccountInfo.class));
                        }
                        break;
                    default:
                        Toast.makeText(WalletActivity.this, msg, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void toLoginNormalFragment() {
        final BlankFragment loginNormalFragment = BlankFragment.newInstance(LoginNormalView.class);
        replaceFragment(loginNormalFragment);
        loginNormalFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
            @Override
            public void onViewCreated() {
                LoginNormalView loginNormalView = (LoginNormalView) loginNormalFragment.getView();
                loginNormalView.setOnExitListener(WalletActivity.this);
                loginNormalView.setOnGameStartListener(WalletActivity.this);
                loginNormalView.setOnLoginNormalListener(WalletActivity.this);
            }
        });
    }

    public static final String TAG = WalletActivity.class.getSimpleName();

    private void toLoginOtherFragment(final AccountInfo accountInfo) {
        Log.i(TAG, "toLoginOtherFragment (line 606): ");
        final BlankFragment loginOtherFragment = BlankFragment.newInstance(LoginOtherView.class);
        replaceFragment(loginOtherFragment);
        loginOtherFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
            @Override
            public void onViewCreated() {
                LoginOtherView loginOtherView = (LoginOtherView) loginOtherFragment.getView();
                loginOtherView.setAccountInfo(accountInfo);
                loginOtherView.setOnExitListener(WalletActivity.this);
                loginOtherView.setOnGameStartListener(WalletActivity.this);
            }
        });
    }

    @Override
    public void onForgetClick() {
        final BlankFragment forgetEmailFragment = BlankFragment.newInstance(ForgetEmailView.class);
        replaceFragment(forgetEmailFragment);

        forgetEmailFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
            @Override
            public void onViewCreated() {
                final ForgetEmailView forgetEmailView = (ForgetEmailView) forgetEmailFragment.getView();
                forgetEmailView.setOnExitListener(WalletActivity.this);
                forgetEmailView.setOnNextListener(WalletActivity.this);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (quickLoginFragment != null && parent != null) {
            QuickLoginView.SpinnerAdapter mSpinnerAdapter = (QuickLoginView.SpinnerAdapter) parent.getAdapter();
            if (mSpinnerAdapter != null) {
                final AccountInfo item = mSpinnerAdapter.getItem(position);
                final Platform platform = item.getPlatform();
                if (Platform.OTHER == platform) {
                    final BlankFragment loginNormalFragment = BlankFragment.newInstance(LoginNormalView.class);
                    replaceFragment(loginNormalFragment, false);
                    loginNormalFragment.setOnViewCreatedCallBack(new BlankFragment.OnViewCreatedCallBack() {
                        @Override
                        public void onViewCreated() {
                            LoginNormalView loginNormalView = (LoginNormalView) loginNormalFragment.getView();
                            loginNormalView.setOnExitListener(WalletActivity.this);
                            loginNormalView.setOnGameStartListener(WalletActivity.this);
                            loginNormalView.setOnLoginNormalListener(WalletActivity.this);
                        }
                    });
                } else if (Platform.ZEUSTEL == platform) {
                    final QuickLoginView quickLoginView = (QuickLoginView) quickLoginFragment.getView();
                    if (quickLoginView != null) {
                        quickLoginView.setBind(true);
                    }
                } else {
                    ZSSDK.getDefault().validBind(item.getId(), item.getPlatform().platform(), new NextCallBack() {
                        @Override
                        public void callBack(int code, String msg) {
                            switch (code) {
                                case ZSStatusCode.SUCCESS:
                                    boolean isBind = false;
                                    try {
                                        final Integer integer = Integer.valueOf(msg);
                                        isBind = integer == 1;
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    final boolean isBind_ = isBind;
                                    final QuickLoginView quickLoginView = (QuickLoginView) quickLoginFragment.getView();
                                    if (quickLoginView != null) {
                                        quickLoginView.setBind(isBind_);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
