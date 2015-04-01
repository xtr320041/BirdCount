package nz.govt.doc.birdcount.adapter;

import java.util.List;

import nz.govt.doc.birdcount.R;
import nz.govt.doc.birdcount.R.id;
import nz.govt.doc.birdcount.R.layout;
import nz.govt.doc.birdcount.domain.Station;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class StationAdapter extends ArrayAdapter<Station> {
 
	private final Activity context;
	private final List<Station> stationList;


	
	public StationAdapter(Activity context, List<Station> stationList) {
		super(context, R.layout.simple_text_item, stationList);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.stationList = stationList;
	}

	@Override
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.simple_text_item,null,true);
		
		String itemText = stationList.get(position).getTextItem();
		TextView txtName = (TextView) rowView.findViewById(R.id.ItemText);
		txtName.setText(itemText);

		return rowView;
	};
}