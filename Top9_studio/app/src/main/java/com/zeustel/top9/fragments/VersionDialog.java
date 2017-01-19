package com.zeustel.top9.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.zeustel.top9.R;
import com.zeustel.top9.bean.Version;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;

import org.apache.http.Header;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 版本更新界面
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/23 17:27
 */
public class VersionDialog extends DialogFragment implements View.OnClickListener {
    private static final String EXTRA_NAME = "version";
    //更新进度条
    private ProgressBar version_progress;
    //更新内容列表
    private ListView version_content;
    //确定更新
    private Button update_commit;
    //取消更新
    private Button update_cancel;
    //版本
    private Version version;
    //apk路径
    private String url;
    //校验码
    private String verifyCode;
    private int channel;
    private long totalSize;
    private File file;

    public static VersionDialog newInstance(Version version) {
        VersionDialog instance = new VersionDialog();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, version);
        instance.setArguments(args);
        return instance;
    }

    private View init(LayoutInflater inflater) {
        View contentView = inflater.inflate(R.layout.dialog_fragment_version, null);
        version_progress = (ProgressBar) contentView.findViewById(R.id.version_progress);
        version_content = (ListView) contentView.findViewById(R.id.version_content);
        update_commit = (Button) contentView.findViewById(R.id.update_commit);
        update_cancel = (Button) contentView.findViewById(R.id.update_cancel);
        update_commit.setOnClickListener(this);
        update_cancel.setOnClickListener(this);
        updateUI();
        return contentView;
    }

    private void updateUI() {
        if (version != null) {
            List<String> remark = version.getRemark();
            if (!Tools.isEmpty(remark)) {
                remark.add(0/*index*/, getString(R.string.version_describe, version.getVerName()));
                remark.add(1/*index*/, getString(R.string.version_time, Tools.getTimeFormatformatyyyyDDNN(version.getTime())));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_version, R.id.version_item, remark);
                version_content.setAdapter(arrayAdapter);
            }
            url = version.getUrl();
            verifyCode = version.getVerifyCode();
            channel = version.getChannel();
            totalSize = version.getLen();
            version_progress.setMax((int) totalSize);
            file = new File(Tools.getCacheDir(getContext()), buildApkName(version));
            Tools.log_i(VersionDialog.class, "updateUI", "url :" + url);
        }
        updateStr();
    }

    private void updateStr() {
        if (file.exists() && file.length() > 0) {
            try {
                String md5Encrypt = Tools.md5Encrypt(file);
                if (md5Encrypt.equals(version.getVerifyCode())) {
                    update_commit.setText(getString(R.string.update_install));
                    return;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
        if (file.exists())
            file.delete();
        update_commit.setText(getString(R.string.update_update));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            version = (Version) getArguments().getSerializable(EXTRA_NAME);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(init(inflater));
        return builder.create();
    }

    private String buildApkName(long time, String channel, String verName) {
        return "Top9_" + time + "_" + channel + "_" + verName + ".apk";
    }

    private String buildApkName(Version version) {
        if (version == null) {
            return null;
        }
        return buildApkName(version.getTime(), String.valueOf(version.getChannel()), version.getVerName());
    }

    @Override
    public void onClick(View v) {
        if (update_cancel.equals(v)) {
            dismiss();
        } else {
            if (getString(R.string.update_install).equals(update_commit.getText().toString())) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else if(getString(R.string.update_update).equals(update_commit.getText().toString())){
                update(url);
            }
        }
    }

    private void update(String url) {
        NetHelper.get(getContext(), url, null, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                Tools.log_i(VersionDialog.class, i + "  onFailure", "");
                if (file.exists())
                    file.delete();
            }

            @Override
            public void onSuccess(int i, Header[] headers, File file) {
                try {
                    String md5 = Tools.md5Encrypt(file);
                    if (md5.equals(version.getVerifyCode())) {
                        updateStr();
                    } else {
                        Tools.showToast(getContext(), getString(R.string.update_modify_ed));
                        if (file.exists())
                            file.delete();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                if (bytesWritten > 0 && VersionDialog.this.totalSize > 0) {
                    version_progress.setProgress((int) bytesWritten);
                    int percent = (int) (bytesWritten * 100 / VersionDialog.this.totalSize);
                    update_commit.setText(percent + "%");
                }
            }
        });
    }
}
