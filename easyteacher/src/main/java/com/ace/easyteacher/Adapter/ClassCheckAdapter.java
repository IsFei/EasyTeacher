package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.Check;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.R;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class ClassCheckAdapter extends RecyclerView.Adapter<ClassCheckAdapter.ViewHolder> {
    private List<Check> mList = new ArrayList<>();
    private Context mContext;

    public ClassCheckAdapter(Context context, List<Check> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.class_check_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextTime, mText, mName;

        public ViewHolder(View view) {
            super(view);

            mTextTime = (TextView) view.findViewById(R.id.item_time);
            mText = (TextView) view.findViewById(R.id.item_text);
            mName = (TextView) view.findViewById(R.id.item_name);

        }

        void bindView(Check check) {
            mTextTime.setText(check.getTime());
            mText.setText(check.getContent());
            DbManager dbManager = x.getDb(DBUtils.getStutdentInfoDaoConfig());
            String name = "";
            try {
                List<StudentInfo> studentInfoList = dbManager.selector(StudentInfo.class).where("sid", "=", check.getSid() + "").findAll();
               if(studentInfoList != null && studentInfoList.size()>0){
                   name = studentInfoList.get(0).getName();
               }
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (name != null) {
                mName.setText(name);
            }
        }
    }
}
