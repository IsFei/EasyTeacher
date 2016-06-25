package com.ace.easyteacher.Activity.AddActivitys;

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

public class AddMenuActivity extends AppCompatActivity {

    @Bind(R.id.add_student)
    Button mAddStudent;
    @Bind(R.id.add_teacher)
    Button mAddTeacher;
    @Bind(R.id.add_grade)
    Button mAddGrade;
    @Bind(R.id.add_class)
    Button mAddClass;
    @Bind(R.id.add_exam)
    Button mAddExam;
    @Bind(R.id.add_schedule)
    Button mAddSchedule;

    private List<View> mList;
    private ExitActivityTransition exitTransition;
    private float mAnimatTime, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);
        mList = new ArrayList<>();
        y = DestinyUtils.dpToPx(60);
        exitTransition = ActivityTransition.with(getIntent()).to(findViewById(R.id.add_student)).duration(300).start(savedInstanceState);
        startAnimat();
        mAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this, AddStuActivity.class);
                startActivity(intent);
            }
        });
        mAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this, AddTeacherActivity.class);
                startActivity(intent);
            }
        });
        mAddGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this, AddScoreActivity.class);
                startActivity(intent);
            }
        });
        mAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this, AddClassActivity.class);
                startActivity(intent);
            }
        });
        mAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this, AddExamActivity.class);
                startActivity(intent);
            }
        });
        mAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMenuActivity.this, AddScheduleActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startAnimat() {
        mList.add(mAddTeacher);
        mList.add(mAddGrade);
        mList.add(mAddClass);
        mList.add(mAddSchedule);
        mList.add(mAddExam);
        mAnimatTime = (1 * 1000 - 350) / mList.size();
        int i;
        for (i = 0; i < mList.size(); i++) {
            mList.get(i).setTranslationY(-y);
            final int finalI = i;
            mList.get(i).animate().translationY(0).setStartDelay((long) (350 + mAnimatTime * i)).setDuration((long) mAnimatTime).setListener(new Animator.AnimatorListener() {
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
                exitTransition.exit(AddMenuActivity.this);
            }
        }, 650);
        int j = 0;
        for (int i = mList.size() - 1; i >= 0; i--) {
            final int finalI = i;
            mList.get(i).animate().translationY(-y).setStartDelay((long) (j * mAnimatTime)).setDuration((long) mAnimatTime).setListener(new Animator.AnimatorListener() {
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
