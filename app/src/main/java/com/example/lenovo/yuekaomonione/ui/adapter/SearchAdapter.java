package com.example.lenovo.yuekaomonione.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.yuekaomonione.R;
import com.example.lenovo.yuekaomonione.bean.DataBean;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    Context context;
    List<DataBean> list;

    public SearchAdapter(Context context, List<DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.search_view, null);
            viewHolder = new ViewHolder();
            viewHolder.flowlayout_tv = convertView.findViewById(R.id.text_title);
            viewHolder.flowlayout_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, list.get(position).getTitle() + "", Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.flowlayout_tv.setText(list.get(position).getTitle());
        return convertView;
    }

    //创建ViewHolder类
    class ViewHolder {
        TextView flowlayout_tv;

    }
}