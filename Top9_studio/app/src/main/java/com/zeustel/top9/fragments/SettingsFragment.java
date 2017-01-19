package com.zeustel.top9.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderSettingsMenu;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.WrapOneAndHandleFragment;
import com.zeustel.top9.bean.SelfItem;
import com.zeustel.top9.bean.Version;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.result.VersionResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.LoadProgress;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataBaseOperate;
import com.zeustel.top9.utils.operate.DataUser;
import com.zeustel.top9.utils.operate.DataVersion;
import com.zeustel.top9.widgets.CircleImage;
import com.zeustel.top9.widgets.CustomRecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置页面
 */
public class SettingsFragment extends WrapOneAndHandleFragment implements CustomRecyclerView.OnItemClickListener, View.OnClickListener {

    private static final int REQUEST_CODE = 1100;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private CircleImage settings_icon;
    private ImageView settings_edit;
    private TextView settings_name;
    private TextView settings_modify_name;
    private TextView settings_modify_password;
    private CustomRecyclerView settings_recycler;
    private OverallRecyclerAdapter<SelfItem> adapter;
    private List<SelfItem> data = new ArrayList();
    private FragmentTransaction fragmentTransaction;
    private LoadProgress loadProgress;
    private DataBaseOperate dataVersion;
    private Uri imageUri;

    public SettingsFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (data != null) {
            data.clear();
            data = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = "file:///" + Tools.getCacheDir(getContext()).getAbsolutePath() + File.separator + "user.png";
        imageUri = Uri.parse(path);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadProgress = new LoadProgress(getActivity());
        dataVersion = new DataVersion(getHandler());
        View contentView = inflater.inflate(R.layout.fragment_settings, container, false);
        settings_icon = (CircleImage) contentView.findViewById(R.id.settings_icon);
        settings_edit = (ImageView) contentView.findViewById(R.id.settings_edit);
        settings_name = (TextView) contentView.findViewById(R.id.settings_name);
        settings_modify_name = (TextView) contentView.findViewById(R.id.settings_modify_name);
        settings_modify_password = (TextView) contentView.findViewById(R.id.settings_modify_password);
        settings_recycler = (CustomRecyclerView) contentView.findViewById(R.id.settings_recycler);
        settings_recycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        settings_recycler.setLayoutManager(mLayoutManager);
        if (data != null) {
            data.clear();
        }
        //        data.add(new SelfItem(0, getString(R.string.settings_home_page), new SettingsHomeFragment()));
        data.add(new SelfItem(0, getString(R.string.settings_check_update), null));
        data.add(new SelfItem(0, getString(R.string.settings_about), new AboutFragment()));
        try {
            adapter = new OverallRecyclerAdapter(data, R.layout.list_item_settings, HolderSettingsMenu.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (adapter != null) {
            settings_recycler.setAdapter(adapter);
        }
        settings_recycler.setOnItemClickListener(this);
        settings_edit.setOnClickListener(this);
        settings_modify_name.setOnClickListener(this);
        settings_modify_password.setOnClickListener(this);
        Tools.endCreateOperate(settings_icon, new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
        return contentView;
    }

    @Override
    public void OnResumeCorrect() {
        super.OnResumeCorrect();
        updateUI();
    }

    private void updateUI() {
        if (Constants.USER.isOnline()) {
            if (settings_name != null) {
                settings_name.setText(Constants.USER.getNickName());
            }
        }
        if (settings_icon != null) {
            imageLoader.displayImage(Constants.TEST_IMG + Constants.USER.getIcon(), settings_icon, Tools.options);
        }
    }


    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public boolean onItemClick(RecyclerView parent, RecyclerView.ViewHolder viewHolder, View itemView, int viewType, int position) {
        if (adapter != null) {
            Fragment fragment = adapter.getItem(position).getFragment();
            if (fragment != null) {
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.selfDetail, fragment).commit();
            } else {
                loadProgress.show(R.string.check_version_update);
                try {
                    dataVersion.getSingleData(dataVersion.createSingleBundle(Constants.URL_VERSION + "?verCode=" + Tools.getAppVersion(getContext())/* + "&channel=" + Tools.getAppChannel(getContext())*/), VersionResult.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack(null);
        if (settings_edit.equals(v)) {
            Tools.startTailoringPicture(imageUri, SettingsFragment.this, REQUEST_CODE);
        } else if (settings_modify_name.equals(v)) {
            fragmentTransaction.replace(R.id.selfDetail, new ModifyNickNameFragment());
        } else if (settings_modify_password.equals(v)) {
            fragmentTransaction.replace(R.id.selfDetail, new ModifyPwdFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onHandleSingleSuccess(Result result, Object obj) {
        super.onHandleSingleSuccess(result, obj);
        loadProgress.dismiss();
        if (obj != null) {
            VersionDialog.newInstance((Version) obj).show(getFragmentManager(), VersionDialog.class.getSimpleName());
        }
    }

    @Override
    public void onHandleSingleFailed(Result result) {
        super.onHandleSingleFailed(result);
        loadProgress.dismiss();
        Tools.showToast(getContext(), R.string.version_last_already);
    }

    /**
     * 更新头像
     */
    private static final class UpdateTask implements Runnable {
        private final Uri uri;
        private final WeakReference<SettingsFragment> fragmentRef;
        private final WeakReference<Handler> handlerRef;
        private final WeakReference<LoadProgress> loadProgressRef;

        public UpdateTask(SettingsFragment fragment, Handler handler, LoadProgress loadProgress, Uri uri) {
            this.uri = uri;
            fragmentRef = new WeakReference<SettingsFragment>(fragment);
            handlerRef = new WeakReference<Handler>(handler);
            loadProgressRef = new WeakReference<LoadProgress>(loadProgress);
        }

        @Override
        public void run() {
            final SettingsFragment settingsFragment = fragmentRef.get();
            if (settingsFragment == null) {
                return;
            }
            final Context context = settingsFragment.getContext();
            final Handler handler = handlerRef.get();
            final LoadProgress loadProgress = loadProgressRef.get();
            if (uri != null && context != null && handler != null && loadProgress != null) {
                Bitmap bitmap = null;
                try {
                    bitmap = Tools.getThumbnail(context, uri, 400/*size*/);
                    final String path = Tools.bitmap2File(bitmap, Tools.getCacheDir(context) + File.separator + "user.png");
                    if (path != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //update local icon
                                //upload icon to server
                                loadProgress.show(R.string.modify_ing);
                                File file = new File(path);
                                if (!file.exists()) {
                                    loadProgress.dismiss();
                                    return;
                                }
                                try {
                                    DataUser.updateInfo(file, new SimpleResponseHandler() {
                                        @Override
                                        public void onCallBack(int code, String json) {
                                            super.onCallBack(code, json);
                                            Gson gson = new Gson();
                                            if (json != null) {
                                                Result result = gson.fromJson(json, Result.class);
                                                if (result != null && result.getSuccess() == Result.SUCCESS) {
                                                    Tools.log_i(SettingsFragment.class, "onCallBack", "msg : " + result.getMsg());
                                                    Constants.USER.setIcon(result.getMsg());
                                                    settingsFragment.updateUI();
                                                }
                                            }
                                            loadProgress.dismiss();
                                        }
                                    });
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } finally {
                                    loadProgress.dismiss();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    loadProgress.dismiss();
                    if (bitmap != null) {
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        bitmap = null;
                    }
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_CODE) {
            Tools.pool.execute(new UpdateTask(this, getHandler(), loadProgress, imageUri));
        }
    }
}
