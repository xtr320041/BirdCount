package nz.govt.doc.birdcount;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nz.govt.doc.birdcount.adapter.BirdCountListAdapter;
import nz.govt.doc.birdcount.adapter.BirdListAdapter;
import nz.govt.doc.birdcount.domain.BirdCountEntity;
import nz.govt.doc.birdcount.domain.BirdLineItem;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView;

@SuppressLint("NewApi")
public class BirdCountDetail extends ActionBarActivity {

    TextView ct;
    EditText speciesTxt;
    CountDownTimer cdt;
    List<BirdLineItem> birdList;
	BirdCountListAdapter birdCountAdapter;
    ListView birdCountListV;    
    AlertDialog.Builder confirmDeleteBird;
    AlertDialog.Builder confirmFinish;
    int birdToDelete;
    String confirmDelete = "Are you sure to delete:";
    private boolean isChecking = true;
    private int distanceCheckedId = R.id.distance0;
    private int sizeCheckedId = R.id.size1;
    private int precisionCheckedId = R.id.precisionA;
    
    
    ArrayAdapter<String> adapter;
    
    String[] birdname ={
    		"Silvereye",
    		"Paradise Duck",
    		"Rifleman",
    		"Blackbird",
    		"Grey Warbler",
    		"Bellbird",
    		"Chaffinch",
    		"Brown Creeper",
    		"Canada Goose",
    		"Kea",
    		"Black-fronted Tern",
    		"Pipit",
    		"Skylark",
    		"Yellowhammer",
    		"Greenfinch",
    		"California Quail",
    		"Unknown"
    		};
    Integer[] imgid={
    		R.drawable.bird00silvereye,
    		R.drawable.bird01paradiseduck,
    		R.drawable.bird02rifleman,
    		R.drawable.bird03blackbird,
    		R.drawable.bird04greywarbler,
    		R.drawable.bird05bellbird,
    		R.drawable.bird06chaffinch,
    		R.drawable.bird07browncreeper,
    		R.drawable.bird08canadagoose,
    		R.drawable.bird09kea,
    		R.drawable.bird10blackfrontedtern,
    		R.drawable.bird11pipit,
    		R.drawable.bird12skylark,
    		R.drawable.bird13yellowhammer,
    		R.drawable.bird14greenfinch,
    		R.drawable.bird15californiaquail,
    		R.drawable.bird99unknown
    		};


