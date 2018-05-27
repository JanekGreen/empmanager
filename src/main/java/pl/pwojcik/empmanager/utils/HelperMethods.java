package pl.pwojcik.empmanager.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.model.Salary;

public class HelperMethods {

	public static String getQueryParam(Optional<String> param) {
		if (!param.isPresent() || (param.isPresent() && "".equals(param.get()))) {
			return "%%";
		}

		return new StringBuilder().append("%").append(param.get()).append("%").toString();
	}

	public static Date getQueryParam(Optional<String> param, Date defaultDate) {
		if (!param.isPresent() || (param.isPresent() && "".equals(param.get()))) {
			return defaultDate;
		}

		Date result = defaultDate;
		try {
			result = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(param.get()).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DATE!!!" + result.toString());

		return result;
	}

	public static Double getQueryParam(Optional<String> param, double defaultValue) {
		if (!param.isPresent() || (param.isPresent() && "".equals(param.get()))) {
			return defaultValue;
		}
		return Double.valueOf(param.get());
	}

	public static void sortEmployees(List<Employee> employees, String column, Sort.Direction direction) {

		if (direction == Sort.Direction.ASC) {
			switch (column) {
			case "id":
				employees.sort(Comparator.comparing(Employee::getId));
				break;
			case "name":
				employees.sort(Comparator.comparing(Employee::getName));
				break;
			case "surname":
				employees.sort(Comparator.comparing(Employee::getSurname));
				break;
			case "email":
				employees.sort(Comparator.comparing(Employee::getEmail));
				break;
			case "phone":
				employees.sort(Comparator.comparing(Employee::getPhone));
				break;
			case "birthdate":
				employees.sort(Comparator.comparing(Employee::getBirthdate));
				break;
			case "hiredate":
				employees.sort(Comparator.comparing(Employee::getHiredate));
				break;
			case "address":
				employees.sort(Comparator.comparing(Employee::getAddress));
				break;

			}
		} else if (direction == Sort.Direction.DESC) {

			switch (column) {
			case "id":
				employees.sort(Comparator.comparing(Employee::getId).reversed());
				break;
			case "name":
				employees.sort(Comparator.comparing(Employee::getName).reversed());
				break;
			case "surname":
				employees.sort(Comparator.comparing(Employee::getSurname).reversed());
				break;
			case "email":
				employees.sort(Comparator.comparing(Employee::getEmail).reversed());
				break;
			case "phone":
				employees.sort(Comparator.comparing(Employee::getPhone).reversed());
				break;
			case "birthdate":
				employees.sort(Comparator.comparing(Employee::getBirthdate).reversed());
				break;
			case "hiredate":
				employees.sort(Comparator.comparing(Employee::getHiredate).reversed());
				break;
			case "address":
				employees.sort(Comparator.comparing(Employee::getAddress).reversed());
				break;
			}
		}
	}

	public static void sortSalaries(List<Salary> salaries, String column, Sort.Direction direction) {

		if (direction == Sort.Direction.ASC) {
			switch (column) {
			case "id":
				salaries.sort(Comparator.comparing(Salary::getId));
				break;
			case "description":
				salaries.sort(Comparator.comparing(Salary::getDescription));
				break;
			case "amount":
				salaries.sort(Comparator.comparing(Salary::getAmount));
				break;
			case "date":
				salaries.sort(Comparator.comparing(Salary::getDate));
				break;

			}
		} else if (direction == Sort.Direction.DESC) {

			switch (column) {
			case "id":
				salaries.sort(Comparator.comparing(Salary::getId).reversed());
				break;
			case "description":
				salaries.sort(Comparator.comparing(Salary::getDescription).reversed());
				break;
			case "amount":
				salaries.sort(Comparator.comparing(Salary::getAmount).reversed());
				break;
			case "date":
				salaries.sort(Comparator.comparing(Salary::getDate).reversed());
				break;

			}
		}
	}
	public static Date getDistantFutureDate() {
	
		Calendar cal = Calendar.getInstance();
		cal.set(2100, 12, 01);
		return new java.sql.Date(cal.getTime().getTime());
	}
	 public static <T> List<T> toList(Iterable<T> iterable){
		List<T> result = new ArrayList<>();
		iterable.forEach(result::add);
		 return result;
		
	}
}
