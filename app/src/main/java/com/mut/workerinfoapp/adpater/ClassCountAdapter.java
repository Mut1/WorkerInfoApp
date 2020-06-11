package com.mut.workerinfoapp.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mut.workerinfoapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassCountAdapter extends RecyclerView.Adapter<ClassCountAdapter.InnerHolder> {
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classcount, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        ViewHolder viewHolder = new ViewHolder(holder.itemView);


    }

    @Override
    public int getItemCount() {
        return 13;
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
