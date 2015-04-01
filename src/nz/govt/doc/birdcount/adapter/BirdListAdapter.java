package nz.govt.doc.birdcount.adapter;

import nz.govt.doc.birdcount.R;
import nz.govt.doc.birdcount.R.id;
import nz.govt.doc.birdcount.R.layout;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class BirdListAdapter extends ArrayAdapter<String> {
 
private final Activity context;
private final String[] itemname;
private final Integer[] imgid;
	public BirdListAdapter(Activity context, String[] itemname, Integer[] imgid) {
		super(context, R.layout.birditem, itemname);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.itemname=itemname;
		this.imgid=imgid;
	}
	
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.birditem, null,true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.birdName);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.birdIcon);
		txtTitle.setText(itemname[position]);
		imageView.setImageResource(imgid[position]);
		return rowView;
	}
	

}