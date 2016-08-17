package com.ct.demo_view4.weight;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_view4.R;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout {
	// 三角行的各种属性
	private int mTriangleWidth;
	private int mTriangleHeight;
	private int mInitTriangleX;
	private int mMoveTriangleX;

	// 显示的tag的数量
	private int mTagCount;

	// 默认的话显示4个
	private static final int TAGCOUNT = 4;
	// 每一个triangle是每一个tag宽度的1/6
	private static float TRIANGLE_WIDTH = 1 / 6f;

	private Path mPath;
	private Paint mPaint;

	private int mTagWidth;

	// 用来存储tag
	private List<TextView> mListTag;

	// 对viewpager的页面滑动进行监听
	private ViewPager mViewPager;

	private OnPageChangeListener mChangeListener;
	
	


	public void setOnPageChangeListener(
			OnPageChangeListener onPageChangeListener) {

		this.mChangeListener = onPageChangeListener;

	}

	public ViewPagerIndicator(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		// 获取自定义的属性
		TypedArray ty = context.obtainStyledAttributes(attrs,
				R.styleable.viewpagerindicator);

		mTagCount = ty.getInteger(
				R.styleable.viewpagerindicator_indicatorocunt, TAGCOUNT);

		ty.recycle();

		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

	}

	/*
	 * step1:绘制一个三角行 path paint caves 咋样获取三角形的高度和宽度
	 * 
	 * step2:去绘制一个三角行
	 * 
	 * step3:当我滑动的时候 三角行也一起跟着滑动 类似于动画 重新绘制这个三角行就可以了
	 * 
	 * step4:让容器滚动 同时viewgroup中包含很多的子类 获取信息的话可以通过finishflate
	 * 
	 * step5:动态的去添加tag 让主函数去决定要添加多少个tag
	 * 
	 * step6:tag上文字的颜色变化以及点击事件的处理
	 */
	// 布局发生改变时候调用 根据控件的宽高来设置属性 控件的宽高发生变化时 会回调该函数
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d("ViewPagerIndicator", "onSizeChanged");
		mTagWidth = w / mTagCount;

		mTriangleWidth = (int) (w / mTagCount * TRIANGLE_WIDTH);
		mTriangleHeight = mTriangleWidth / 2;

		mInitTriangleX = w / mTagCount / 2 - mTriangleWidth / 2;

		Log.d("ViewPagerIndicator", "onSizeChanged" + h + "...." + oldh);

		initTriangle();

	}

	/**
	 * 初始化三角行 (不需要去考虑绘画在哪里啊什么的)
	 */

	private void initTriangle() {
		// TODO Auto-generated method stub
		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
		mPath.close();

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.d("ViewPagerIndicator", "dispatchDraw");

		canvas.save();
		canvas.translate(mInitTriangleX + mMoveTriangleX, getHeight());
		canvas.drawPath(mPath, mPaint);

		canvas.restore();

		super.dispatchDraw(canvas);
	}

	/**
	 * 三角行跟着一起移动
	 * 
	 * @param position
	 *            当前的fragment
	 * @param percent
	 *            移动的百分比 0-1
	 */

	public void moveTriangle(int position, float percent) {
		// TODO Auto-generated method stub
		// 每次移动的距离都会发生改变
		mMoveTriangleX = (int) (mTagWidth * percent) + mTagWidth * position;

		// 移动容器
		if (position >= mTagCount - 2 && percent > 0
				&& getChildCount() > mTagCount
				&& position <= getChildCount() - 3) {
			// this.scrollTo((position - (indiccatorCount - 2)) * indicator
			// + (int) (indicator * percent), 0);
			this.scrollTo(
					(int) ((position - (mTagCount - 2)) * mTagWidth + (int) mTagWidth
							* percent), 0);

		}
		invalidate();

	}

	/**
	 * xml信息加载完毕会回调此函数
	 */
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		Log.d("ViewPagerIndicator", "onFinishInflate");
		super.onFinishInflate();
		int ChildSum = getChildCount();
		for (int i = 0; i < ChildSum; i++) {
			View view = getChildAt(i);
			LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
			layoutParams.weight = 0;
			layoutParams.width = getScreenWidth() / mTagCount;
			view.setLayoutParams(layoutParams);

		}

	}

	/**
	 * 屏幕的宽度
	 * 
	 * @return
	 */
	private int getScreenWidth() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Service.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		return outMetrics.widthPixels;
	}

	/**
	 * 动态的去添加tag
	 */

	public void addTag(int tagvisiblecount, List<String> mListFragmentContent) {
		// TODO Auto-generated method stub
		// mListTagText=mListFragmentContent;
		mListTag=new ArrayList<TextView>();
		mTagCount = tagvisiblecount;
		int position=0;
		for (String string : mListFragmentContent) {
			TextView view = new TextView(getContext());
			LinearLayout.LayoutParams ll = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			ll.width = getScreenWidth() / tagvisiblecount;
			view.setText(string);
			view.setTextColor(Color.BLACK);
			view.setGravity(Gravity.CENTER);
			view.setLayoutParams(ll);
			view.setTag(position);

			addView(view);
			mListTag.add(view);
			position++;
			//对view中点击事件的处理
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					int position=(Integer) view.getTag();
					resetTagColor();
					changeTagColor(view, Color.WHITE);
					//moveTriangle(position, 1);
					mViewPager.setCurrentItem(position);
				}

			

			
			});
			
		

		}

	}

	public void setViewPager(ViewPager ViewPager) {
		// TODO Auto-generated method stub
		mViewPager = ViewPager;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				mChangeListener.onPageSelected(position);
				resetTagColor();
				changeTagColor(position,Color.WHITE);

			}

			@Override
			public void onPageScrolled(int position, float percent, int arg2) {
				// TODO Auto-generated method stub
				moveTriangle(position, percent);
				
				mChangeListener.onPageScrolled(position, percent, arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				mChangeListener.onPageScrollStateChanged(arg0);
			}
		});
	}

	
	protected void changeTagColor(int position,int color) {
		// TODO Auto-generated method stub
		TextView textView=mListTag.get(position);
		textView.setTextColor(color);
	}

	/**
	 * 颜色的改变
	 */
	
	private void changeTagColor(View view,int color) {
		// TODO Auto-generated method stub
	TextView tv=(TextView) view;	
	tv.setTextColor(color);
		
		
	}
	/**
	 * 颜色重置
	 */
	private void resetTagColor() {
	
		for(TextView textView:mListTag){
			textView.setTextColor(Color.BLACK);
			
		}
	
		
			
	}
	

//	private void changeTagColor(int position,int color){
//		TextView tv=mListTag.get(position);
//		
//		tv.setTextColor(color);
//
//		
//		
//		
//	}
	
	

}
