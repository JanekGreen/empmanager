package pl.pwojcik.empmanager.model;

public class EmployeeSumSalary {
	private Long id;
	private String name;
	private String surname;
	private double sumSalary;
	
	public EmployeeSumSalary() {}
	
	public EmployeeSumSalary(Long id, String name, String surname, double sumSalary) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.sumSalary = sumSalary;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public double getSumSalary() {
		return sumSalary;
	}
	public void setSumSalary(double sumSalary) {
		this.sumSalary = sumSalary;
	}
	
	
	
}
