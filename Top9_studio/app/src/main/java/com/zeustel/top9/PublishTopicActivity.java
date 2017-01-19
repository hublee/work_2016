package com.zeustel.top9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zeustel.top9.fragments.PublishTopicFragment;

public class PublishTopicActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "gameId";
    public static final int REQUEST_CODE = 11101;

    private int gameId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_topic);
        if (getIntent() != null) {
            gameId = getIntent().getIntExtra(EXTRA_NAME, -1);
        }
        if (gameId == -1) {
            finish();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.publish, PublishTopicFragment.newInstance(gameId)).commit();
        }
    }
}
