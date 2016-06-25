package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.Check;
import com.ace.easyteacher.R;

import java.util.ArrayList;
import java.util.List;


public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {
    private List<Check> mList = new ArrayList<>();
    private Context mContext;

    public CheckAdapter(Context context, List<Check> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.check_item, parent,false));
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

        private TextView mTextTime, mText;

        public ViewHolder(View view) {
            super(view);

            mTextTime = (TextView) view.findViewById(R.id.item_time);
            mText = (TextView) view.findViewById(R.id.item_text);

        }

        void bindView(Check check) {
            mTextTime.setText(check.getTime());
            mText.setText(check.getContent());
        }
    }
}
