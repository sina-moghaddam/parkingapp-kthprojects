package com.kth.seds.parking;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kth.seds.database.ParkingDataSource;
import com.kth.seds.entity.Parking;
import com.kth.seds.utilities.ParkingAdapter;

public class FavoriteView extends ListFragment{
	private Cursor parkingListCursor;
	private ParkingAdapter adapter;
	private ParkingDataSource parkingDAO;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		View header = (View)getActivity().getLayoutInflater().inflate(R.layout.list_header,null);
		getListView().addHeaderView(header);
		parkingDAO = new ParkingDataSource(getActivity()
				.getApplicationContext());
		parkingDAO.open();
		
		parkingListCursor = parkingDAO.queryCursor("1", ParkingDataSource.COLUMN_FAVORITE);
		
		if (parkingListCursor != null && parkingListCursor.getCount() != 0) {
			getActivity().startManagingCursor(parkingListCursor);
			adapter = new ParkingAdapter(getActivity().getApplicationContext(),
					parkingListCursor,true);
			setListAdapter(adapter);
		} else {
			setListAdapter(null);
		}

	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent myIntent = new Intent();
		myIntent.setClass(getActivity().getApplicationContext(), DetailedInfoView.class);
		
		parkingListCursor.moveToPosition(position-1);
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

	}
	
}
