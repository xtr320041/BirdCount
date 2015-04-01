package nz.govt.doc.birdcount;

import java.text.SimpleDateFormat;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.CheckBox;
import java.util.ArrayList;
import java.util.List;

import nz.govt.doc.birdcount.domain.BirdCountEntity;
import nz.govt.doc.birdcount.domain.Observer;
import nz.govt.doc.birdcount.domain.Station;
import android.widget.ArrayAdapter;

public class BirdCountHeader extends ActionBarActivity {
	Handler mHandler;
	
	private String getCurrentDate(){
		long date = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss a");
		return sdf.format(date); 		
	}
	

	private String getStation(int id){
		int radioId = ((RadioGroup)findViewById(R.id.Station)).getCheckedRadioButtonId();
		return ((RadioButton)findViewById(radioId)).getText().toString();		
	}
	
	private String getRadioChecked(int id){
		int radioId = ((RadioGroup)findViewById(id)).getCheckedRadioButtonId();
		return ((RadioButton)findViewById(radioId)).getText().toString();		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bird_count_header);
		
		TextView tvDisplayDate = (TextView) findViewById(R.id.currentDateTime);
		tvDisplayDate.setText(getCurrentDate());
		
		addItemsOnCountStation();
		addItemsOnObserver();
		addItemsOnTemperature();
		addItemsOnPrecipitation1();
		addItemsOnPrecipitation2();
		addItemsOnWind();
		addItemsOnOtherNoise();
		
	    mHandler = new Handler();
	    mHandler.postDelayed(mRunnable, 5000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bird_count_header, menu);
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
	
	public void startCounting(View v)
    {
		BirdCountEntity bce = new BirdCountEntity("");
		bce.setDate(getCurrentDate());
		bce.setLocation(((Spinner)findViewById(R.id.stationSpinner)).getSelectedItem().toString());
		bce.setStation(getRadioChecked(R.id.Station));
		bce.setObserver(((Spinner)findViewById(R.id.observerSpinner)).getSelectedItem().toString());
		bce.setSun(getRadioChecked(R.id.sun));		
		bce.setTemperature(((Spinner)findViewById(R.id.temperatureSpinner)).getSelectedItem().toString());
		bce.setPrecipitation1(((Spinner)findViewById(R.id.precipitation1Spinner)).getSelectedItem().toString());
		bce.setPrecipitation2(((Spinner)findViewById(R.id.precipitation2Spinner)).getSelectedItem().toString());
		bce.setWind(((Spinner)findViewById(R.id.windSpinner)).getSelectedItem().toString());
		bce.setOtherNoise(((Spinner)findViewById(R.id.otherNoiseSpinner)).getSelectedItem().toString());		
		bce.setNotes(((EditText)findViewById(R.id.notes)).getText().toString());		
		bce.setNotMeasured(((CheckBox)findViewById(R.id.notMeasured)).isChecked());
		bce.setReason(((EditText)findViewById(R.id.reason)).getText().toString());				
		((BirdCountApp) this.getApplication()).setBirdCountEntity(bce);
		
		Intent i = new Intent(BirdCountHeader.this, BirdCountDetail.class);
        startActivity(i);
    } 
	
	// add items into spinner dynamically
	public void addItemsOnCountStation() {	 		
		List<Station> list = ((BirdCountApp)this.getApplication()).getStationList();		
		bindSpinner(R.id.stationSpinner, Station.getSpinnerList(list));
	  }	
	
	public void addItemsOnObserver() {	 
		List<String> list = Observer.getSpinnerList(((BirdCountApp)this.getApplication()).getObserverList());
		bindSpinner(R.id.observerSpinner, list);
	  }	
	
	public void addItemsOnTemperature() {	 
		List<String> list = new ArrayList<String>();
		list.add("1  < 0 \u2103");
		list.add("2  0 - 5 \u2103");
		list.add("3  6 - 10 \u2103");
		list.add("4  11 - 15 \u2103");
		list.add("5  16 - 22 \u2103");
		list.add("6  > 22 \u2103");
		bindSpinner(R.id.temperatureSpinner, list);
	  }	
	
	public void addItemsOnPrecipitation1() {	 
		List<String> list = new ArrayList<String>();
		list.add("0  None");
		list.add("1  Dripping foilage");
		list.add("2  Drizzle");
		list.add("3  Light");
		list.add("4  Moderate");
		list.add("5  Heavy");
		bindSpinner(R.id.precipitation1Spinner, list);
	  }		
	
	public void addItemsOnPrecipitation2() {	 
			List<String> list = new ArrayList<String>();
			list.add("       ");
			list.add("M  Mist");
			list.add("R  Rain");
			list.add("H  Hail");
			list.add("S  Snow");
			bindSpinner(R.id.precipitation2Spinner, list);
	}	
	
	public void addItemsOnWind() {	 
		List<String> list = new ArrayList<String>();
		list.add("0  Leavs still or move without noise");
		list.add("1  Leaves rustle");
		list.add("2  Leaves and branchlets in constant motion");
		list.add("3  Branches or trees sway");
		bindSpinner(R.id.windSpinner, list);
	}	
	
	public void addItemsOnOtherNoise() {	 
		List<String> list = new ArrayList<String>();
		list.add("0  Not Important");
		list.add("1  Moderate");
		list.add("2  Loud");
		bindSpinner(R.id.otherNoiseSpinner, list);
	}	
		
	
	public void bindSpinner(int spinnerId, List<String> dataList) {	 
		Spinner spinner = (Spinner) findViewById(spinnerId);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner_text_item,dataList);
		//android.R.layout.simple_spinner_item,dataList);
		//foodadapter.setDropDownViewResource(R.layout.spinner_layout);
		spinner.setAdapter(dataAdapter);
	  }	
	

	  private Runnable mRunnable = new Runnable() {
	    @Override
	    public void run() {
	      /** Do something **/
			TextView tvDisplayDate = (TextView) findViewById(R.id.currentDateTime);
			tvDisplayDate.setText(getCurrentDate());	    	
	    	mHandler.postDelayed(mRunnable, 1000);
	    }
	  };
}
