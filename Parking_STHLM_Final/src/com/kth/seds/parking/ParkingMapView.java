package com.kth.seds.parking;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kth.seds.database.ParkingDataSource;
import com.kth.seds.entity.Parking;
import com.kth.seds.utilities.Communication;

public class ParkingMapView extends Fragment implements OnMarkerClickListener,
OnInfoWindowClickListener {

	private static final LatLng STOCKHOLM_CENTRAL = new LatLng(59.330993, 18.059471);
	private boolean start = true; 
	private LatLng latLgn;
	private static GoogleMap map;
	private MapFragment mapFrag;
	private UiSettings mUiSettings;
	ArrayList<Parking> parkingInfo;
	ParkingDataSource parkingDS;
	Marker nearGalleria;
	Marker galleria;
	static Marker marker;
	private ProgressDialog progressDialog;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_frag, container, false);
		return view;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mapFrag = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map));

		map = mapFrag.getMap();

		mUiSettings = map.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);

		View mv = getActivity().getLayoutInflater().inflate(
				R.layout.custom_marker, null);
		map.setInfoWindowAdapter(new CustomInfoWindowAdapter(mv));
		map.setOnInfoWindowClickListener(this);
		// map.getMyLocation();
		map.setMyLocationEnabled(true);

		parkingDS = new ParkingDataSource(getActivity().getApplicationContext());
		parkingDS.open();
		parkingInfo = parkingDS.getAllParking();
		for (Parking parking : parkingInfo) {
			latLgn = new LatLng(parking.getLatitude(), parking.getLongitude());
			if (parking.getAvailableSpace() == 0) {
				marker = map.addMarker(new MarkerOptions()
				.position(latLgn)
				.title(parking.getName())
				.snippet(
						"Available space:"
								+ Integer.toString(parking
										.getAvailableSpace()))
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.full)));
			} else {

				marker = map.addMarker(new MarkerOptions()
				.position(latLgn)
				.title(parking.getName())
				.snippet(
						"Available space:"
								+ Integer.toString(parking
										.getAvailableSpace()))
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.free1)));
			}

		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(STOCKHOLM_CENTRAL, 15));


		progressDialog = ProgressDialog
				.show(getActivity(), "Loading", "Getting Available Spaces");
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// Communication part is here! Due to lack of access to server, we removed
				// it and used local spaces in database so that you can run the app.
//				Communication connection = new Communication("http://130.229.164.160:8080/ParkingService/webresources/Parkings",null);
//				byte[] responseBody;
//				try {
//					responseBody = connection.connectToServer();
//					if(responseBody!=null && responseBody.length!=0){
//						JSONObject jObject = new JSONObject(new String(responseBody, "US-ASCII"));
//						
//					}
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				handlerMessage.sendEmptyMessageDelayed(0, 10000);
			}
		});
		t.start();
	}






	public static Location getLocation(){
		Location myLocation = map.getMyLocation();
		return myLocation;
	}
	@Override
	public void onInfoWindowClick(Marker arg0) {
		Location myLocation = map.getMyLocation();
		final Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?" + "saddr="
						+ myLocation.getLatitude() + "," + ""
						+ myLocation.getLongitude() + "&daddr="
						+ arg0.getPosition().latitude + "," + ""
						+ arg0.getPosition().longitude));
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		startActivity(intent);

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {

		return false;
	}

	public static void searchMap(double latitude, double longitude){
		LatLng latLgn = new LatLng(latitude, longitude);
		marker = map.addMarker(new MarkerOptions()
		.position(latLgn));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLgn, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}

	/** Demonstrates customizing the info window and/or its contents. */
	class CustomInfoWindowAdapter implements InfoWindowAdapter {

		private final View mWindow;

		CustomInfoWindowAdapter(View view) {
			mWindow = view;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, mWindow);
			return mWindow;
		}

		// @Override
		public View getInfoContents(Marker marker) {
			return null;
		}

		private void render(Marker marker, View view) {

			((ImageView) view.findViewById(R.id.badge))
			.setImageResource(R.drawable.location_directions);

			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));

			if (title != null) {
				// Spannable string allows us to edit the formatting of the
				// text.
				SpannableString titleText = new SpannableString(title);
				titleUi.setText(titleText);
			} else {
				titleUi.setText("");
			}

			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}

	}

	/**
	 * Handler for Controlling progress bar on UI
	 **/
	private Handler handlerMessage = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			LatLng zoomPosition = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomPosition, 15));
			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
			start = false;
			progressDialog.dismiss();

		}
	};


}
