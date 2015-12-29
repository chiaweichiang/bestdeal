package com.cs597.bestdeal;

import com.cs597.bestdeal.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.entities.Product;
import com.cs597.bestdeal.entities.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class HomeRightFragment extends Fragment{
	
	private static final String ARG_SECTION_TITLE = "section_title";
	public User user;
	
	private HomeRightCallbacks mCallbacks;
	
	public static HomeRightFragment newInstance(String title, User user) {
		HomeRightFragment fragment = new HomeRightFragment();
		fragment.user = user;
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.home_right_fragment, container, false);
		
		TextView nearby = (TextView) view.findViewById(R.id.home_nearby);
		nearby.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra("USER", user);
                startActivity(intent);
            }
        });

		TextView search = (TextView) view.findViewById(R.id.home_search);
		search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	selectItem("Search");
            }
        });

		TextView scan = (TextView) view.findViewById(R.id.home_scan);
		scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
    			scanIntegrator.initiateScan();
            }
        });

        
		return view;
	}
	
	private void selectItem(String item) {
        if (mCallbacks != null) {
        	mCallbacks.onHomeRightItemSelected(item);
        }
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (HomeRightCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement HomeRightCallbacks.");
        }
    }
	
	 @Override
	 public void onDetach() {
		 super.onDetach();
		 mCallbacks = null;
	 }
	
	public static interface HomeRightCallbacks {
        void onHomeRightItemSelected(String title);
    }
}