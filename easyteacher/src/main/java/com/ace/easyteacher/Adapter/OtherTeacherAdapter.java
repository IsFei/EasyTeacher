package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.R;
import com.ace.easyteacher.View.MaterialRippleLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by lenovo on 2015-12-13.
 */
public class OtherTeacherAdapter extends RecyclerView.Adapter<OtherTeacherAdapter.ViewHolder> {
    private static LayoutInflater layoutInflater;
    public OnItemClickListener mItemClickListener;
    private List<TeacherInfo> mListTeacher;

    public OtherTeacherAdapter(Context context, List<TeacherInfo> mListTeacher) {
        super();
        this.mListTeacher = mListTeacher;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_teacher, null);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // 绑定数据到ViewHolder上
//        viewHolder.textView.setText(mDataset.get(position));
        viewHolder.bindView(mListTeacher.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mListTeacher.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mStudentName, mStudentId, mStudentAge, mStudentPosition;
        private SimpleDraweeView mHead;
        private MaterialRippleLayout mp;

        public ViewHolder(View itemView) {
            super(itemView);
            mHead = (SimpleDraweeView) itemView.findViewById(R.id.student_head);
            mStudentName = (TextView) itemView.findViewById(R.id.student_name);
            mStudentAge = (TextView) itemView.findViewById(R.id.student_age);
            mStudentId = (TextView) itemView.findViewById(R.id.student_number);
            mStudentPosition = (TextView) itemView.findViewById(R.id.student_position);
            mp = (MaterialRippleLayout) itemView.findViewById(R.id.mp);
        }

        void bindView(TeacherInfo teacherInfo, final int position) {
            mStudentId.setText(teacherInfo.getJob_number() + "");
            mStudentAge.setText((teacherInfo.getClass_name().split(","))[0]);
            mStudentPosition.setText(teacherInfo.getTel());
            mStudentName.setText(teacherInfo.getName());
            Uri uri = Uri.parse("http://com-ace-teacher.oss-cn-shenzhen.aliyuncs.com/" + teacherInfo.getJob_number() + ".jpg");
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
