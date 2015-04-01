package nz.govt.doc.birdcount.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BirdCountEntity {
	private String date;
	private String location;
	private String station;
	private String observer;
	private String sun;
	private String temperature;
	private String precipitation1;
	private String precipitation2;
	private String wind;
	private String otherNoise;
	private String notes;
	private Boolean notMeasured;
	private String reason;
	private List<BirdLineItem> birds;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getObserver() {
		return observer;
	}

	public void setObserver(String observer) {
		this.observer = observer;
	}

	public String getSun() {
		return sun;
	}

	public void setSun(String sun) {
		this.sun = sun;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getPrecipitation1() {
		return precipitation1;
	}

	public void setPrecipitation1(String precipitation1) {
		this.precipitation1 = precipitation1;
	}

	public String getPrecipitation2() {
		return precipitation2;
	}

	public void setPrecipitation2(String precipitation2) {
		this.precipitation2 = precipitation2;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getOtherNoise() {
		return otherNoise;
	}

	public void setOtherNoise(String otherNoise) {
		this.otherNoise = otherNoise;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getNotMeasured() {
		return notMeasured;
	}

	public void setNotMeasured(Boolean notMeasured) {
		this.notMeasured = notMeasured;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<BirdLineItem> getBirds() {
		return birds;
	}

	public void setBirds(List<BirdLineItem> birds) {
		this.birds = birds;
	}	
	
	public static String getHeader() {
		return "Date,Location,Station,Observer,Sun,Temperature,Precipitation,,Wind,OtherNoise,Notes,NotMeasured,Reason";
	}

	public static String getHeaderAllStr() {
		return getHeader() + "," + BirdLineItem.getHeader() + "\r\n";
	}

	public String getDataStr() {
		return date + "," + location + "," + station + "," + observer + ","
				+ sun + "," + temperature + "," + precipitation1 + ","
				+ precipitation2 + "," + wind + "," + otherNoise + ",\""
				+ notes + "\"," + notMeasured.toString() + ",\"" + reason
				+ "\"";
	}

	public String getDataAll() {
		StringBuilder sb = new StringBuilder();
		String header = getDataStr();
		if (birds != null) {
			for (BirdLineItem b : birds) {
				sb.append(header + "," + b.getDataStr() + "\r\n");
			}
		} else {
			BirdLineItem bli = new BirdLineItem("", "", "", "", "");
			sb.append(header + "," + bli.getDataStr() + "\r\n");
		}
		return sb.toString();
	}

	public String getTextItem() {
		return date + ", " + location + "," + station + ", " + observer
				+ ", sun:" + sun + ", Temp:" + temperature + ", "
				+ precipitation1 + "," + precipitation2 + "," + wind
				+ ", Other Noise:" + otherNoise + ", \"" + notes
				+ "\", Not Measured:" + notMeasured.toString() + ", \""
				+ reason + "\"";
	}

	public String getTextItemShort() {
		return date + ", " + location + "-" + station + ", by " + observer;
	}

	public BirdCountEntity(String line) {
		if (line.length() > 1) {
			int p0 = line.indexOf(","); // date
			date = line.substring(0, p0);

			int p1 = line.indexOf(",", p0 + 1);// location
			location = line.substring(p0 + 1, p1);

			int p2 = line.indexOf(",", p1 + 1);// station
			station = line.substring(p1 + 1, p2);

			int p3 = line.indexOf(",", p2 + 1);// observer
			observer = line.substring(p2 + 1, p3);

			int p4 = line.indexOf(",", p3 + 1);// sun
			sun = line.substring(p3 + 1, p4);

			int p5 = line.indexOf(",", p4 + 1);// temperature
			temperature = line.substring(p4 + 1, p5);

			int p6 = line.indexOf(",", p5 + 1);// precipitation1
			precipitation1 = line.substring(p5 + 1, p6);

			int p7 = line.indexOf(",", p6 + 1);// precipitation2
			precipitation2 = line.substring(p6 + 1, p7);

			int p8 = line.indexOf(",", p7 + 1);// wind
			wind = line.substring(p7 + 1, p8);

			int p9 = line.indexOf(",\"", p8 + 1);// otherNoise
			otherNoise = line.substring(p8 + 1, p9);

			int p10 = line.indexOf("\",", p9 + 2);// notes
			notes = line.substring(p9 + 2, p10);

			int p11 = line.indexOf(",", p10 + 2);// notMeasured
			notMeasured = Boolean
					.parseBoolean(line.substring(p10 + 2, p11));

			int p12 = line.indexOf("\",", p11 + 2);// reason
			reason = line.substring(p11 + 2, p12);

			int p13 = line.indexOf(",", p12 + 2);// name
			String name = line.substring(p12 + 2, p13);

			int p14 = line.indexOf(",", p13 + 1);// distance
			String distance = line.substring(p13 + 1, p14);

			int p15 = line.indexOf(",", p14 + 1);// clust
			String clust = line.substring(p14 + 1, p15);

			int p16 = line.indexOf(",\"", p15 + 1);// precision
			String precision = line.substring(p15 + 1, p16);

			int p17 = line.indexOf("\"", p16 + 2);// comment
			String comment = line.substring(p16 + 2, p17);

			List<BirdLineItem> birds = new ArrayList<BirdLineItem>();
			birds.add(new BirdLineItem(name, distance, clust, precision,
					comment));
		}
	}

	public static List<String> getBirdCountList(List<BirdCountEntity> birdList){
		List<String> itemList = new ArrayList<String>();
		String preItem = "";
		String thisItem = "";
		for(BirdCountEntity b:birdList){
			thisItem = b.getTextItemShort();
			if (!thisItem.equals(preItem)){
				itemList.add(thisItem);
				preItem = thisItem;
			}
		}
		return itemList;
	}
}
