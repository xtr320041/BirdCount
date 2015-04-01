package nz.govt.doc.birdcount;

import java.util.ArrayList;
import java.util.List;

import nz.govt.doc.birdcount.adapter.BirdCountAdapter;
import nz.govt.doc.birdcount.adapter.StationAdapter;
import nz.govt.doc.birdcount.domain.BirdCountEntity;
import nz.govt.doc.birdcount.domain.Station;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AdminBirdCount extends ActionBarActivity {
    AlertDialog.Builder confirmDelete;
    AlertDialog.Builder confirmFinish;
    int toDelete;
    String confirmDeleteMsg = "Are you sure to delete:";
    
	List<String> birdCountList = new ArrayList<String>();
	BirdCountAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_bird_count);
		List<BirdCountEntity> bce = ((BirdCountApp)this.getApplication()).readBirdCountData();
		
		birdCountList = BirdCountEntity.getBirdCountList(bce);
		adapter = new BirdCountAdapter(this, birdCountList);
		
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
            	confirmDelete.setMessage(confirmDeleteMsg + "\n" + birdCountList.get(position).toString() + " ?");
            	confirmDelete.show();   
                
            }
        });	  				
		
	}
	
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
            	birdCountList.remove(toDelete);
    			adapter.notifyDataSetChanged();
            	break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
            }
        }
    };		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_bird_count, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.uplaod_all) {
			//((BirdCountApp)this.getApplication()).uploadAll();
	        Intent i = new Intent(AdminBirdCount.this, DataUpload.class);
	        startActivity(i);	
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
