package com.example.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Administrator on 2015/10/13 0013.
 */
public class IntentServiceTest extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IntentServiceTest(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
