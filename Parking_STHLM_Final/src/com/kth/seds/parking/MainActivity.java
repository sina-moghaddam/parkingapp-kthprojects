package com.kth.seds.parking;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends Activity implements OnQueryTextListener {
	private class MyTabListener implements ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mFragName;
		private boolean start;

		public MyTabListener(Activity activity, String fragName) {
			mActivity = activity;
			mFragName = fragName;
			start = true;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (start) {
				mFragment = Fragment.instantiate(mActivity, mFragName);
				ft.add(R.id.main, mFragment);
				start = false;
			} else {
				ft.show(mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.hide(mFragment);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ActionBar ab = getActionBar();
		setTabNavigation(ab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.search_view, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setOnQueryTextListener(this);
		return true;
	}

	private void setTabNavigation(ActionBar actionBar) {
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle(R.string.app_name);

		Tab tab = actionBar
				.newTab()
				.setText(R.string.map_view)
				.setTabListener(
						new MyTabListener(this, ParkingMapView.class.getName()));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.list_view)
				.setTabListener(
						new MyTabListener(this, ParkingListView.class.getName()));
		actionBar.addTab(tab);
		tab = actionBar
				.newTab()
				.setText(R.string.favorite_view)
				.setTabListener(
						new MyTabListener(this, FavoriteView.class.getName()));
		actionBar.addTab(tab);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Address address;
		try {
			Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);
			List<Address> results = geocoder.getFromLocationName(query, 1);

			if (results.size() == 0) {
				return false;
			}

			address = results.get(0);
			
			// Now do something with this GeoPoint:
//			GeoPoint p = new GeoPoint((int) (address.getLatitude() * 1E6),
//					(int) (address.getLongitude() * 1E6));
			ParkingMapView.searchMap(address.getLatitude(), address.getLongitude());
		} catch (Exception e) {
			Log.e("", "Something went wrong: ", e);
			return false;
		}
		return true;
	}

}
