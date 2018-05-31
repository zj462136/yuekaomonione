package com.example.lenovo.yuekaomonione.ui.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.yuekaomonione.R;
import com.example.lenovo.yuekaomonione.bean.SearchBean;
import com.example.lenovo.yuekaomonione.ui.search.SearchActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MySearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SearchBean.DataBean> list;
    private LayoutInflater inflater;
    public MySearchAdapter(Context context, List<SearchBean.DataBean> list) {
        this.context=context;
        this.list=list;
        inflater = LayoutInflater.from(context);
    }
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.xrv_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        SearchBean.DataBean dataBean = list.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        String imgUrls =dataBean.getImages();
        String imgUrl = "";
        if (!TextUtils.isEmpty(imgUrls)) {
            imgUrl = imgUrls.split("\\|")[0];
        }
        myViewHolder.iv.setImageURI(imgUrl);
        myViewHolder.tvName.setText("￥"+dataBean.getBargainPrice());
        myViewHolder.tvTime.setText(dataBean.getTitle());
        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView iv;
        private final TextView tvName;
        private final TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_img);
            tvName = itemView.findViewById(R.id.item_price);
            tvTime = itemView.findViewById(R.id.item_title);
        }
    }

    public void refresh(List<SearchBean.DataBean> tempList) {
        //清楚原先集合里的内容
        this.list.clear();
        this.list.addAll(tempList);
        notifyDataSetChanged();
    }

    public void loadMore(List<SearchBean.DataBean> tempList) {
        this.list.addAll(tempList);
        notifyDataSetChanged();
    }

    public int getListNum(){
        return this.list.size();
    }

}