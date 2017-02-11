package net.winnerawan.openmadiun.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.winnerawan.openmadiun.R;

public class MoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        overridePendingTransition(R.anim.anim_pop_down, R.anim.anim_push_down);
    }
}
