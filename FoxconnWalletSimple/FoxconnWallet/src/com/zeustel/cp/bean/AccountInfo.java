package com.zeustel.cp.bean;

import java.io.Serializable;

/**
 * 账号信息
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/4 15:20
 */
public class AccountInfo implements Serializable {
	private String id;
	private String email;
    //令牌
    private String token;
    //账号
    private String account;
    //账号图标
    private int accountIcon;
    private Platform platform;
    private String infoJson;

    private AccountInfo bindAccount;

    public AccountInfo getBindAccount() {
        return bindAccount;
    }

    public void setBindAccount(AccountInfo bindAccount) {
        this.bindAccount = bindAccount;
    }

    public AccountInfo() {
    }

    public AccountInfo(String account, int accountIcon, String token) {
        this.account = account;
        this.accountIcon = accountIcon;
        this.token = token;
    }
    

    public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getInfoJson() {
		return infoJson;
	}

	public void setInfoJson(String infoJson) {
		this.infoJson = infoJson;
	}

	public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountIcon() {
        return accountIcon;
    }

    public void setAccountIcon(int accountIcon) {
        this.accountIcon = accountIcon;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    @Override
    public String toString() {
        return "AccountInfo{" +
                "account='" + account + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", accountIcon=" + accountIcon +
                ", platform=" + platform +
                ", infoJson='" + infoJson + '\'' +
                '}';
    }
}
