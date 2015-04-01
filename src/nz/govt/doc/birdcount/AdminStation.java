package nz.govt.doc.birdcount;

import java.util.ArrayList;
import java.util.List;

import nz.govt.doc.birdcount.adapter.ObserverAdapter;
import nz.govt.doc.birdcount.adapter.StationAdapter;
import nz.govt.doc.birdcount.domain.Station;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class AdminStation extends ActionBarActivity {
    AlertDialog.Builder confirmDelete;
    AlertDialog.Builder confirmFinish;
    int toDelete;
    String confirmDeleteMsg = "Are you sure to delete:";
    
	List<Station> stationList = new ArrayList<Station>();
	StationAdapter adapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_simple_list);
		
		stationList = ((BirdCountApp)this.getApplication()).getStationList();
//		stationList.add(new Station("Test 123", "A1", -12345, 56789));
//		stationList.add(new Station("Test 345", "B1", -12345, 56789));
		adapter = new StationAdapter(this, stationList);
		
		ListView listV = (ListView) findViewById(R.id.ItemList);  
		listV.setAdapter(adapter);

	
		confirmDelete = new AlertDialog.Builder(this);
		confirmDelete.setPositiveButton("Yes", dialogClickListener);
		confirmDelete.setNegativeButton("No", dialogClickListener);		
		
		listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	toDelete = position;
            	confirmDelete.setMessage(confirmDeleteMsg + "\n" + stationList.get(position).getTextItem() + " ?");
            	confirmDelete.show();   
                
            }
        });	  				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_station, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
            	stationList.remove(toDelete);
    			adapter.notifyDataSetChanged();
            	break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
            }
        }
    };	
    
    public void addNew(View v){
        LayoutInflater inflater = this.getLayoutInflater();
        final View stationView = inflater.inflate(R.layout.station_add_new, null);
    	
    	new AlertDialog.Builder(AdminStation.this)
        .setTitle("New Bird Count Station")
        .setMessage("Please details below:")
        .setView(stationView)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	String shortName = ((EditText)stationView.findViewById(R.id.stationShortName)).getText().toString();
            	String name = ((EditText)stationView.findViewById(R.id.stationName)).getText().toString();
            	String latitudeStr = ((EditText)stationView.findViewById(R.id.stationLatitude)).getText().toString();
            	String longitudeStr = ((EditText)stationView.findViewById(R.id.stationLongitude)).getText().toString();
            	double latitude = 0.0;
            	double longitude = 0.0;
            	try{latitude = Double.parseDouble(latitudeStr);}
            	catch(Exception e){}
            	try{longitude = Double.parseDouble(longitudeStr);}
            	catch(Exception e){}
            	stationList.add(new Station(name, shortName, latitude, longitude));
    			adapter.notifyDataSetChanged();            	
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();    	
    }
    
    public void saveAndExit(View v){
		((BirdCountApp)this.getApplication()).setStationList(stationList);
		((BirdCountApp)this.getApplication()).saveStationList();	
		finish();  	
    }     
}
