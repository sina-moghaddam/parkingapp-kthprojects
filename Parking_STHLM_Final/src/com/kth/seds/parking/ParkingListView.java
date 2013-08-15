package com.kth.seds.parking;

import com.kth.seds.database.ParkingDataSource;
import com.kth.seds.entity.Parking;
import com.kth.seds.utilities.ParkingAdapter;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ParkingListView extends Fragment {
	private Cursor parkingListCursor;
	private ParkingAdapter adapter;

	private ParkingDataSource parkingDAO;
	private ImageView sort;
	private ListView parkingList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_frag, container, false);
		parkingList = (ListView)view.findViewById(R.id.parkingListView);
		parkingList.setOnItemClickListener(onParkingClick);
		sort = (ImageView) view.findViewById(R.id.sort);
		return view;

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.sort_price:	
			parkingListCursor = parkingDAO.queryAllCursor(ParkingDataSource.SORT_BY_PRICE);
			getActivity().startManagingCursor(parkingListCursor);

			if (parkingListCursor != null && parkingListCursor.getCount() != 0) {
				adapter = new ParkingAdapter(getActivity().getApplicationContext(),
						parkingListCursor,true);
				
				parkingList.setAdapter(adapter);
				
			} else {

			}
			Toast.makeText(getActivity(), "List Sorted By Price", Toast.LENGTH_LONG).show();

			return true;
		case R.id.sort_space:
			parkingListCursor = parkingDAO.queryAllCursor(ParkingDataSource.SORT_BY_SPACE);
			getActivity().startManagingCursor(parkingListCursor);

			if (parkingListCursor != null && parkingListCursor.getCount() != 0) {
				adapter = new ParkingAdapter(getActivity().getApplicationContext(),
						parkingListCursor,true);
				
				parkingList.setAdapter(adapter);
				
			} else {

			}
			Toast.makeText(getActivity(), "List Sorted By Space", Toast.LENGTH_LONG).show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		View header = (View)getActivity().getLayoutInflater().inflate(R.layout.list_header,null);
		
		parkingList.addHeaderView(header);
		parkingDAO = new ParkingDataSource(getActivity()
				.getApplicationContext());
		parkingDAO.open();

		parkingListCursor = parkingDAO.queryAllCursor(ParkingDataSource.NO_SORT);
		getActivity().startManagingCursor(parkingListCursor);

		if (parkingListCursor != null && parkingListCursor.getCount() != 0) {
			adapter = new ParkingAdapter(getActivity().getApplicationContext(),
					parkingListCursor,true);
			
			parkingList.setAdapter(adapter);
		} else {

		}
		
		

		sort.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				parkingListCursor = parkingDAO.queryAllCursor(ParkingDataSource.SORT_BY_SPACE);
				getActivity().startManagingCursor(parkingListCursor);

				if (parkingListCursor != null && parkingListCursor.getCount() != 0) {
					adapter = new ParkingAdapter(getActivity().getApplicationContext(),
							parkingListCursor,true);
					
					parkingList.setAdapter(adapter);
					
				} else {

				}
			}
		});

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu,inflater);
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.sort, menu);
		
	}
	
	private OnItemClickListener onParkingClick = new OnItemClickListener() {
		@Override
		public void onItemClick(android.widget.AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent myIntent = new Intent();
			myIntent.setClass(getActivity().getApplicationContext(), DetailedInfoView.class);
			
			parkingListCursor.moveToPosition(arg2-1);
			Parking parking = new Parking(
					parkingListCursor.getString(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_ID)),
					parkingListCursor.getString(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_NAME)),
					parkingListCursor.getString(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_ADDRESS)),
					parkingListCursor.getDouble(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_LATITUDE)),
					parkingListCursor.getDouble(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_LONGITUDE)),
					parkingListCursor.getInt(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_TOTAL_SPACE)),
					parkingListCursor.getInt(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_AVAILABLE_SPACE)),
					parkingListCursor.getString(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_OPENING_HOURS)),
					parkingListCursor.getDouble(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_PRICE)),
					parkingListCursor.getString(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_DESCRIPTION)),false);
			if(parkingListCursor.getInt(parkingListCursor.getColumnIndex(ParkingDataSource.COLUMN_FAVORITE))==1){
				parking.setFavorite(true);
			}else{
				parking.setFavorite(false);
			}
			myIntent.putExtra("ParkingObject", parking);  
			startActivity(myIntent);
		};
	};
	
	}