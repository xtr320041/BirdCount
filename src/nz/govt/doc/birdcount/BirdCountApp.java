package nz.govt.doc.birdcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import nz.govt.doc.birdcount.domain.BirdCountEntity;
import nz.govt.doc.birdcount.domain.Observer;
import nz.govt.doc.birdcount.domain.Station;
import nz.govt.doc.birdcount.Mail;

import android.app.AlertDialog;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.EditText;
import android.widget.Toast;


public class BirdCountApp extends Application {
	private static final String APPDIR = "TIM";
	private static final String DATADIR = "data";
	private static final String CONFIGDIR = "config";
	private static final String MAPDIR = "maps";
	private static final String IMAGEDIR = "images";
	private static final String BIRDDATAFILE = "BirdCountData.txt";
	private static final String OBSERVERFILE = "observer.txt";
	private static final String STATIONFILE = "station.txt";
	private static final String BIRDFILE = "bird.txt";
	    
	
	private BirdCountEntity birdCountEntity;
	private List<Observer> observerList;
	private List<Station> stationList;

	public BirdCountEntity getBirdCountEntity() {
		return birdCountEntity;
	}

	public void setBirdCountEntity(BirdCountEntity birdCountEntity) {
		this.birdCountEntity = birdCountEntity;
	}

	public  void writeToFile(String fileDir, String fileName, String data, boolean append, String header)
    {
        FileOutputStream fos = null;
        boolean isNew = false;
        try {
        	StringBuilder sb = new StringBuilder();
        	
        	final File dir = new File(fileDir);                       
            if (!dir.exists())
            {
                dir.mkdirs(); 
            }
            final File myFile = new File(dir, fileName);

            if (!myFile.exists()) 
            {    
                myFile.createNewFile();
                isNew = true;
            } 
            
            if (!append)
                fos = new FileOutputStream(myFile);
            else
            {
            	fos = new FileOutputStream (new File(myFile.getAbsolutePath().toString()), true); 
            }
            
            if ((isNew)&&(header.length() > 0)){
            	sb.append(header);
            }
            sb.append(data);
            
            fos.write(sb.toString().getBytes());
            
            fos.close();
            
        } catch (IOException e) {
        	AlertDialog.Builder errDialog = new AlertDialog.Builder(this);
    		errDialog.setTitle("SYSTEM ERROR ");
    		errDialog.setNeutralButton("OK", null);	
    		errDialog.setCancelable(false);
    		errDialog.setMessage(e.getMessage());
    		errDialog.show();  	
        }
    }	
	
	public void saveBirdCountData(){
		String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + DATADIR;
		writeToFile(fileDir,BIRDDATAFILE,birdCountEntity.getDataAll(),true,BirdCountEntity.getHeaderAllStr());
	}
	
	public void saveObserverList(){
		StringBuilder sb = new StringBuilder();
		for(Observer observer:observerList){
			sb.append(observer.getFileText() + "\n");
		}
		String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + CONFIGDIR;
		writeToFile(fileDir,OBSERVERFILE,sb.toString(),false,"");
	}
	
	public void saveStationList(){
		StringBuilder sb = new StringBuilder();
		for(Station s:stationList){
			sb.append(s.getFileText() + "\n");
		}
		String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + CONFIGDIR;
		writeToFile(fileDir,STATIONFILE,sb.toString(),false,"");
	}
	
	public  void writeToFileOld()
    {
        FileOutputStream fos = null;
        boolean isNew = false;
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR );
            StringBuilder sb = new StringBuilder();
            
            if (!dir.exists())
            {
                dir.mkdirs(); 
            }
            final File myFile = new File(dir, BIRDDATAFILE);

            if (!myFile.exists()) 
            {    
                myFile.createNewFile();
                fos = new FileOutputStream(myFile);
                isNew = true;
            } 
            else
            {
            	//String filePath = myFile.getAbsolutePath();
            	//fos = openFileOutput(new File(filePath), MODE_APPEND);
            	fos = new FileOutputStream (new File(myFile.getAbsolutePath().toString()), true); 
            }
            
            if (isNew){
            	sb.append(BirdCountEntity.getHeaderAllStr());
            }
            sb.append(birdCountEntity.getDataAll());
            
            fos.write(sb.toString().getBytes());
            
            fos.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	public List<Observer> getObserverList() {
		observerList = readObserverData();
		return observerList;
	}

	public void setObserverList(List<Observer> observerList) {
		this.observerList = observerList;
	}
	
	
	
