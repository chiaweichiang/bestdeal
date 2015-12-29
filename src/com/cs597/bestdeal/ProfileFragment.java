package com.cs597.bestdeal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cs597.bestdeal.R;
import com.cs597.bestdeal.entities.User;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

	private static final String ARG_SECTION_TITLE = "section_title";
	
	private User user;
	
	private ProfileCallbacks mCallbacks;
	
	public static ProfileFragment newInstance(String title, User user) {
		ProfileFragment fragment = new ProfileFragment();
		fragment.user = user;
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.profile, container, false);
		
		ImageView user_img = (ImageView) view.findViewById(R.id.profile_user_img);
		TextView user_text = (TextView) view.findViewById(R.id.profile_user_name);
		user_text.setText(user.username);
		Bitmap bitmap = getLoacalBitmap("/User/"+ user.id +".jpg");
		if(bitmap == null)
		{
			user_img.setBackgroundResource(R.drawable.default_profile_pic);
		}
		else
		{
			user_img.setImageBitmap(bitmap);
		}
				
		TextView review = (TextView) view.findViewById(R.id.profile_review);
		review.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("USER", user);
                startActivity(intent);
            }
        });

		TextView favorite = (TextView) view.findViewById(R.id.profile_favorite);
		favorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	selectItem("Favorite");
            }
        });

		TextView scan = (TextView) view.findViewById(R.id.profile_history);
		scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getActivity(), HistoryActivity.class);
                intent.putExtra("USER", user);
                startActivity(intent);
            }
        });
		
		return view;
	}
	
	 public Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	
	private void selectItem(String item) {
		if (mCallbacks != null) {
			mCallbacks.onProfileItemSelected(item);
	    }
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (ProfileCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement HomeRightCallbacks.");
        }
    }
	
	 @Override
	 public void onDetach() {
		 super.onDetach();
		 mCallbacks = null;
	 }
	
	public static interface ProfileCallbacks {
        void onProfileItemSelected(String title);
    }
}
