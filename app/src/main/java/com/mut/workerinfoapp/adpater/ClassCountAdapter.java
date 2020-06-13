package com.mut.workerinfoapp.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mut.workerinfoapp.R;
import com.mut.workerinfoapp.domain.ClassCount;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassCountAdapter extends RecyclerView.Adapter<ClassCountAdapter.InnerHolder> {
    private List<ClassCount.DataBean> mdata=new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classcount, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ViewHolder viewHolder = new ViewHolder(holder.itemView);
        ClassCount.DataBean dataBean = mdata.get(position);
        viewHolder.mTvClass.setText(dataBean.getType());
        viewHolder.mTvClassCount.setText(dataBean.getTotal());

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setData(ClassCount data) {

        mdata.clear();
        mdata.addAll(data.getData());
        notifyDataSetChanged();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static
    class ViewHolder {
        public View rootView;
        public TextView mTvClass;
        public TextView mTvClassCount;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTvClass = (TextView) rootView.findViewById(R.id.tv_class);
            this.mTvClassCount = (TextView) rootView.findViewById(R.id.tv_class_count);
        }

    }
}
