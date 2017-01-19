package com.example;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import com.example.hlb.myapplication.R;
import com.example.service.BinderService;

/**
 * Created by Administrator on 2015/10/13 0013.
 */
public class BinderActivity extends Activity{

    private Button btnStartBinderService;
    private Button btnStopBinderService;

    private Boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        btnStartBinderService = (Button) findViewById(R.id.btnStartBinderService);
        btnStopBinderService = (Button) findViewById(R.id.btnStopBinderService);
        btnStartBinderService.setOnClickListener(listener);
        btnStopBinderService.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnStartBinderService:
                    bind();
                    break;
                case R.id.btnStopBinderService:
                    unbind();
                    break;
                default:
                    break;
            }
        }

        private void unbind() {
            if(isConnected){
                unbindService(conn);
            }
        }

        private void bind() {
            Intent intent = new Intent(BinderActivity.this, BinderService.class);
            bindService(intent,conn, Context.BIND_AUTO_CREATE);
        }
    };


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            BinderService.MyBinder myBinder = (BinderService.MyBinder) binder;
            BinderService service = myBinder.getService();
            service.MyMethod();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };

}
