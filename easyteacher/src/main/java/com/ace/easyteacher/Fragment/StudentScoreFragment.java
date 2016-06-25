package com.ace.easyteacher.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ace.easyteacher.Activity.StudentInfoActivity;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentGrade;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ClassScoreUtils;
import com.ace.easyteacher.Utils.ToastUtils;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class StudentScoreFragment extends Fragment {

    @Bind(R.id.chart_old)
    LineChartView mChartOld;
    @Bind(R.id.chart_now)
    ColumnChartView mChartNow;

    private ColumnChartData mDataNow;
    private LineChartData mDataOld;

    private List<ClassScoreUtils> mClass;
    private List<StudentGrade> mListClass;
    private StudentInfo mStudent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_score, container, false);
        ButterKnife.bind(this, view);
        mStudent = ((StudentInfoActivity) getActivity()).getStudent();
        mChartNow.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                //i为科目，通过科目找到相对的成绩，传到chartold
                getOldChartDataAndSet(i);
            }

            @Override
            public void onValueDeselected() {

            }
        });
        mChartNow.setZoomEnabled(false);
        mChartOld.setViewportCalculationEnabled(false);

        getGradeBySid(mStudent.getSid() + "");
        return view;
    }


    private void getGradeBySid(String sid) {
        HttpUtils.HttpCallback<StudentGrade> callback = new HttpUtils.HttpCallback<StudentGrade>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<StudentGrade> result = db.findAll(StudentGrade.class);
                    mListClass = result;
                    initClass(result);
                } catch (DbException e) {
                    ToastUtils.Toast(getActivity(), "获取考勤记录失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(StudentGrade result) {
                Log.d("ian", "success");

            }

            @Override
            public void onSuccess(List<StudentGrade> result) {
                Log.d("ian", "success" + result.size());
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(StudentGrade.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (StudentGrade studentInfo : result) {
                    try {
                        db.save(studentInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                mListClass = result;
                initClass(result);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getGradeBySid(sid, callback);
    }

    private void initClass(List<StudentGrade> result) {
        if (result == null || result.size() == 0) {
            return;
        }
        mClass = new ArrayList<ClassScoreUtils>();
        mClass.add(new ClassScoreUtils("语文", 0xFFe51c23, result.get(0).getYw()));
        mClass.add(new ClassScoreUtils("数学", 0xFFe91e63, result.get(0).getSx()));
        mClass.add(new ClassScoreUtils("英语", 0xFF9c27b0, result.get(0).getWy()));
        mClass.add(new ClassScoreUtils("物理", 0xFF673ab7, result.get(0).getWl()));
        mClass.add(new ClassScoreUtils("化学", 0xFF3f51b5, result.get(0).getHx()));
        mClass.add(new ClassScoreUtils("生物", 0xFF5677fc, result.get(0).getSw()));
        mClass.add(new ClassScoreUtils("地理", 0xFF0277bd, result.get(0).getDl()));
        mClass.add(new ClassScoreUtils("历史", 0xFF00bcd4, result.get(0).getLs()));
        mClass.add(new ClassScoreUtils("政治", 0xFF009688, result.get(0).getZz()));
        mClass.add(new ClassScoreUtils("体育", 0xFF259b24, result.get(0).getTy()));
        //判断文理科
        for (int i = 0; i < mListClass.size(); i++) {
            if (String.valueOf(mListClass.get(i).getWl()).equals("") &&
                    String.valueOf(mListClass.get(i).getHx()).equals("") &&
                    String.valueOf(mListClass.get(i).getSw()).equals("")) {
                mClass.remove(3);
                mClass.remove(3);
                mClass.remove(3);
                break;
            }
            if (String.valueOf(mListClass.get(i).getDl()).equals("") &&
                    String.valueOf(mListClass.get(i).getLs()).equals("") &&
                    String.valueOf(mListClass.get(i).getZz()).equals("")) {
                mClass.remove(6);
                mClass.remove(6);
                mClass.remove(6);
                break;
            }
        }
        generateDefaultData();
    }

    private void getOldChartDataAndSet(int iClass) {

        int numValues = mListClass.size();

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        switch (iClass) {
            case 0:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getYw()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 1:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getSx()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 2:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getWy()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 3:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getWl()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 4:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getHx()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 5:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getSw()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 6:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getDl()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 7:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getLs()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 8:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getZz()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
            case 9:
                for (int i = 0; i < numValues; ++i) {
                    values.add(new PointValue(i, mListClass.get(i).getTy()));
                    axisValues.add(new AxisValue(i).setLabel(String.valueOf(i + 1)));
                }
                break;
        }
        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        mDataOld = new LineChartData(lines);
        mDataOld.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        mDataOld.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        mChartOld.setLineChartData(mDataOld);
        mChartOld.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(0, 150, 6, 0);
        mChartOld.setMaximumViewport(v);
        mChartOld.setCurrentViewport(v);
//        mChartOld.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = mClass.size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue(mClass.get(i).getScore(), mClass.get(i).getColor()));
            }

            axisValues.add(new AxisValue(i).setLabel(mClass.get(i).getName()));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        mDataNow = new ColumnChartData(columns);

        mDataNow.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        mDataNow.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        mChartNow.setColumnChartData(mDataNow);

//        mChartNow.setOnValueTouchListener(new ValueTouchListener());

        mChartNow.setValueSelectionEnabled(true);

        mChartNow.setZoomType(ZoomType.HORIZONTAL);

        // chartBottom.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // SelectedValue sv = chartBottom.getSelectedValue();
        // if (!sv.isSet()) {
        // generateInitialLineData();
        // }
        //
        // }
        // });

    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineData(mClass.get(columnIndex).getColor(), columnIndex);
        }

        @Override
        public void onValueDeselected() {
            generateLineData(ChartUtils.COLOR_GREEN, 0);
        }
    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        mChartOld.cancelDataAnimation();
        // Modify data targets
        Line line = mDataOld.getLines().get(0);
        line.setColor(color);
//        final PointValue value = null;
//        switch ((int) range) {
//            case 0:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getYw());
//                }
//                break;
//            case 1:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getSx());
//                }
//                break;
//            case 2:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getWy());
//                }
//                break;
//            case 3:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getWl());
//                }
//                break;
//            case 4:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getHx());
//                }
//                break;
//            case 5:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getSw());
//                }
//                break;
//            case 6:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getDl());
//                }
//                break;
//            case 7:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getLs());
//                }
//                break;
//            case 8:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getZz());
//                }
//                break;
//            case 9:
//                for (int i = 0; i < mListClass.size(); i++) {
//                    // Change target only for Y value.
//                    value.setTarget(value.getX(), mListClass.get(i).getTy());
//                }
//                break;
//        }
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * 100);
        }
        // Start new data animation with 300ms duration;
        mChartOld.startDataAnimation(300);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
