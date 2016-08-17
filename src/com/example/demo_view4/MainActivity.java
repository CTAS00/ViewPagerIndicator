package com.example.demo_view4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

import com.ct.demo_view4.fragment.MyFragment;
import com.ct.demo_view4.weight.ViewPagerIndicator;

public class MainActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	//private MyFragment mFragment;
	
	private List<String> mListFragmentContent=Arrays.asList("����1","��ѯ1","����1","����2","��ѯ2","����2","����3","��ѯ3","����3");
	private List<MyFragment> mListFragment;
	
	
	private ViewPagerIndicator mPagerIndicator;
	private int mTagVisibleCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initDate();
	}

	private void initDate() {
		// TODO Auto-generated method stub
		
		
		
		mListFragment=new ArrayList<MyFragment>();
		for(int i=0;i<mListFragmentContent.size();i++){
			//ÿһ��fragment�ڴ�����ʱ���Ѿ������洫��������
			MyFragment Fragment=MyFragment.newInstance(mListFragmentContent.get(i));
			mListFragment.add(Fragment);
			
		}
		
		mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mListFragment.size();
			}
			
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				return mListFragment.get(position);
			}
		});
//		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			//������ʱ���ȥ����
//			@Override
//			public void onPageScrolled(int position, float percent, int arg2) {
//				// TODO Auto-generated method stub
//				//������ʱ����ҲҪȥ���»���ͼ��
//				mPagerIndicator.moveTriangle(position,percent);
//				
//				
//				
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
			
	
	}

	private void initView() {
		// TODO Auto-generated method stub
		mViewPager=(ViewPager) findViewById(R.id.viewpager);
		mPagerIndicator=(ViewPagerIndicator) findViewById(R.id.viewpagerIndicator);
		//ȥ���indicator�е�tag
		mTagVisibleCount=6;
		//��̬��ȥ����tag
		mPagerIndicator.addTag(mTagVisibleCount,mListFragmentContent);
		mPagerIndicator.setViewPager(mViewPager);
		
		
		mPagerIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "��ǰ���ڵ�"+position+"ҳ", 0).show();
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub1
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

		
	}

	
	
	
	
}
