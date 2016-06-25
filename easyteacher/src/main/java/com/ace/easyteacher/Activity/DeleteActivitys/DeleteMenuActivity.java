package com.ace.easyteacher.Activity.DeleteActivitys;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.DestinyUtils;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeleteMenuActivity extends AppCompatActivity {
    @Bind(R.id.delete_student)
    Button delete_student;
    @Bind(R.id.delete_teacher)
    Button delete_teacher;
    @Bind(R.id.delete_schedule)
    Button delete_schedule;


    private List<View> mList;
    private ExitActivityTransition exitTransition;
    private float mAnimatTime, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_menu);
        ButterKnife.bind(this);
        mList = new ArrayList<>();
        y = DestinyUtils.dpToPx(60);
        exitTransition = ActivityTransition.with(getIntent()).to(findViewById(R.id.delete_schedule)).duration(100).start(savedInstanceState);
        //动画
        startAnimat();

        delete_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteMenuActivity.this, DeleteScheduleActivity.class);
                startActivity(intent);
            }
        });
        delete_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteMenuActivity.this, DeleteStudentActivity.class);
                startActivity(intent);
            }
        });
        delete_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteMenuActivity.this, DeleteTeacherActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startAnimat() {
        mList.add(delete_student);
        mList.add(delete_teacher);
        mAnimatTime = (1 * 1000 - 350) / 5;
        int i;
        for (i = 0; i < mList.size(); i++) {
            mList.get(i).setTranslationY(y);
            final int finalI = i;
            mList.get(i).animate().translationY(0).setStartDelay((long) (150 + mAnimatTime * i)).setDuration((long) mAnimatTime).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mList.get(finalI).setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    private void finishThis() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                exitTransition.exit(DeleteMenuActivity.this);
            }
        }, 310);
        int j = 0;
        for (int i = mList.size() - 1; i >= 0; i--) {
            final int finalI = i;
            mList.get(i).animate().translationY(y).setStartDelay((long) (j * mAnimatTime)).setDuration((long) mAnimatTime).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mList.get(finalI).setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            j += 1;
        }
    }

    @Override
    public void onBackPressed() {
        finishThis();
    }
}
