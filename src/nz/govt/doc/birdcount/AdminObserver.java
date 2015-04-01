package nz.govt.doc.birdcount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nz.govt.doc.birdcount.adapter.ObserverAdapter;
import nz.govt.doc.birdcount.domain.Observer;
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



public class AdminObserver extends ActionBarActivity {

    AlertDialog.Builder confirmDeleteBird;
    AlertDialog.Builder confirmFinish;
    int observerToDelete;
    String confirmDelete = "Are you sure to delete:";
    
	List<Observer> observerList = new ArrayList<Observer>();
	ObserverAdapter adapter;
	String newObserver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_observer);
		
		observerList = ((BirdCountApp)this.getApplication()).getObserverList();
		adapter = new ObserverAdapter(this, observerList);
		
		ListView observerListV = (ListView) findViewById(R.id.observerList);  
		observerListV.setAdapter(adapter);

	
		confirmDeleteBird = new AlertDialog.Builder(this);
		confirmDeleteBird.setPositiveButton("Yes", dialogClickListener);
		confirmDeleteBird.setNegativeButton("No", dialogClickListener);		
		
		observerListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	observerToDelete = position;
            	confirmDeleteBird.setMessage(confirmDelete + "\n" + observerList.get(position).getTextItem() + " ?");
            	confirmDeleteBird.show();   
                
            }
        });	  		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_observer, menu);		      
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
            	observerList.remove(observerToDelete);
    			adapter.notifyDataSetChanged();
            	break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
            }
        }
    };	
    
    public void addNewObserver(View v){
        LayoutInflater inflater = this.getLayoutInflater();
        final View stationView = inflater.inflate(R.layout.observer_add_new, null);
    	
    	new AlertDialog.Builder(AdminObserver.this)
        .setTitle("New Observer")
        .setMessage("Please details below:")
        .setView(stationView)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	String name = ((EditText)stationView.findViewById(R.id.observerName)).getText().toString();
            	String email = ((EditText)stationView.findViewById(R.id.observerEmail)).getText().toString();
            	observerList.add(new Observer(name, email));
    			adapter.notifyDataSetChanged();            	
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();      	
    	
    }
    
    public void saveObserverList(View v){
		((BirdCountApp)this.getApplication()).setObserverList(observerList);
		((BirdCountApp)this.getApplication()).saveObserverList();	
		finish();  	
    }    
}
