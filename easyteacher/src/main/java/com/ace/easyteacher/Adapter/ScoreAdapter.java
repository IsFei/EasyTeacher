package com.ace.easyteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.Exam;
import com.ace.easyteacher.R;
import com.ace.easyteacher.View.MaterialRippleLayout;

import java.util.List;

/**
 * Created by lenovo on 2015-12-13.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    private List<String> mDataset;
    private static LayoutInflater layoutInflater;
    public OnItemClickListener mItemClickListener;
    private List<Exam> mListExam;

    private Context context;
    private int lastAnimatedPosition = -1;
    private int viewHeight = 0;

    public ScoreAdapter(Context context, List<Exam> mListExam) {
        super();
//        mDataset = dataset;
//        for (int i = 0; i < 30; i++) {
//            mDataset.add("item" + i);
//        }
        this.mListExam = mListExam;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_exam, null);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindView(mListExam.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mListExam.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mExam;
        private CardView mCard;
        private MaterialRippleLayout mp;

        public ViewHolder(View itemView) {
            super(itemView);
            mExam = (TextView) itemView.findViewById(R.id.text_exam);
            mCard = (CardView) itemView.findViewById(R.id.card);
            mp = (MaterialRippleLayout) itemView.findViewById(R.id.mp);
//            itemView.setOnClickListener(this);
        }

        void bindView(Exam exam, final int position) {
            mExam.setText(exam.getTime() + "  " + exam.getName());
            if (position % 2 == 0) {
                mCard.setCardBackgroundColor(0XFF0277bd);
            } else {
                mCard.setCardBackgroundColor(0XFF1db65e);
            }
            mp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, position);
                }
            });
        }

//        @Override
//        public void onClick(View v) {
//            if (mItemClickListener != null) {
//                mItemClickListener.onItemClick(v, getPosition());
//            }
//        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
