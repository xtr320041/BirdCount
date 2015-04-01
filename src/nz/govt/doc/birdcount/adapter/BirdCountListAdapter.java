package nz.govt.doc.birdcount.adapter;

import java.util.List;

import nz.govt.doc.birdcount.R;
import nz.govt.doc.birdcount.R.id;
import nz.govt.doc.birdcount.R.layout;
import nz.govt.doc.birdcount.domain.BirdLineItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class BirdCountListAdapter extends ArrayAdapter<BirdLineItem> {
 
	private final Activity context;
	private final List<BirdLineItem> birdList;

//	public BirdCountListAdapter(Context context, int textViewResourceId) {
//	    super(context, textViewResourceId);
//	}
//
//	public BirdCountListAdapter(Context context, int resource, List<BirdLineItem> items) {
//	    super(context, resource, items);
//	}	
	
	public BirdCountListAdapter(Activity context, List<BirdLineItem> birdList) {
		super(context, R.layout.birdline, birdList);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.birdList = birdList;
	}

	@Override
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.birdline,null,true);
		
		BirdLineItem bird = birdList.get(position);
		TextView txtName = (TextView) rowView.findViewById(R.id.birdName);

		txtName.setText(bird.getDisplayBird());

		return rowView;
	};
}