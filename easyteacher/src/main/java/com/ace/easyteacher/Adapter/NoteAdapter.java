package com.ace.easyteacher.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.Note;
import com.ace.easyteacher.R;
import com.ace.easyteacher.View.MaterialRippleLayout;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.BaseViewHolder> {

    private final List<Note> mList;
    private itemListener mListener;

    public NoteAdapter(List<Note> mList) {
        this.mList = mList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface itemListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(itemListener listener) {
        this.mListener = listener;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time;
        private TextView tv_content;
        private MaterialRippleLayout rp;
        public BaseViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            rp = (MaterialRippleLayout) itemView.findViewById(R.id.rp);
        }
        public void bindView(Note bean, final int position){
            tv_time.setText(bean.getCreate_time());
            tv_content.setText(bean.getContent());
            rp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, position);
                }
            });
        }
    }
}
