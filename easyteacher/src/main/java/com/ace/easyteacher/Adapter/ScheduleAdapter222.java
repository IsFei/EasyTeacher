package com.ace.easyteacher.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.TeacherSchedule;
import com.ace.easyteacher.R;

import java.util.List;


public class ScheduleAdapter222 extends RecyclerView.Adapter<ScheduleAdapter222.BaseViewHolder> {

    private final List<TeacherSchedule> mList;
    public OnItemClickListener mItemClickListener;

    public ScheduleAdapter222(List<TeacherSchedule> mList) {
        this.mList = mList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TextView tv_lesson;

        public BaseViewHolder(View itemView) {
            super(itemView);
            tv_lesson = (TextView) itemView.findViewById(R.id.tv_lesson);
            itemView.setOnClickListener(this);
        }

        public void bindView(TeacherSchedule bean) {
            tv_lesson.setText(bean.getSubject_name());
            tv_lesson.setBackgroundColor(Color.parseColor("#00B2EE"));
            tv_lesson.setAlpha(0.7f);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
