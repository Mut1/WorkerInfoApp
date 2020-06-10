package com.mut.workerinfoapp.adpater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.mut.workerinfoapp.R;
import com.mut.workerinfoapp.Utils.Base2pic;
import com.mut.workerinfoapp.domain.workerbean;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Rightadpter extends RecyclerView.Adapter<Rightadpter.InnerHolder> {
    private List<workerbean.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View Itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right, parent, false);
        return new InnerHolder(Itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
            ViewHolder viewHolder = new ViewHolder(holder.itemView);
        workerbean.DataBean bean = data.get(position);
        viewHolder.tv_name.setText(bean.getPersonname());
        viewHolder.tv_banzu.setText(bean.getDepname());
        viewHolder.tv_cometime.setText(TimeUtils.millis2String(bean.getRecordtime()));
        viewHolder.tv_worktype.setText(bean.getWorktypename());
        if (bean.getCommtype().equals("进"))
        viewHolder.img_commtype.setImageResource(R.drawable.comein);
        if (bean.getCommtype().equals("出"))
            viewHolder.img_commtype.setImageResource(R.drawable.comeout);
        String bas="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";
        viewHolder.mImageView.setImageBitmap(Base2pic.stringtoBitmap(bas));
           /*
        import android.util.Base64;

# 代码片段
String base64 = "data:image/png;base64......"
byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
imageView.setImageBitmap(decodedByte);


         */
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setdata(workerbean workerbean) {
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
