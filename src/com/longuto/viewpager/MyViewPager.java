package com.longuto.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyViewPager extends ViewGroup {

	public MyViewPager(Context context) {
		super(context);
		initView();
	}

	public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private GestureDetector mGesture;	// 手势
	private Scroller mScroller;	// 滑动器
	private Context context;
	private OnMyPagerChangedListen mListen;	// 页面选择监听
	
	private void initView() {
		context = getContext();
		mScroller = new Scroller(context);
		mGesture = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){		
			/**el:起始滑动事件, e2:终点滑动事件, distanceX:x轴偏移量, distanceY:y轴偏移量*/
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				System.out.println("e1:" + e1.getAction() + "	e2:" + e2.getAction());
				scrollBy((int)distanceX, 0);	// 滑动多远距离
				return super.onScroll(e1, e2, distanceX, distanceY);
			}
		});
	}

	private int startX;
	private int startY;
	/** 是否向下传递触摸事件 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("onInterceptTouchEvent" + "ACTION_DOWN,");
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			// 下面判断ACTION_MOVE不传递给子控件,但是ACTION_DOWN确已经传递给子控件了,所以将此处动作传给手势识别器
			mGesture.onTouchEvent(ev);	
			break;
		case MotionEvent.ACTION_MOVE:
			System.out.println("onInterceptTouchEvent" + "ACTION_MOVE,");
			int endX = (int)ev.getX() - startX;
			int endY = (int)ev.getY() - startY;
			if(Math.abs(endX) > Math.abs(endY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGesture.onTouchEvent(event);	// 委托手势识别器处理触摸事件
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();	// 获取x的偏移量
			int position = scrollX/getWidth();	// 获取当前位置
			if(scrollX%getWidth() > getWidth()/2) {
				position++;
			}
			if(position >= getChildCount()) {
				position = getChildCount() - 1;
			}
			if(position < 0) {
				position = 0;
			}
			setCurrentPosition(position);
			break;
		default:
			break;
		}	
		return true;
	}
	
	/**
	 * 设置当前的页面位置
	 * @param position
	 */
	public void setCurrentPosition(int position) {
		int scrollX = getScrollX();	// 当前x的位置
		int posX = position * getWidth();	// 应该移动的位置
		int offSetX = posX - scrollX;	// 移动距离
		 /** 
		  * 调用Scroll执行滑动
		  * 参一:起始x的位置, 参二:起始y位置, 参三:x方向移动位置, 参四:y方向移动位置, 参五:持续时间
		  */
		mScroller.startScroll(scrollX, getScrollY(), offSetX, 0, Math.abs(offSetX));
		if(mListen != null) {
			mListen.myPagerChanged(position);
		}
	}

	@Override
	public void computeScroll() {
		// 判断滑动是否结束, 没结束执行以下方法
		if(mScroller.computeScrollOffset()) {
			int scrollCurrX = mScroller.getCurrX();
			scrollTo(scrollCurrX, 0);
		}
		invalidate();	// 刷新界面
		super.computeScroll();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);			
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).layout(l + getWidth() * i, 0, r+getWidth() * i, getHeight());
		}
	}
	
	/**
	 * 自定义回调接口
	 * @author Administrator
	 */
	public interface OnMyPagerChangedListen {
		public void myPagerChanged(int position);
	}
	
	public void setOnMyPagerChangedListen(OnMyPagerChangedListen listen) {
		mListen = listen;
	}
}
