package com.example.souso;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TitleItem extends LinearLayout {
    //2.定义接口对象
    private OnTitleClickListener listener;
    private EditText titleEd;
    private Button titleBut;

    public TitleItem(Context context) {
        this(context,null);
    }

    public TitleItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化控件上的方法
        initView(context);
    }

    /**
     * 初始化控件上的方法
     * @param context
     */
    private void initView(Context context) {
        View view = View.inflate(context, R.layout.title_item, this);
        titleEd=view.findViewById(R.id.title_ed);
        titleBut=view.findViewById(R.id.title_but);
        titleBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = titleEd.getText().toString();
                //5.添加一个触发事件
                listener.onClick(s);
            }
        });
    }
    //1.定义一个接口
    public interface OnTitleClickListener{
        void onClick(String s);
    }
    //3为接口对象赋值的方法
    void setOnTitleClickListener(OnTitleClickListener listener){
        this.listener=listener;
    }
}