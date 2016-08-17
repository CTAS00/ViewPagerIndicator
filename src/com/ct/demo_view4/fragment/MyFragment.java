package com.ct.demo_view4.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {

	private static final String KEY = "TITLE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(getActivity());
		Bundle bundle = getArguments();
		String text = bundle.getString(KEY);
		tv.setText(text);
		tv.setGravity(Gravity.CENTER);

		return tv;
	}
    //activity创建fragment的时候需要做的事情
	// activity向fragment传递值的时候都是以bundle来传递的
	public static MyFragment newInstance(String text) {

		MyFragment myFragment = new MyFragment();
		Bundle bundle = new Bundle();
		bundle.putString(KEY, text);

		myFragment.setArguments(bundle);
		return myFragment;

	}


}
