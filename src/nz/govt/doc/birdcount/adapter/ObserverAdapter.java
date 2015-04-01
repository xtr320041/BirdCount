

package nz.govt.doc.birdcount.adapter;

import java.util.List;

import nz.govt.doc.birdcount.R;
import nz.govt.doc.birdcount.R.id;
import nz.govt.doc.birdcount.R.layout;
import nz.govt.doc.birdcount.domain.Observer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
public class ObserverAdapter extends ArrayAdapter<Observer> {
 
	private final Activity context;
	private final List<Observer> observerList;

//	public BirdCountListAdapter(Context context, int textViewResourceId) {
//	    super(context, textViewResourceId);
//	}
//
//	public BirdCountListAdapter(Context context, int resource, List<BirdLineItem> items) {
//	    super(context, resource, items);
//	}	
	
	public ObserverAdapter(Activity context, List<Observer> observerList) {
		super(context, R.layout.observeritem, observerList);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.observerList = observerList;
	}

	@Override
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.observeritem,null,true);
		
		String observerName = observerList.get(position).getTextItem();
		TextView txtName = (TextView) rowView.findViewById(R.id.observerName);
		txtName.setText(observerName);

		return rowView;
	};
}