    private static final String FORMAT = "%02d:%02d:%02d";
    private static final long FIVEMINUTE = 300000;
    //private static final long FIVEMINUTE = 5000;

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
            	birdList.remove(birdToDelete);
    			birdCountAdapter.notifyDataSetChanged();
            	break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
            }
        }
    };

    
    DialogInterface.OnClickListener confirmCloseListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_NEUTRAL:
                //Yes button clicked
            	finish();
            	break;
            }
        }
    };    

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bird_count_detail);
		
		speciesTxt =(EditText)findViewById(R.id.speciesTxt);
		//birdCountList = (ListView)findViewById(R.id.birdCountList);
		ct =(TextView)findViewById(R.id.counterTimer);
		
    	ct.setText(getTimerTxt(FIVEMINUTE));  

		cdt = new CountDownTimer(FIVEMINUTE, 1000) { 
	        @Override
			@SuppressLint("NewApi")
			public void onTick(long millisUntilFinished) {
	        					ct.setText(getTimerTxt(millisUntilFinished));              
						 }
					
			@Override
			public void onFinish() {
						        ct.setText("Finished");
						        showFinish();
						 }
		};   		
		
	
		Button startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.requestFocus();
		
		birdList = new ArrayList<BirdLineItem>();		
		birdCountAdapter=new BirdCountListAdapter(this, birdList);
		birdCountListV = (ListView) findViewById(R.id.birdCountList);    
				
		birdCountListV.setAdapter(birdCountAdapter);		
		confirmDeleteBird = new AlertDialog.Builder(this);
		confirmDeleteBird.setPositiveButton("Yes", dialogClickListener);
		confirmDeleteBird.setNegativeButton("No", dialogClickListener);		
		
		birdCountListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            	birdToDelete = position;
            	confirmDeleteBird.setMessage(confirmDelete + "\n" + birdList.get(position).getDisplayBird() + " ?");
            	confirmDeleteBird.show();   
                
            	//Toast.makeText(BirdCountDetail.this, "You clicked at " +birdList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });	        
		
		final RadioGroup radioGroupA = (RadioGroup) findViewById(R.id.distanceA);        
		final RadioGroup radioGroupB = (RadioGroup) findViewById(R.id.distanceB);    
		final RadioGroup sizeGroup = (RadioGroup) findViewById(R.id.size);    
		final RadioGroup precisionGroup = (RadioGroup) findViewById(R.id.precision);    
		
	    radioGroupA.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            // checkedId is the RadioButton selected
				// find the radiobutton by returned id
	        	if (checkedId != -1 && isChecking){
	        		isChecking = false;
	        		radioGroupB.clearCheck();
	        		distanceCheckedId = checkedId;
	        	}
	        	isChecking = true;

	        }
	    });
	    
	        
	    radioGroupB.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            // checkedId is the RadioButton selected
				// find the radiobutton by returned id
	        	if (checkedId != -1 && isChecking){
	        		isChecking = false;
	        		radioGroupA.clearCheck();
	        		distanceCheckedId = checkedId;
	        	}
	        	isChecking = true;

	        }
	    });   
        
	    
	    sizeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	sizeCheckedId = checkedId;
	        }
	    }); 	    
	    
	    precisionGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	precisionCheckedId = checkedId;
	        }
	    }); 	
	    

	    speciesTxt.setOnKeyListener(new EditText.OnKeyListener() {          
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((KeyEvent.KEYCODE_DEL == keyCode) || (KeyEvent.KEYCODE_ENTER == keyCode)) { // match ENTER key            {
					SetSaveBtn();
		        }				
				return false;
			}
	       });
	    
	    SetSaveBtn();
	    ((Button)findViewById(R.id.saveBtn)).setEnabled(false); 
		((Button)findViewById(R.id.resetBtn)).setEnabled(false); 
	}
	
    private void ClearGroupA(){
    	RadioGroup radioGroupA = (RadioGroup) findViewById(R.id.distanceA);  
    	radioGroupA.clearCheck(); 
    }	

	private String getTimerTxt(long timeLong)
	{
		return ""+String.format(FORMAT,
                TimeUnit.MILLISECONDS.toHours(timeLong),
				TimeUnit.MILLISECONDS.toMinutes(timeLong) - TimeUnit.HOURS.toMinutes(
				                    TimeUnit.MILLISECONDS.toHours(timeLong)),
				  TimeUnit.MILLISECONDS.toSeconds(timeLong) - TimeUnit.MINUTES.toSeconds(
				                      TimeUnit.MILLISECONDS.toMinutes(timeLong)));
	}
	

	
	public void startBird(View v)
    {
		cdt.start();
		((Button)findViewById(R.id.saveBtn)).setEnabled(true); 
		((Button)findViewById(R.id.resetBtn)).setEnabled(true); 
		((Button)findViewById(R.id.startBtn)).setEnabled(false); 
    }	
	
	public void resetBird(View v)
    {
		cdt.cancel();
		ct.setText(getTimerTxt(FIVEMINUTE)); 
		speciesTxt.setText("");
		((EditText) findViewById(R.id.comment)).setText("");		
		SetSaveBtn();
		birdList.clear();
		birdCountAdapter.notifyDataSetChanged();
		((Button)findViewById(R.id.saveBtn)).setEnabled(false); 
		((Button)findViewById(R.id.resetBtn)).setEnabled(false); 
		((Button)findViewById(R.id.startBtn)).setEnabled(true); 
    }
	
	public void resetBirdName(View v)
    {
		speciesTxt.setText("");
		SetSaveBtn();
    }	
	
	public void setBird(View v)
    {
		speciesTxt.setText((String)v.getTag());
//		Toast toast = Toast.makeText(getApplicationContext(), "Selected -> " + (String)v.getTag(), Toast.LENGTH_SHORT);
//        toast.show();
    }	
	
	public void saveBird(View v)
    {
		
		final Button saveBtn = (Button)findViewById(R.id.saveBtn);
		if (speciesTxt.getText().length() > 0){
			//Toast toast = Toast.makeText(getApplicationContext(), "Saving... " + speciesTxt.getText(), Toast.LENGTH_SHORT);
			//toast.show();		

			//Find a bird...
			String newBird = speciesTxt.getText().toString();			
			String newSize = ((RadioButton) findViewById(sizeCheckedId)).getText().toString();
			String newDistance = ((RadioButton) findViewById(distanceCheckedId)).getText().toString();
			String newPrecision = ((RadioButton) findViewById(precisionCheckedId)).getText().toString().substring(0, 1);
			String newComment = ((EditText) findViewById(R.id.comment)).getText().toString();
			BirdLineItem b = new BirdLineItem(newBird, newDistance, newSize, newPrecision, newComment);
			birdList.add(b);				
			birdCountAdapter.notifyDataSetChanged();
			speciesTxt.setText("");
			((EditText) findViewById(R.id.comment)).setText("");
			SetSaveBtn();
		}
		else
		{
	        final Dialog d = new Dialog(this);
	        d.setTitle("Select Bird Species");
	        d.setContentView(R.layout.popup_bird_picker);
			BirdListAdapter adapter=new BirdListAdapter(this, birdname, imgid);
	        ListView birdPopupList = (ListView) d.findViewById(R.id.birdlist);

	        birdPopupList.setAdapter(adapter);
	        birdPopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                	speciesTxt.setText(birdname[+ position]);
                	d.dismiss();
                	SetSaveBtn();               
                }
            });	        
	        d.show();				
		}
		
    }
	
	public void SetBirdCountList(){
		birdCountAdapter.clear();
		birdCountAdapter.addAll(birdList);
		birdCountAdapter.notifyDataSetChanged();
	}
	
	public void SetSaveBtn()
	{
		Button saveBtn = (Button)findViewById(R.id.saveBtn);
		if (speciesTxt.getText().length() > 0){
			saveBtn.setText("Save this record ...");
		}
		else
			saveBtn.setText("Name that bird ...");		
	}
	
	public void showFinish()
	{
		confirmFinish = new AlertDialog.Builder(this);
		confirmFinish.setTitle("FINISH! ");
		confirmFinish.setNeutralButton("OK", null);	
		confirmFinish.setCancelable(false);
		confirmFinish.setMessage("You can now save the data to the file and close. \n\nAny bird counted after this should not be added to the list.");
		confirmFinish.show();  	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bird_count_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.saveToFile) {
			BirdCountEntity bce = ((BirdCountApp)this.getApplication()).getBirdCountEntity();
			bce.setBirds(birdList);
			((BirdCountApp)this.getApplication()).saveBirdCountData();						
			//return true;
			confirmFinish = new AlertDialog.Builder(this);
			confirmFinish.setTitle("FINISH! ");
			confirmFinish.setNeutralButton("OK", confirmCloseListener);	
			confirmFinish.setCancelable(false);
			confirmFinish.setMessage("Bird count data have been saved to file successfully.\n\nYou can start a new bird count again.");
			confirmFinish.show(); 	
		}
		return super.onOptionsItemSelected(item);
	}	
	
	
}
