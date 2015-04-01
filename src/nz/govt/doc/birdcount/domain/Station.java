package nz.govt.doc.birdcount.domain;

import java.util.ArrayList;
import java.util.List;

public class Station {
	private String name;
	private String shortName;
	private double latitude;
	private double longitude;
	public String getName() {
		return name;
	}
	public String getShortName() {
		return shortName;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getTextItem(){
		return shortName + " - " + name + ", Lat:" + String.valueOf(latitude) + ",Long:" + String.valueOf(longitude);
	}
	
	public String getSpinnerText(){
		return shortName + " - " + name;
	}	
	
	public String getFileText(){
		return shortName + "~" + name + "~" + String.valueOf(latitude) + "~" + String.valueOf(longitude);
	}
	
	public Station(String name, String shortName, double latitude, double longitude){
		this.name = name;
		this.shortName = shortName;
		this.latitude = latitude;
		this.longitude = longitude;		
	}
	
	public Station(String line){
		String[] field = line.split("~");
		this.shortName = field[0];
    	this.name = field[1];
    	String latitudeStr = field[2];
    	String longitudeStr = field[3];
    	double latitude = 0.0;
    	double longitude = 0.0;
    	try{latitude = Double.parseDouble(latitudeStr);}
    	catch(Exception e){}
    	try{longitude = Double.parseDouble(longitudeStr);}
    	catch(Exception e){}
    	this.latitude = latitude;
    	this.longitude = longitude;		
	}

	public static List<String> getSpinnerList(List<Station> stationList){
		List<String> spinnerList = new ArrayList<String>();
		for(Station s:stationList){
			spinnerList.add(s.getSpinnerText());
		}
		return spinnerList;
	}	

}
