package com.cs597.bestdeal;

import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.entities.Product;
import com.cs597.bestdeal.entities.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, 
        		   HomeRightFragment.HomeRightCallbacks,
        		   ProfileFragment.ProfileCallbacks	{
	
	static final private int LOGIN_REQUEST = 1;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    
    private Fragment currentFragment;
	private Fragment lastFragment;
	
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        Intent intent = this.getIntent();
		user = intent.getParcelableExtra("USER");
		
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), user);
    }
    
    @Override
    public void onHomeRightItemSelected(String title) {
    	onNavigationDrawerItemSelected(title);
    }

    @Override
    public void onNavigationDrawerItemSelected(String title) {
    	FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		currentFragment = fragmentManager.findFragmentByTag(title);
		if(currentFragment == null) {
			if(title.equals("Login"))
			{
				currentFragment = HomeRightFragment.newInstance(title,user);
				Intent login = new Intent(HomeActivity.this, LoginActivity.class);
				startActivityForResult(login, LOGIN_REQUEST);
			}
			else if(title.equals("Profile"))
			{
				currentFragment = ProfileFragment.newInstance(title,user);
				ft.add(R.id.container, currentFragment, title);
			}
			else if(title.equals("Home"))
			{
				currentFragment = HomeRightFragment.newInstance(title,user);
				ft.add(R.id.container, currentFragment, title);
			}
			else if(title.equals("Favorite"))
			{
				currentFragment = FavoriteFragment.newInstance(title, user);
				ft.add(R.id.container, currentFragment, title);
			}
			else if(title.equals("Search"))
			{
				currentFragment = SearchFragment.newInstance(title, user);
				ft.add(R.id.container, currentFragment, title);
			}
			else
			{
				currentFragment = ContentFragment.newInstance(title);
				ft.add(R.id.container, currentFragment, title);
			}
			
		}
		if(lastFragment != null) {
			ft.hide(lastFragment);
		}
		if(currentFragment.isDetached()){
			ft.attach(currentFragment);
		}
		ft.show(currentFragment);
		lastFragment = currentFragment;
		ft.commit();
		onSectionAttached(title);
    }

    public void onSectionAttached(String title) {
		mTitle = title;
	}

    public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == LOGIN_REQUEST)
		{
			Bundle bundle = intent.getExtras();
			user = bundle.getParcelable("user");
			mNavigationDrawerFragment.user = user;
			onNavigationDrawerItemSelected("Home");
		}
		else
		{
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			//we have a result
			String scanContent = scanningResult.getContents();
			
			ContentResolver resolver = this.getContentResolver();
			Cursor cursor = resolver.query(ProductContract.CONTENT_URI, null, "Products.bar = " + scanContent, null, null);
			if(cursor.getCount() == 0)
			{
				Toast.makeText(this.getApplicationContext(), "No such product.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				cursor.moveToFirst();
				Product product = new Product(cursor);
				Intent p = new Intent(this, ProductActivity.class);
				p.putExtra("USER", user);
				p.putExtra("PRODUCT_ID", product.id);
				startActivity(p);
			}
		} 
		else{
			 Toast.makeText(this.getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
		}
		}
	}

    public static class ContentFragment extends Fragment {
		
		private static final String ARG_SECTION_TITLE = "section_title";

		public static ContentFragment newInstance(String title) {
			ContentFragment fragment = new ContentFragment();
			Bundle args = new Bundle();
			args.putString(ARG_SECTION_TITLE, title);
			fragment.setArguments(args);
			return fragment;
		}

		public ContentFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			TextView textView1 = (TextView) rootView.findViewById(R.id.section_label1);
			TextView textView2 = (TextView) rootView.findViewById(R.id.section_label2);
			TextView textView3 = (TextView) rootView.findViewById(R.id.section_label3);
			textView1.setText("BestDeal App is a phone app that’s developed for you to have fun and to feel "
					+ "comfortable while shopping. It focuses more on these categories (Clothes,"
					+ "Accessories, and Shoes).");
			textView2.setText("BestDeal should be able to get you the cheapest"
					+ "prices either online or in stores around you."
					+ "We are a team of developers who is looking into the world newest ways of"
					+ "mobile apps marketing." );
			textView3.setText("Big Thanks to these developers:"
					+ "Hadeel Alhosaini ­ Zhenzhe Hu ­ Chia­Wei Chiang ­ Mohammed Alqasimi");
			return rootView;
		}
    }

	@Override
	public void onProfileItemSelected(String title) {
		onNavigationDrawerItemSelected(title);		
	}
}
