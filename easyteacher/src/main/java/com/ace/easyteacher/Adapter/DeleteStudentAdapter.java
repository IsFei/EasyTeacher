package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.R;

import java.util.List;


public class DeleteStudentAdapter extends RecyclerView.Adapter<DeleteStudentAdapter.BaseViewHolder> {
    private List<StudentInfo> mList;
    public OnItemClickListener mItemClickListener;
    private Context mContext;

    public DeleteStudentAdapter(Context context, List<StudentInfo> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.common_list_item, null));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TextView tv_name;
        private TextView tv_sid;

        public BaseViewHolder(View itemView) {
            super(itemView);
            tv_sid = (TextView) itemView.findViewById(R.id.item_tv_job_number);
            tv_name = (TextView) itemView.findViewById(R.id.item_tv_name);
            itemView.setOnClickListener(this);
        }

        public void bindView(StudentInfo bean) {
            tv_name.setText(bean.getName());
            tv_sid.setText(bean.getSid() + "");
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
