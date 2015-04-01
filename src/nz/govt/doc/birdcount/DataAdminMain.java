package nz.govt.doc.birdcount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DataAdminMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_admin_main);
	}
	
	public void gotoAdminObserver(View v) {
        Intent i = new Intent(DataAdminMain.this, AdminObserver.class);
        startActivity(i);		
	}	
	
	public void gotoAdminStation(View v) {
        Intent i = new Intent(DataAdminMain.this, AdminStation.class);
        startActivity(i);		
	}	
	
	public void gotoAdminBirdcount(View v) {
        Intent i = new Intent(DataAdminMain.this, AdminBirdCount.class);
        startActivity(i);		
	}	

}
