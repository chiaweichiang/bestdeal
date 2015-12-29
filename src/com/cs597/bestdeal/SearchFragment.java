package com.cs597.bestdeal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cs597.bestdeal.contracts.ProductContract;
import com.cs597.bestdeal.entities.Product;
import com.cs597.bestdeal.entities.User;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchFragment extends Fragment {
	
	private static final String ARG_SECTION_TITLE = "section_title";
	
	private User user;
	private ListView resultList;
	private SearchAdapter adapter;	
	private SearchView searchview;
	private String selection ="";
	private CheckBox online;
	private CheckBox instore;
	private String selectionOnline = " AND Products.type = 0";
	private String selectionInstore = " AND Products.type = 1";
	private String selectionSort = " ORDER BY Products.price";

	public static SearchFragment newInstance(String title, User user) {
		SearchFragment fragment = new SearchFragment();
		fragment.user = user;
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.search, container, false);
		searchview = (SearchView) view.findViewById(R.id.search_searchView);
		online = (CheckBox) view.findViewById(R.id.search_online_checkBox);
		instore = (CheckBox) view.findViewById(R.id.search_instore_checkBox);
		resultList = (ListView) view.findViewById(R.id.search_result_listView);
		
		adapter = new SearchAdapter(getActivity(),null);
		resultList.setAdapter(adapter);		
		View emptyView = view.findViewById(android.R.id.empty);
		resultList.setEmptyView(emptyView);
		resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("USER", user);
                intent.putExtra("PRODUCT_ID", arg3);
                startActivity(intent);			
			}
		});
		
		searchview.setIconifiedByDefault(true);  
		searchview.onActionViewExpanded();  
		searchview.setFocusable(false);  
		searchview.clearFocus();
		searchview.setOnQueryTextListener(new OnQueryTextListener() {  
            @Override  
            public boolean onQueryTextChange(String queryText) {
            	selection = ProductContract.NAME + " LIKE '%" + queryText + "%' ";
            	String Qselection;
            	if(online.isChecked() && instore.isChecked())
            	{
            		Qselection = selection;
            	}
            	else if(online.isChecked())
            	{
            		Qselection = selection + selectionOnline;
            	}
            	else
            	{
            		Qselection = selection + selectionInstore;
            	}
                ContentResolver resolver = getActivity().getContentResolver();
        		Cursor cursor = resolver.query(ProductContract.CONTENT_URI, null, Qselection, null, null); 
                adapter.changeCursor(cursor);  
                return true;  
            }
  
            @Override  
            public boolean onQueryTextSubmit(String queryText) {  
                if (searchview != null) {  
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
                    if (imm != null) {  
                        imm.hideSoftInputFromWindow(  
                        		searchview.getWindowToken(), 0);  
                    }  
                    searchview.clearFocus();  
                }  
                return true;  
            }  
        });
		
		online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	String Cselection = "";
                if(isChecked){
                	if(!selection.equals(""))
                	{
                		if(instore.isChecked())
                		{
                			Cselection = selection;
                		}
                		else
                		{
                			Cselection = selection + selectionOnline;
                		}
                	}
                }
                else{
                	if(!selection.equals(""))
                	{
                		if(instore.isChecked())
                		{
                			Cselection = selection + selectionInstore;
                		}
                	}
                }
                ContentResolver resolver = getActivity().getContentResolver();
        		Cursor cursor = resolver.query(ProductContract.CONTENT_URI, null, Cselection, null, null);
        		adapter.changeCursor(cursor);
            } 
        });
		
		instore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	String Cselection = "";
                if(isChecked){
                	if(!selection.equals(""))
                	{
                		if(online.isChecked())
                		{
                			Cselection = selection;
                		}
                		else
                		{
                			Cselection = selection + selectionInstore;
                		}
                	}
                }
                else{
                	if(!selection.equals(""))
                	{
                		if(online.isChecked())
                		{
                			Cselection = selection + selectionOnline;
                		}
                	}
                }
                ContentResolver resolver = getActivity().getContentResolver();
        		Cursor cursor = resolver.query(ProductContract.CONTENT_URI, null, Cselection, null, null);
        		adapter.changeCursor(cursor);
            } 
        });	
		
		Spinner spinner = (Spinner) view.findViewById(R.id.search_spinner);
		String[] mItems = getResources().getStringArray(R.array.spinnername);
		ArrayAdapter<String> Aadapter = new ArrayAdapter<String> (getActivity(),android.R.layout.simple_spinner_item, mItems);
		spinner.setAdapter(Aadapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view,
		            int position, long id) {
		    	String Sselection = "";
		    	if(!selection.equals(""))
		    	{
		        if(position == 0)
		        {
		        	if(online.isChecked() && instore.isChecked())
            		{
		        		Sselection = selection;
            		}
            		else if(online.isChecked())
            		{
            			Sselection = selection + selectionOnline;
            		}
            		else
            		{
            			Sselection = selection + selectionInstore;
            		}
		        }
		        else if(position == 1)
		        {
		        	if(online.isChecked() && instore.isChecked())
            		{
		        		Sselection = selection + selectionSort;
            		}
            		else if(online.isChecked())
            		{
            			Sselection = selection + selectionOnline + selectionSort;
            		}
            		else
            		{
            			Sselection = selection + selectionInstore + selectionSort;
            		}
		        }
		        else
		        {
		        	if(online.isChecked() && instore.isChecked())
            		{
		        		Sselection = selection;
            		}
            		else if(online.isChecked())
            		{
            			Sselection = selection + selectionOnline;
            		}
            		else
            		{
            			Sselection = selection + selectionInstore;
            		}
		        }
		        ContentResolver resolver = getActivity().getContentResolver();
        		Cursor cursor = resolver.query(ProductContract.CONTENT_URI, null, Sselection, null, null);
        		adapter.changeCursor(cursor);
		    	}
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        // TODO Auto-generated method stub
		    }
		});
		
		return view;
	}
	
	public class SearchAdapter extends ResourceCursorAdapter {
		
		protected final static int ROW_LAYOUT = R.layout.search_row;
		
		public SearchAdapter(Context context, Cursor cursor) {
			super(context, ROW_LAYOUT, cursor, 0);
		}
		
		@Override
		public View newView(Context context, Cursor cur, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater)
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(ROW_LAYOUT, parent, false);
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ImageView productimg = (ImageView) view.findViewById(R.id.searchrow_product_img);
			TextView productname = (TextView) view.findViewById(R.id.searchrow_product_name);
			TextView productprice = (TextView) view.findViewById(R.id.searchrow_product_price);
			
			Product product = new Product(cursor);
			productname.setText(product.name);
			productprice.setText("$ " + product.price);
			Bitmap bitmap = getLoacalBitmap("/Product/"+ product.id +".jpg");
			if(bitmap == null)
			{
				productimg.setBackgroundResource(R.drawable.default_product_pic);
			}
			else
			{
				productimg.setImageBitmap(bitmap);
			}
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
	}
}
