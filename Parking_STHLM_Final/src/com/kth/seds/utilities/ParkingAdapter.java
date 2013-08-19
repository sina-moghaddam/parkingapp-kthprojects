package com.kth.seds.utilities;

import com.kth.seds.database.ParkingDataSource;
import com.kth.seds.entity.Parking;
import com.kth.seds.parking.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ParkingAdapter extends CursorAdapter {

	private final LayoutInflater mInflater;
	private ParkingDataSource parkingDAO;

	public ParkingAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		mInflater = LayoutInflater.from(context);
		parkingDAO = new ParkingDataSource(context);
		parkingDAO.open();
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
		Parking parkingTemp = new Parking();
		parkingTemp.setId(c.getString(c
				.getColumnIndex(ParkingDataSource.COLUMN_ID)));
		parkingTemp.setName(c.getString(c
				.getColumnIndex(ParkingDataSource.COLUMN_NAME)));
		parkingTemp.setAddress(c.getString(c
				.getColumnIndex(ParkingDataSource.COLUMN_ADDRESS)));
		parkingTemp.setAvailableSpace(c.getInt(c
				.getColumnIndex(ParkingDataSource.COLUMN_AVAILABLE_SPACE)));
		parkingTemp.setPrice(c.getDouble(c
				.getColumnIndex(ParkingDataSource.COLUMN_PRICE)));

		TextView nameTextView = (TextView) view.findViewById(R.id.ParkingName);
		nameTextView.setText(parkingTemp.getName());

		TextView addressTextView = (TextView) view
				.findViewById(R.id.ParkingAddress);
		addressTextView.setText(parkingTemp.getAddress());

		TextView capacityTextView = (TextView) view
				.findViewById(R.id.ParkingCapacity);
		capacityTextView.setText(String.valueOf(parkingTemp.getAvailableSpace()));
		if(parkingTemp.getAvailableSpace()==0){
			capacityTextView.setBackgroundResource(R.drawable.no_space);
		}else{
			capacityTextView.setBackgroundResource(R.drawable.free_space);
		}

		TextView priceTextView = (TextView) view
				.findViewById(R.id.ParkingPrice);
		priceTextView.setText(String.valueOf(parkingTemp.getPrice()) + " SEK");

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		final View view = mInflater.inflate(R.layout.parking_list, arg2, false);
		return view;
	}

}
