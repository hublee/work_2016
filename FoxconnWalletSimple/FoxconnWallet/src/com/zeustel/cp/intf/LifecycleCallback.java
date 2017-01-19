package com.zeustel.cp.intf;

public interface LifecycleCallback {
	public void onstop();
	public void onstart();
	public void onResume();
	public void onPaused();
	public void onDestroyed();
	public void onCreated();
}
