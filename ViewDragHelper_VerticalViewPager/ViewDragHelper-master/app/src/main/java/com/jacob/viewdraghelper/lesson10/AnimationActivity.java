package com.jacob.viewdraghelper.lesson10;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jacob.viewdraghelper.Gift;
import com.jacob.viewdraghelper.GiftLinearLayout;
import com.jacob.viewdraghelper.R;

public class AnimationActivity extends AppCompatActivity {

    private GiftLinearLayout viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        viewById = ((GiftLinearLayout) findViewById(R.id.gll_layout));
        for (int i = 0; i < 10; i++) {
            viewById.addGift(new Gift(1, 10, 1000));
        }
    }
}
