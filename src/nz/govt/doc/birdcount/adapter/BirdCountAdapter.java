

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
 
public class BirdCountAdapter extends ArrayAdapter<String> {
 
	private final Activity context;
	private final List<String> birdCountList;

	public BirdCountAdapter(Activity context, List<String> birdCountList) {
		super(context, R.layout.simple_text_item, birdCountList);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.birdCountList = birdCountList;
	}
	

	@Override
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.simple_text_item, null, true);		
		String itemText = birdCountList.get(position).toString();
		TextView txtName = (TextView) rowView.findViewById(R.id.ItemText);
		txtName.setText(itemText);
		return rowView;
	};
}