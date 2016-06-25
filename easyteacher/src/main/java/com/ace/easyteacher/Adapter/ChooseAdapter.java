package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.R;

import java.util.ArrayList;
import java.util.List;


public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.BaseViewHolder> {
    private List<ClassInfo> mList;
    public OnItemClickListener mItemClickListener;
    private Context mContext;
    private List<ClassInfo> mResultList = new ArrayList<>();

    public ChooseAdapter(Context context, List<ClassInfo> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.dialog_rv_item, null));
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
        private CheckBox checkBox;

        public BaseViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_class_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_check);

            itemView.setOnClickListener(this);
        }

        public void bindView(final ClassInfo bean) {
            tv_name.setText(bean.getClass_name());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mResultList.add(bean);
                    } else {
                        if (mResultList.contains(bean)) {
                            mResultList.remove(bean);
                        }
                    }
                }
            });
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
    public List<ClassInfo> getList(){
        return mResultList;
    }
}
