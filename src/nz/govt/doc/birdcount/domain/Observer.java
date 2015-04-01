

package nz.govt.doc.birdcount.domain;

import java.util.ArrayList;
import java.util.List;

public class Observer {
	private String name;
	private String email;

	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTextItem(){
		return name + " - " + email;
	}
	
	public String getSpinnerText(){
		return name;
	}	
	
	public String getFileText(){
		return name + "~" + email;
	}
	
	public Observer(String name, String email){
		this.name = name;
		this.email = email;
	}
	
	public Observer(String line){
		String[] field = line.split("~");
		this.name = field[0];
    	this.email = field[1];	
	}

	public static List<String> getSpinnerList(List<Observer> observerList){
		List<String> spinnerList = new ArrayList<String>();
		for(Observer s:observerList){
			spinnerList.add(s.getSpinnerText());
		}
		return spinnerList;
	}	

}
