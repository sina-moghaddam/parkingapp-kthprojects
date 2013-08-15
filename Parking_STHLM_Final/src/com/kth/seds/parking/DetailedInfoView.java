package com.kth.seds.parking;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.kth.seds.database.ParkingDataSource;
import com.kth.seds.entity.Parking;

public class DetailedInfoView extends Activity {

	private static final String SHARED_FILE_NAME = "shared.png";
	private static final String ACTION_BAR_TITLE = "Parking Inforamtion";
	private static final String PRICE_TITLE = "Price: ";
	private static final String TOTAL_SPACES_TITLE = "Total Spaces: ";
	private static final String OPENING_HOURS_TITLE = "Opening Hours: ";
	private boolean favorite;
	private ParkingDataSource parkingDAO;
	private Parking parking;
	private Menu menuApp;
	private ImageView getDirectionButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		copyPrivateRawResuorceToPubliclyAccessibleFile();

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(ACTION_BAR_TITLE);
		actionBar.setIcon(R.drawable.list_icon);

		setContentView(R.layout.activity_detailed_info);
		getDirectionButton = (ImageView)findViewById(R.id.imageView2);
		getDirectionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDirectionOnclick();
			}
		});
		parkingDAO = new ParkingDataSource(getApplicationContext());
		Intent myIntent = getIntent();
		parking = (Parking) myIntent
				.getSerializableExtra("ParkingObject");

		TextView parkingName = (TextView) findViewById(R.id.parkingName);
		parkingName.setText(parking.getName());

		TextView parkingAddress = (TextView) findViewById(R.id.parkingAddress);
		parkingAddress.setText(parking.getAddress());

		TextView capacityTextView = (TextView) findViewById(R.id.availableSpace);
		capacityTextView.setText(String.valueOf(parking.getAvailableSpace()));
		if (parking.getAvailableSpace() == 0) {
			capacityTextView.setBackgroundResource(R.drawable.no_space);
		} else {
			capacityTextView.setBackgroundResource(R.drawable.free_space);
		}

		TextView parkingPrice = (TextView) findViewById(R.id.Price);
		parkingPrice.setText(PRICE_TITLE + parking.getPrice());

		TextView totalSpaces = (TextView) findViewById(R.id.totalSpaces);
		totalSpaces.setText(TOTAL_SPACES_TITLE + parking.getTotalSpace());

		TextView openingHour = (TextView) findViewById(R.id.OpeningHour);
		openingHour.setText(OPENING_HOURS_TITLE + parking.getOpeningHours());

		TextView description = (TextView) findViewById(R.id.moreInfoDescription);
		description.setText(parking.getDescription());
		favorite = parking.isFavorite();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ContentValues values = new ContentValues();
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.unfavorite_action:	
			values.put(ParkingDataSource.COLUMN_FAVORITE, 1);
			parkingDAO.updateRecord(parking.getId(), values);
			item.setVisible(false);
			menuApp.findItem(R.id.favorite_action).setVisible(true);
			return true;
		case R.id.favorite_action:
			values.put(ParkingDataSource.COLUMN_FAVORITE, 0);
			parkingDAO.updateRecord(parking.getId(), values);
			item.setVisible(false);
			menuApp.findItem(R.id.unfavorite_action).setVisible(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem favItem = menu.findItem(R.id.favorite_action);
		MenuItem unFavItem = menu.findItem(R.id.unfavorite_action);
		if(favorite){
			favItem.setVisible(true);
			unFavItem.setVisible(false);
		}else{
			favItem.setVisible(false);
			unFavItem.setVisible(true);
		}
		return true;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_detailed_info, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider.setShareIntent(createShareIntent());
		menuApp = menu;
		return true;
	}

	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		Uri uri = Uri.fromFile(getFileStreamPath(SHARED_FILE_NAME));
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	private void copyPrivateRawResuorceToPubliclyAccessibleFile() {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = getResources().openRawResource(R.drawable.free1);
			outputStream = openFileOutput(SHARED_FILE_NAME,
					Context.MODE_WORLD_READABLE | Context.MODE_APPEND);
			byte[] buffer = new byte[1024];
			int length = 0;
			try {
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
			} catch (IOException ioe) {
				/* ignore */
			}
		} catch (FileNotFoundException fnfe) {
			/* ignore */
		} finally {
			try {
				inputStream.close();
			} catch (IOException ioe) {
				/* ignore */
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
				/* ignore */
			}
		}

	}

	public void getDirectionOnclick(){
		Location location = ParkingMapView.getLocation();
		final Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?" + "saddr="
						+ parking.getLatitude() + "," + ""
						+ parking.getLongitude() + "&daddr="
						+ location.getLatitude() + "," + ""
						+ location.getLongitude()));
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		startActivity(intent);

	}

}
