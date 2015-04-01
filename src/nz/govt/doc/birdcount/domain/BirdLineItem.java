package nz.govt.doc.birdcount.domain;

public class BirdLineItem {
	private String name;
	private String distance;
	private String clust;
	private String precision;
	private String comment;
	
	public static String getHeader(){
		return "Species,Distance,Clust Size,Precision,Comment";
	}
	
	public String getDataStr(){
		return name + "," + distance + "," + clust + "," + precision 
				+ ",\"" + comment + "\"";
	}	
	
	public BirdLineItem(String name, String distance, String clust, String pecision, String comment){
		this.name = name;
		this.distance = distance;
		this.clust = clust;
		this.precision = pecision;
		this.comment = comment;
	}	
	
	public String getDisplayBird()
	{
		return name + ", " + distance + "m, " + clust + ", " + precision + ", " + comment;	
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getClust() {
		return clust;
	}
	public void setClust(String clust) {
		this.clust = clust;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	

}
