package com.mut.workerinfoapp.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.mut.workerinfoapp.R;
import com.mut.workerinfoapp.Utils.Base2pic;
import com.mut.workerinfoapp.domain.Workerbean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Rightadpter extends RecyclerView.Adapter<Rightadpter.InnerHolder> {
    private List<Workerbean.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View Itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right, parent, false);
        return new InnerHolder(Itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
            ViewHolder viewHolder = new ViewHolder(holder.itemView);
        Workerbean.DataBean bean = data.get(position);
        viewHolder.tv_name.setText(bean.getPersonname());
        viewHolder.tv_banzu.setText(bean.getDepname());
        viewHolder.tv_cometime.setText(TimeUtils.millis2String(bean.getRecordtime()));
        viewHolder.tv_worktype.setText(bean.getWorktypename());
        if (bean.getCommtype().equals("进"))
        viewHolder.img_commtype.setImageResource(R.drawable.comein);
        if (bean.getCommtype().equals("出"))
            viewHolder.img_commtype.setImageResource(R.drawable.comeout);
        viewHolder.mImageView.setImageBitmap(Base2pic.stringtoBitmap(bean.getImagepath()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setdata(Workerbean workerbean) {
        data.clear();
        data.addAll(workerbean.getData());
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
        public TextView tv_name;
        public TextView tv_banzu;
        public TextView tv_worktype;
        public ImageView img_commtype;
        public TextView tv_cometime;
public ImageView mImageView;
        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.tv_banzu = (TextView) rootView.findViewById(R.id.tv_banzu);
            this.tv_worktype = (TextView) rootView.findViewById(R.id.tv_worktype);
            this.img_commtype = (ImageView) rootView.findViewById(R.id.img_commtype);
            this.tv_cometime = (TextView) rootView.findViewById(R.id.tv_cometime);
            this.mImageView=rootView.findViewById(R.id.img_person);
        }

    }



}
