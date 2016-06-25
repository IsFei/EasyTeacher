package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.MessageInfoUtils;

import java.util.List;

/**
 * Created by lenovo on 2016-4-9.
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    private List<String> mDataset;
    private static LayoutInflater layoutInflater;
    public OnItemClickListener mItemClickListener;
    private List<StudentInfo> mListStudent;
    private List<MessageInfoUtils> mListPeople;

    private Context context;

    public PeopleAdapter(Context context, List<StudentInfo> mListStudent, List<MessageInfoUtils> messageInfoUtilsList) {
        super();
//        mDataset = dataset;
//        for (int i = 0; i < 30; i++) {
//            mDataset.add("item" + i);
//        }
        this.mListStudent = mListStudent;
        layoutInflater = LayoutInflater.from(context);
        mListPeople = messageInfoUtilsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_people, null);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // 绑定数据到ViewHolder上
//        viewHolder.textView.setText(mDataset.get(position));
        viewHolder.bindView(mListStudent.get(position / 2), position);
    }

    @Override
    public int getItemCount() {
        return mListStudent.size() * 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private TextView mName, mNumber;
        private ImageView mCheck;

        public ViewHolder(View itemView) {
            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.text);
            mName = (TextView) itemView.findViewById(R.id.people_name);
            mNumber = (TextView) itemView.findViewById(R.id.people_number);
            mCheck = (ImageView) itemView.findViewById(R.id.check);
            itemView.setOnClickListener(this);
        }

        void bindView(StudentInfo studentInfo, int position) {
            if (position % 2 == 0) {
                mName.setText(studentInfo.getFather() + "(" + studentInfo.getName() + "的父亲)");
                mNumber.setText(studentInfo.getFather_tel());
            } else {
                mName.setText(studentInfo.getMother() + "(" + studentInfo.getName() + "的母亲)");
                mNumber.setText(studentInfo.getMother_tel());
            }
            if (mListPeople.get(position).getIsCheck()) {
                mCheck.setVisibility(View.VISIBLE);
            } else mCheck.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
