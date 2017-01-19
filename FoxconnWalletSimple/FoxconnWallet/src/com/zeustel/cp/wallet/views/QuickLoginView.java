package com.zeustel.cp.wallet.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zeustel.cp.bean.AccountInfo;
import com.zeustel.cp.bean.Platform;
import com.zeustel.cp.utils.Tools;
import com.zeustel.cp.wallet.interfaces.OnAccountBoundListener;
import com.zeustel.cp.wallet.interfaces.OnExitListener;
import com.zeustel.cp.wallet.interfaces.OnGameStartListener;
import com.zeustel.cp.wallet.interfaces.OnLoginListener;
import com.zeustel.foxconn.cp_sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录主页
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 11:43
 */
public class QuickLoginView extends WalletFrameLayout implements View.OnClickListener {
    //账号下拉列表
    private Spinner quickSpinner;
    //开始游戏
    private Button gameStart;
    //账号绑定
    private Button accountBound;
    //facebook登录
    private ImageView facebook_login;
    //migme 登录
    private ImageView migme_login;
    private OnAccountBoundListener onAccountBoundListener;
    private OnGameStartListener onGameStartListener;
    private OnExitListener onExitListener;
    private OnLoginListener onLoginListener;
    private List<AccountInfo> accountInfos = new ArrayList();
    private SpinnerAdapter mSpinnerAdapter;
    private boolean isBind;

    public QuickLoginView(Context context) {
        super(context);
    }

    public QuickLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_quick_login, this);
        quickSpinner = (Spinner) findViewById(R.id.quickSpinner);
        findViewById(R.id.close).setOnClickListener(this);
        gameStart = (Button) findViewById(R.id.gameStart);
        accountBound = (Button) findViewById(R.id.accountBound);
        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        migme_login = (ImageView) findViewById(R.id.migme_login);
        gameStart.setOnClickListener(this);
        facebook_login.setOnClickListener(this);
        migme_login.setOnClickListener(this);
        accountBound.setOnClickListener(this);
        updateBindState();
        final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                final int width = quickSpinner.getWidth();
                Log.i(TAG, "onPreDraw (line 87): width : " + width);
                quickSpinner.setDropDownWidth(quickSpinner.getWidth());
                return false;
            }
        });
    }

    public static final String TAG = QuickLoginView.class.getSimpleName();


    public void setBind(boolean bind) {
        isBind = bind;
        updateBindState();
    }

    private void updateBindState() {
        accountBound.setEnabled(!isBind);
    }

    public void prepare() {
        accountInfos.clear();
        AccountInfo otherAccount = new AccountInfo();
        otherAccount.setAccount("Other Account Login");
        otherAccount.setPlatform(Platform.OTHER);
        otherAccount.setAccountIcon(R.drawable.change_account);
        accountInfos.add(otherAccount);
        mSpinnerAdapter = new SpinnerAdapter(getContext(), accountInfos);
        quickSpinner.setAdapter(mSpinnerAdapter);

    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        if (quickSpinner != null) {
            quickSpinner.setOnItemSelectedListener(onItemSelectedListener);
        }

    }

    public void setAccountInfos(List<AccountInfo> accountInfos) {
        if (accountInfos != null && !accountInfos.isEmpty()) {
            this.accountInfos.addAll(0, accountInfos);
            mSpinnerAdapter.notifyDataSetChanged();
        }
    }

    public void setOnAccountBoundListener(OnAccountBoundListener onAccountBoundListener) {
        this.onAccountBoundListener = onAccountBoundListener;
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }

    public void setOnGameStartListener(OnGameStartListener onGameStartListener) {
        this.onGameStartListener = onGameStartListener;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if ((R.id.gameStart == id)) {
            if (onGameStartListener != null) {
                final int selectedItemPosition = quickSpinner.getSelectedItemPosition();
                final AccountInfo item = mSpinnerAdapter.getItem(selectedItemPosition);
                if (item.getPlatform() == Platform.OTHER) {
                    return;
                }
                onGameStartListener.onGameStartClick(this, item);
            }
        } else if (R.id.close == id) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }

        } else if (facebook_login.equals(v)) {
            if (onLoginListener != null) {
                onLoginListener.onLogin(this, OnLoginListener.LoginType.LOGIN_FACEBOOK);
            }

        } else if (migme_login.equals(v)) {
            //if (onLoginListener != null) {
            //    onLoginListener.onLogin(this, OnLoginListener.LoginType.LOGIN_MIGME);
           // }
            Tools.showToast(getContext(),"This function is not valid!");
        } else if (accountBound.equals(v)) {
            if (onAccountBoundListener != null) {
                onAccountBoundListener.onAccountBound(this);
            }
        }
    }

    public static class SpinnerAdapter extends BaseAdapter {
        private Context context;
        private List<AccountInfo> accountInfos;
        private LayoutInflater inflater;
        public static final String TAG = SpinnerAdapter.class.getSimpleName();

        SpinnerAdapter(Context context, List<AccountInfo> accountInfos) {
            this.context = context;
            this.accountInfos = accountInfos;
            inflater = LayoutInflater.from(context);
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return accountInfos == null ? 0 : accountInfos.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public AccountInfo getItem(int position) {
            return accountInfos == null ? null : accountInfos.get(position);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder = null;
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.spinner_item_layout, null);
                mViewHolder.accountName = (TextView) convertView.findViewById(R.id.account_name);
                mViewHolder.accountIcon = (ImageView) convertView.findViewById(R.id.account_icon);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            final AccountInfo item = getItem(position);
            if (item != null) {
                final String account = item.getAccount();
                final int accountIcon = item.getAccountIcon();
                final String email = item.getEmail();
                mViewHolder.accountName.setText(email == null ? (account == null ? "" : account) : email);
                mViewHolder.accountIcon.setImageResource(accountIcon);
            }
            return convertView;
        }

        private static class ViewHolder {
            private TextView accountName;
            private ImageView accountIcon;
        }
    }
}
