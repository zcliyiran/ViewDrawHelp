package com.jacob.viewdraghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.jacob.viewdraghelper.lession7.LessonSevenActivity;
import com.jacob.viewdraghelper.lession9.Lession9Activity;
import com.jacob.viewdraghelper.lesson1.LessonOneActivity;
import com.jacob.viewdraghelper.lesson10.AnimationActivity;
import com.jacob.viewdraghelper.lesson2.LessonTwoActivity;
import com.jacob.viewdraghelper.lesson3.LessonThreeActivity;
import com.jacob.viewdraghelper.lesson4.LessonFourActivity;
import com.jacob.viewdraghelper.lesson5.LessonFiveActivity;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void lessonOne(View view) {
        Intent intent = new Intent(this, LessonOneActivity.class);
        startActivity(intent);
    }

    public void lessonTwo(View view) {
        Intent intent = new Intent(this, LessonTwoActivity.class);
        startActivity(intent);
    }

    public void lessonThree(View view) {
        Intent intent = new Intent(this, LessonThreeActivity.class);
        startActivity(intent);
    }

    public void lessonFour(View view) {
        Intent intent = new Intent(this, LessonFourActivity.class);
        startActivity(intent);
    }

    public void lessonFive(View view) {
        Intent intent = new Intent(this, LessonFiveActivity.class);
        startActivity(intent);
    }

    public void lessonSeven(View view){
        Intent intent = new Intent(this, LessonSevenActivity.class);
        startActivity(intent);
    }

    public void lessonEgith(View view){
        Intent intent = new Intent(this, LessonEghitActivity.class);
        startActivity(intent);
    }

    public void lesson9(View view){
        Intent intent = new Intent(this, Lession9Activity.class);
        startActivity(intent);
    }

    public void lesson10(View view){
        Intent intent = new Intent(this, AnimationActivity.class);
        startActivity(intent);
    }

}
