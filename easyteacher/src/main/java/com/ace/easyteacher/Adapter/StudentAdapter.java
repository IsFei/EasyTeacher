package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.R;
import com.ace.easyteacher.View.MaterialRippleLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lenovo on 2015-12-13.
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<String> mDataset;
    private static LayoutInflater layoutInflater;
    public OnItemClickListener mItemClickListener;
    private List<StudentInfo> mListStudent;

    private Context context;
    private int lastAnimatedPosition = -1;
    private int viewHeight = 0;

    public StudentAdapter(Context context, List<StudentInfo> mListStudent) {
        super();
//        mDataset = dataset;
//        for (int i = 0; i < 30; i++) {
//            mDataset.add("item" + i);
//        }
        this.mListStudent = mListStudent;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_student, null);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // 绑定数据到ViewHolder上
//        viewHolder.textView.setText(mDataset.get(position));
        viewHolder.bindView(mListStudent.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mListStudent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mStudentName, mStudentId, mStudentAge, mStudentPosition;
        private SimpleDraweeView mHead;
        private MaterialRippleLayout mp;

        public ViewHolder(View itemView) {
            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.text);
            mHead = (SimpleDraweeView) itemView.findViewById(R.id.student_head);
            mStudentName = (TextView) itemView.findViewById(R.id.student_name);
            mStudentAge = (TextView) itemView.findViewById(R.id.student_age);
            mStudentId = (TextView) itemView.findViewById(R.id.student_number);
            mStudentPosition = (TextView) itemView.findViewById(R.id.student_position);
            mp = (MaterialRippleLayout) itemView.findViewById(R.id.mp);
        }

        void bindView(StudentInfo studentInfo, final int position) {
            mStudentId.setText(studentInfo.getSid() + "");
            mStudentAge.setText(studentInfo.getBirthday());
            mStudentPosition.setText(studentInfo.getPosition());
            mStudentName.setText(studentInfo.getName());
            Uri uri = Uri.parse("http://com-ace-teacher.oss-cn-shenzhen.aliyuncs.com/" + studentInfo.getSid() + ".jpg");
            mHead.setImageURI(uri);
            mp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, position);
                }
            });
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
