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
	// �����еĸ�������
	private int mTriangleWidth;
	private int mTriangleHeight;
	private int mInitTriangleX;
	private int mMoveTriangleX;

	// ��ʾ��tag������
	private int mTagCount;

	// Ĭ�ϵĻ���ʾ4��
	private static final int TAGCOUNT = 4;
	// ÿһ��triangle��ÿһ��tag��ȵ�1/6
	private static float TRIANGLE_WIDTH = 1 / 6f;

	private Path mPath;
	private Paint mPaint;

	private int mTagWidth;

	// �����洢tag
	private List<TextView> mListTag;

	// ��viewpager��ҳ�滬�����м���
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
		
		// ��ȡ�Զ��������
		TypedArray ty = context.obtainStyledAttributes(attrs,
				R.styleable.viewpagerindicator);

		mTagCount = ty.getInteger(
				R.styleable.viewpagerindicator_indicatorocunt, TAGCOUNT);

		ty.recycle();

		// ��ʼ������
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

	}

	/*
	 * step1:����һ�������� path paint caves զ����ȡ�����εĸ߶ȺͿ��
	 * 
	 * step2:ȥ����һ��������
	 * 
	 * step3:���һ�����ʱ�� ������Ҳһ����Ż��� �����ڶ��� ���»�����������оͿ�����
	 * 
	 * step4:���������� ͬʱviewgroup�а����ܶ������ ��ȡ��Ϣ�Ļ�����ͨ��finishflate
	 * 
	 * step5:��̬��ȥ���tag ��������ȥ����Ҫ��Ӷ��ٸ�tag
	 * 
	 * step6:tag�����ֵ���ɫ�仯�Լ�����¼��Ĵ���
	 */
	// ���ַ����ı�ʱ����� ���ݿؼ��Ŀ������������ �ؼ��Ŀ�߷����仯ʱ ��ص��ú���
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
	 * ��ʼ�������� (����Ҫȥ���ǻ滭�����ﰡʲô��)
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
	 * �����и���һ���ƶ�
	 * 
	 * @param position
	 *            ��ǰ��fragment
	 * @param percent
	 *            �ƶ��İٷֱ� 0-1
	 */

	public void moveTriangle(int position, float percent) {
		// TODO Auto-generated method stub
		// ÿ���ƶ��ľ��붼�ᷢ���ı�
		mMoveTriangleX = (int) (mTagWidth * percent) + mTagWidth * position;

		// �ƶ�����
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
	 * xml��Ϣ������ϻ�ص��˺���
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
	 * ��Ļ�Ŀ��
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
	 * ��̬��ȥ���tag
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
			//��view�е���¼��Ĵ���
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
	 * ��ɫ�ĸı�
	 */
	
	private void changeTagColor(View view,int color) {
		// TODO Auto-generated method stub
	TextView tv=(TextView) view;	
	tv.setTextColor(color);
		
		
	}
	/**
	 * ��ɫ����
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
