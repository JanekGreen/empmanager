package pl.pwojcik.empmanager.model;

public class MonthlyAverage {
	
	private String header;
	private double average;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	
	public MonthlyAverage(String header, double average) {
		this.header = header;
		this.average = average;
	}
	public MonthlyAverage() {}
	

}
