package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentGrade;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.R;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class EndScoreAdapter extends RecyclerView.Adapter<EndScoreAdapter.ViewHolder> {
    private List<StudentGrade> mList = new ArrayList<>();
    private Context mContext;

    public EndScoreAdapter(Context context, List<StudentGrade> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.score_item, null));
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

        private TextView mTv_name, mTv_sid, mTv_yw, mTv_sx, mTv_wy, mTv_wl, mTv_hx, mTv_sw, mTv_dl, mTv_ls, mTv_zz, mTv_ty;

        public ViewHolder(View view) {
            super(view);
            mTv_name = (TextView) view.findViewById(R.id.item_name);
            mTv_sid = (TextView) view.findViewById(R.id.item_sid);
            mTv_yw = (TextView) view.findViewById(R.id.item_yw);
            mTv_sx = (TextView) view.findViewById(R.id.item_sx);
            mTv_wy = (TextView) view.findViewById(R.id.item_wy);
            mTv_wl = (TextView) view.findViewById(R.id.item_wl);
            mTv_hx = (TextView) view.findViewById(R.id.item_hx);
            mTv_sw = (TextView) view.findViewById(R.id.item_sw);
            mTv_dl = (TextView) view.findViewById(R.id.item_dl);
            mTv_ls = (TextView) view.findViewById(R.id.item_ls);
            mTv_zz = (TextView) view.findViewById(R.id.item_zz);
            mTv_ty = (TextView) view.findViewById(R.id.item_ty);
        }

        void bindView(StudentGrade studentGrade) {
            DbManager dbManager  = x.getDb(DBUtils.getStutdentInfoDaoConfig());
            String name = null;
            try {
                List<StudentInfo> studentInfoList = dbManager.selector(StudentInfo.class).where("sid","=",studentGrade.getSid()+"").findAll();
                if(studentInfoList != null && studentInfoList.size()>0){
                    name =studentInfoList.get(0).getName();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (name!=null){
                mTv_name.setText(name);
            }
            mTv_sid.setText(Long.toString(studentGrade.getSid()));
            mTv_yw.setText(studentGrade.getYw() + "");
            mTv_sx.setText(studentGrade.getSx() + "");
            mTv_wy.setText(studentGrade.getWy() + "");
            mTv_wl.setText(studentGrade.getWl() + "");
            mTv_hx.setText(studentGrade.getHx() + "");
            mTv_sw.setText(studentGrade.getSw() + "");
            mTv_dl.setText(studentGrade.getDl() + "");
            mTv_ls.setText(studentGrade.getLs() + "");
            mTv_zz.setText(studentGrade.getZz() + "");
            mTv_ty.setText(studentGrade.getTy() + "");
        }
    }
}
