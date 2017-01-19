package com.zeustel.top9.assist.gotye;

import android.os.Bundle;
import android.widget.Toast;

import com.gotye.api.voichannel.ChannelInfo;
import com.gotye.api.voichannel.ErrorType;
import com.gotye.api.voichannel.LoginInfo;
import com.gotye.api.voichannel.MemberType;
import com.gotye.api.voichannel.TalkMode;
import com.gotye.api.voichannel.VoiChannelAPI;
import com.gotye.api.voichannel.VoiChannelAPIListener;
import com.zeustel.top9.assist.OpenPlatform;
import com.zeustel.top9.base.WrapNotAndHandleActivity;

import java.util.Map;

public abstract class BaseVoiceActivity extends WrapNotAndHandleActivity implements VoiChannelAPIListener {
    protected VoiChannelAPI voiChannelAPI = VoiChannelAPI.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化亲加语音通讯云
        OpenPlatform.initGotyeSDK(BaseVoiceActivity.this);
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            //登录亲加服务器
            voiChannelAPI.setLoginInfo(loginInfo);
        }
        //添加监听
        voiChannelAPI.addListener(this);
    }

    @Override
    protected void onDestroy() {
        //移除监听
        voiChannelAPI.removeListener(this);
        super.onDestroy();
    }

    protected abstract LoginInfo getLoginInfo();

    protected abstract void talking(boolean isTalking, String uId);

    @Override
    public void onExit(boolean b) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onJoinChannel(boolean b) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onGetChannelMember(String s) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onGetUserNickname(Map<String, String> map) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onExitChannel(boolean b) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onChannelRemoved() {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onRemoveChannelMember(String s) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onSilencedStateChanged(boolean b, String s) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onMuteStateChanged(boolean muted) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onStartTalking(String s) {
        if (isFinishing()) {
            return;
        }
        talking(true, s);
    }

    @Override
    public void onStopTalking(String s) {
        if (isFinishing()) {
            return;
        }
        talking(false, s);
    }

    @Override
    public void notifyChannelMemberTypes(Map<String, MemberType> map) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void notifyChannelTalkMode(TalkMode talkMode) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onGetChannelDetail(ChannelInfo channelInfo) {
        if (isFinishing()) {
            return;
        }
    }

    @Override
    public void onError(ErrorType errorType) {
        if (isFinishing()) {
            return;
        }
        ErrorType errortype = errorType;
        if (errortype == ErrorType.ErrorAppNotExsit) {
            Toast.makeText(BaseVoiceActivity.this, "App Not Exist", Toast.LENGTH_SHORT).show();
        } else if (errortype == ErrorType.ErrorChannelIsFull) {
            Toast.makeText(BaseVoiceActivity.this, "Channel is Full", Toast.LENGTH_SHORT).show();

        } else if (errortype == ErrorType.ErrorInvalidUserID) {
            Toast.makeText(BaseVoiceActivity.this, "Invalid User ID", Toast.LENGTH_SHORT).show();
        } else if (errortype == ErrorType.ErrorUserIDInUse) {
            Toast.makeText(BaseVoiceActivity.this, "User ID Is In Use", Toast.LENGTH_SHORT).show();
        } else if (errortype == ErrorType.ErrorNetworkInvalid) {
            Toast.makeText(BaseVoiceActivity.this, "Network Disconnected", Toast.LENGTH_SHORT).show();
        } else if (errortype == ErrorType.ErrorServerIsFull) {
            Toast.makeText(BaseVoiceActivity.this, "Server is Full", Toast.LENGTH_SHORT).show();
        } else if (errortype == ErrorType.ErrorPermissionDenial) {
            Toast.makeText(BaseVoiceActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(BaseVoiceActivity.this, "Unknown Error: " + errorType, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public String getFloatTitle() {
        return null;
    }
}
