package com.longuto.viewpager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private final int[] mImageRes = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5};
	private Context context;	// 上下文
	private MyViewPager mMyViewPager;	// 自定义Viewpager 
	private RadioGroup mPointSetRgp;	// 點集合
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        mMyViewPager = (MyViewPager) findViewById(R.id.mvp_diyViewGroup);
        mPointSetRgp = (RadioGroup) findViewById(R.id.rgp_pointSet);
        
        for (int i = 0; i < mImageRes.length; i++) {
        	ImageView imageView = new ImageView(context);
        	imageView.setBackgroundResource(mImageRes[i]);
        	mMyViewPager.addView(imageView);
        	// 添加测试页图片
        	if(i == 0) {
        		ScrollView testSv = new ScrollView(context);
        		View view = View.inflate(context, R.layout.list_item_test, null);
        		testSv.addView(view);
        		mMyViewPager.addView(testSv);
        	}
		}
        for(int i = 0; i <= mImageRes.length; i++) {
        	RadioButton rb = new RadioButton(context);
        	rb.setId(i);
        	if(i == 0) {
        		rb.setChecked(true);
        	}
        	mPointSetRgp.addView(rb);
        }
        
        
        mMyViewPager.setOnMyPagerChangedListen(new MyViewPager.OnMyPagerChangedListen() {
			@Override
			public void myPagerChanged(int position) {
				((RadioButton)mPointSetRgp.getChildAt(position)).setChecked(true);
				Toast.makeText(context, "当前页面为:" + position , 0).show();
			}
        });
        mPointSetRgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mMyViewPager.setCurrentPosition(checkedId);
			}
		});
    }


}
