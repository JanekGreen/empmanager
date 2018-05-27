package pl.pwojcik.empmanager.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="EMPLOYEES")
public class Employee {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Size(min=2, max=30)
	@NotNull
	@Column(name="NAME",nullable=false)
	private String name;
	
	@Size(min=2, max=30)
	@NotNull
	@Column(name="SURNAME",nullable=false)
	private String surname;
	@Column(name="BIRTHDATE",nullable=false)
	private Date birthdate;
	@Column(name="HIREDATE", nullable=false)
	private Date hiredate;

	@NotBlank
	@Email
	@Column(name="EMAIL")
	private String email;
	
	@Size(min=6, max=16)
	@NotBlank
	@Column(name="PHONE")
	private String phone;
	
	@NotBlank
	@Size(min=10, max=60)
	@Column(name="ADDRESS")
	private String address;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="employee")
	private Set<Salary> salaries;
	
	
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Date getHiredate() {
		return hiredate;
	}

	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<Salary> getSalaries() {
		return salaries;
	}

	public void setSalaries(Set<Salary> salaries) {
		this.salaries = salaries;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
