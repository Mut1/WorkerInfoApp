package com.mut.workerinfoapp.adpater;

import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.mut.workerinfoapp.R;
import com.mut.workerinfoapp.Utils.Base2pic;
import com.mut.workerinfoapp.domain.Workerbean;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Rightadpter extends RecyclerView.Adapter<Rightadpter.InnerHolder> {
    private static final String TAG ="Rightadpter" ;

    private List<Workerbean.DataBean> data = new ArrayList<>();
    private List<Workerbean.DataBean> removedErrorList = new ArrayList<>();

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
        removedErrorList.clear();
//        if (workerbean.getCode()==200) {
//            Collections.reverse(workerbean.getData());
//            for (int i = 0; i < workerbean.getData().size(); i++) {
//                Workerbean.DataBean currentbean = workerbean.getData().get(i);
//                Log.d(TAG, "workin&out size ---> :"+ workerbean.getData().size());
//                if (removedErrorList.size()==0) {
//                    removedErrorList.add(currentbean);
//                }
//                else
//                {
//
//                    String currentbeanPersonidcardcode = currentbean.getPersonidcardcode();
//                    String personidcardcode = removedErrorList.get(removedErrorList.size()-1).getPersonidcardcode();
//                    String currentbeanCommtype = currentbean.getCommtype();
//                    String commtype = removedErrorList.get(removedErrorList.size()-1).getCommtype();
//                    if (currentbeanPersonidcardcode.equals(personidcardcode))
//                    {
//                        if (currentbeanCommtype.equals(commtype))
//                        {
//
//                        }
//                        else
//                        {
//                            removedErrorList.add(currentbean);
//                        }
//                    }
//                    else
//                    {
//                        removedErrorList.add(currentbean);
//                    }
//                }
//            }
//
//            data.addAll(removedErrorList);
//
//        }
        if (workerbean.getCode()== HttpURLConnection.HTTP_OK) {
            Log.d(TAG, "workin&out size ---> :"+ workerbean.getData().size());
            data.addAll(workerbean.getData());
        }
        Collections.reverse(data);
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
