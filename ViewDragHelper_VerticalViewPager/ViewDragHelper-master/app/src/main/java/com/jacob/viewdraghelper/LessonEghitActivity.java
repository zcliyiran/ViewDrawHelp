package com.jacob.viewdraghelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jacob.viewdraghelper.lession7.DragLayoutRight;

public class LessonEghitActivity extends AppCompatActivity {

    private DragLayoutRight dragLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_eghit);


        dragLayout = ((DragLayoutRight) findViewById(R.id.drag_layout));
        dragLayout.setIsCanVerticalDrag(true);
        dragLayout.setVerticalScroll(new DragLayoutRight.VerticalHorizontalScroll() {
            @Override
            public void goNext() {
                Toast.makeText(LessonEghitActivity.this, "goNext", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void goPrev() {
                Toast.makeText(LessonEghitActivity.this, "goPrev", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void goRight() {
                Toast.makeText(LessonEghitActivity.this, "goRight", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void goLeft() {
                Toast.makeText(LessonEghitActivity.this, "goLeft", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
