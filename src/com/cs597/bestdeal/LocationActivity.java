package com.cs597.bestdeal;

import android.os.AsyncTask;
import android.os.Bundle;

import com.cs597.bestdeal.contracts.StoreContract;
import com.cs597.bestdeal.entities.Mark;
import com.cs597.bestdeal.entities.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class LocationActivity extends FragmentActivity implements ConnectionCallbacks,
														OnConnectionFailedListener,
														LocationListener,
														OnMyLocationButtonClickListener,
														OnMapReadyCallback {

	    private GoogleApiClient mGoogleApiClient;
	    
	    private GoogleMap map;
	    
	    private int count = 0;
	    
	    private User user;
	    
	    private ContentResolver resolver;

	    // These settings are the same as the settings for the map. They will in fact give you updates
	    // at the maximal rates currently possible.
	    private static final LocationRequest REQUEST = LocationRequest.create()
	            .setInterval(5000)         // 5 seconds
	            .setFastestInterval(16)    // 16ms = 60fps
	            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.location);

	        Intent intent = this.getIntent();
			user = intent.getParcelableExtra("USER");
			
			resolver = this.getContentResolver();
			
	        SupportMapFragment mapFragment =
	                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	        mapFragment.getMapAsync(this);
	        
	        mGoogleApiClient = new GoogleApiClient.Builder(this)
	                           .addApi(LocationServices.API)
	                           .addConnectionCallbacks(this)
	                           .addOnConnectionFailedListener(this)
	                           .build();
	        
	        map = mapFragment.getMap();
	        map.setInfoWindowAdapter(new InfoWindowAdapter(){

				private View mInfoWindowContent = null;
				
				@Override
				public View getInfoContents(Marker marker) {
					LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
					if (mInfoWindowContent == null) {
						mInfoWindowContent = mInflater.inflate(R.layout.info_window, null);
					}
					TextView infoTitle = (TextView) mInfoWindowContent.findViewById(R.id.info_title);
					infoTitle.setText(marker.getTitle());
					TextView infoSnippet = (TextView) mInfoWindowContent.findViewById(R.id.info_snippet);
					infoSnippet.setText(marker.getSnippet());
					return mInfoWindowContent;
				}

				@Override
				public View getInfoWindow(Marker marker) {
					return null;
				}
			});
	        
			MarkStoreTask task = new MarkStoreTask();
			task.execute("");
			
	    }

	    @Override
	    protected void onResume() {
	        super.onResume();
	        mGoogleApiClient.connect();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        mGoogleApiClient.disconnect();
	    }

	    @Override
	    public void onMapReady(GoogleMap map) {
	        map.setMyLocationEnabled(true);
	        map.setOnMyLocationButtonClickListener(this);
	    }

	    /**
	     * Implementation of {@link LocationListener}.
	     */
	    @Override
	    public void onLocationChanged(Location location) {
	        //mMessageView.setText("Location = " + location);
	    	if(count == 0)
	    	{
	    		LatLng client = new LatLng(location.getLatitude(),location.getLongitude());
				CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(client)
					.zoom(13)
					.bearing(0)
					.tilt(30)
					.build();
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				count++;
	    	}
	    }

	    /**
	     * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
	     */
	    @Override
	    public void onConnected(Bundle connectionHint) {
	        LocationServices.FusedLocationApi.requestLocationUpdates(
	                mGoogleApiClient,
	                REQUEST,
	                this);  // LocationListener
	    }

	    /**
	     * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
	     */
	    @Override
	    public void onConnectionSuspended(int cause) {
	        // Do nothing
	    }

	    /**
	     * Implementation of {@link OnConnectionFailedListener}.
	     */
	    @Override
	    public void onConnectionFailed(ConnectionResult result) {
	        // Do nothing
	    }

	    @Override
	    public boolean onMyLocationButtonClick() {
	        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
	        // Return false so that we don't consume the event and the default behavior still occurs
	        // (the camera animates to the user's current position).
	        return false;
	    }
	    
		private class MarkStoreTask extends AsyncTask<String, Mark, Void> {
			
			@Override
			protected Void doInBackground(String ... params) {
				ContentResolver resolver = LocationActivity.this.getContentResolver();
				Cursor c = resolver.query(StoreContract.CONTENT_URI, null, "Stores.type = 1", null, null);
				c.moveToFirst();
				while(!c.isAfterLast())
				{
					double latitude = StoreContract.getLatitude(c);
					double longitude =  StoreContract.getLongitude(c);
					String name = StoreContract.getName(c);
					String address = StoreContract.getAddress(c);
					
					Mark mark = new Mark(latitude,longitude, name, address);
					publishProgress(mark);
					c.moveToNext();
				}
				c.close();
				return null;
			}
			
			protected void onProgressUpdate(Mark ... progress) {
				Mark mark = progress[0];
				addmark(mark);
			}

			public void addmark(Mark mark) {
				map.addMarker(
						new MarkerOptions()
						.position(new LatLng(mark.latitude,mark.longitude))
						.title(mark.name)
						.snippet(mark.address)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}
		}
	}