	public List<Observer> readObserverData(){
	    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + CONFIGDIR + "/" + OBSERVERFILE;
		List<Observer> data = new ArrayList<Observer>();
		try {
	    	FileInputStream is;
			BufferedReader reader;
			final File file = new File(filePath);
			if (file.exists()) {	
				is = new FileInputStream(file);				
			    reader = new BufferedReader(new InputStreamReader(is));
			    String line = reader.readLine();
			    while(line != null){
			    	if (line.length() > 1){
		            	data.add(new Observer(line));			    	
	            	}			    	
			    	line = reader.readLine();
			    }
			    reader.close();
			}
			return data;
			
	    } catch (Exception e) {
			// TODO Auto-generated catch block
        	AlertDialog.Builder errDialog = new AlertDialog.Builder(this);
    		errDialog.setTitle("SYSTEM ERROR ");
    		errDialog.setNeutralButton("OK", null);	
    		errDialog.setCancelable(false);
    		errDialog.setMessage(e.getMessage());
    		errDialog.show();
    		return data;
		} 
	}

	public List<Station> getStationList() {
		stationList = readStationData();
		return stationList;
	}

	public void setStationList(List<Station> stationList) {
		this.stationList = stationList;
	}
	
	public List<Station> readStationData(){
	    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + CONFIGDIR + "/" + STATIONFILE;
		List<Station> data = new ArrayList<Station>();
		try {
	    	FileInputStream is;
			BufferedReader reader;
			final File file = new File(filePath);
			if (file.exists()) {	
				is = new FileInputStream(file);				
			    reader = new BufferedReader(new InputStreamReader(is));
			    String line = reader.readLine();
			    while(line != null){
	            	if (line.length() > 1){
		            	data.add(new Station(line));			    	
	            	}			    	
			    	line = reader.readLine();
			    }
			    reader.close();
			}
			return data;
			
	    } catch (Exception e) {
			// TODO Auto-generated catch block
        	AlertDialog.Builder errDialog = new AlertDialog.Builder(this);
    		errDialog.setTitle("SYSTEM ERROR ");
    		errDialog.setNeutralButton("OK", null);	
    		errDialog.setCancelable(false);
    		errDialog.setMessage(e.getMessage());
    		errDialog.show();
    		return data;
		} 
	}
	
	public List<BirdCountEntity> readBirdCountData(){
	    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + DATADIR + "/" + BIRDDATAFILE;
		List<BirdCountEntity> data = new ArrayList<BirdCountEntity>();
		String line = "";
		try {
	    	FileInputStream is;
			BufferedReader reader;
			final File file = new File(filePath);
			if (file.exists()) {	
				is = new FileInputStream(file);				
			    reader = new BufferedReader(new InputStreamReader(is));
			    line = reader.readLine();
			    line = reader.readLine();
			    while(line != null){
	            	if (line.length() > 1){
		            	data.add(new BirdCountEntity(line));			    	
	            	}			    	
			    	line = reader.readLine();
			    }
			    reader.close();
			}
			return data;
			
	    } catch (Exception e) {
			// TODO Auto-generated catch block
        	AlertDialog.Builder errDialog = new AlertDialog.Builder(this);
    		errDialog.setTitle("SYSTEM ERROR ");
    		errDialog.setNeutralButton("OK", null);	
    		errDialog.setCancelable(false);
    		errDialog.setMessage(e.getMessage());
    		errDialog.show();
    		return data;
		} 
	}
	
	public void uploadAll(){
  	  	String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + DATADIR + "/" + BIRDDATAFILE;
  	  	new SendMail().execute(new String[]{filePath});
	}
	
	public void uploadAllOld(){
      Mail m = new Mail(); 
 
      String[] toArr = {"doctest.nz@gmail.com"}; 
      m.setTo(toArr); 
      //m.setFrom("wooo@wooo.com"); 
      //m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
      m.setBody("Email body."); 
 
      try { 
    	  String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + DATADIR + "/" + BIRDDATAFILE;
    	  m.addAttachment(filePath); 
 
        if(m.send()) { 
          Toast.makeText(BirdCountApp.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
        } else { 
          Toast.makeText(BirdCountApp.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
        } 
      } catch(Exception e) { 
        Toast.makeText(BirdCountApp.this, "There was a problem sending the email.\n" + e.getMessage(), Toast.LENGTH_LONG).show(); 
        //Log.e("MailApp", "Could not send email", e); 
      } 		
	}
	
	public String getBirdCountDataFile(){
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + DATADIR + "/" + BIRDDATAFILE;
		return filePath;
	}	
	
	public String getStationFile(){
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APPDIR + "/" + CONFIGDIR + "/" + STATIONFILE;
		return filePath;
	}
	
	private class SendMail extends AsyncTask<String, Integer, Void> {

		protected void onProgressUpdate() {
			// called when the background task makes any progress
		}

		protected void onPreExecute() {
			// called before doInBackground() is started
		}

		protected void onPostExecute() {
			// called after doInBackground() has finished
		}

		@Override
		protected Void doInBackground(String... filePath) {
			// TODO Auto-generated method stub
			try {
				sendAll(filePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void sendAll(String[] filePath) throws Exception {
			Mail m = new Mail();
//			String[] toArr = { "doctest.nz@gmail.com" };
//			m.setTo(toArr);
//			m.setBody("Email body.");
//			m.addAttachment(filePath[0]);
//			m.send();
			m.Send0();
			
			
		}

	}
	
}