package pl.pwojcik.empmanager.service;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.model.EmployeeSumSalary;
import pl.pwojcik.empmanager.model.MonthlyAverage;
import pl.pwojcik.empmanager.model.Salary;

public interface SalaryService {
	Iterable<Salary> findAll();
	Iterable<Salary> findPageable(int pageNum, int pageSize);
	Iterable<Salary> findPageable(int pageNum, int pageSize, Sort.Direction sortDirection, String column);
	Iterable<Salary> findAll(Sort sort);
	void saveOrUpdateSalary(Salary employee);
	void deleteSalary(long salaryId);
	Salary findSalary(long id);
	boolean isSalaryInDatabase(long id);
	long getNumberOfPages(int pageSize);
	Iterable<Employee> findAllEmployees();
	Iterable<Salary> findSalaryByCriteria(Optional<String> description, Optional<String> amountFrom, 
			 Optional<String> amountTo, Optional<String> dateFrom, Optional<String> dateTo, Optional<String> name, Optional<String> surname,
			 Sort.Direction sortDirection, String column);
	
	double getSum(Iterable<Salary> salaries);
	double getAverage(Iterable<Salary> salaries);
	Salary getLowest(Iterable<Salary> salaries);
	Salary getHighest(Iterable<Salary> salaries);
	Iterable<MonthlyAverage> getAvgSalaryPerMonth(Optional<String> dateFrom, Optional<String> dateTo); 
	List<EmployeeSumSalary> getSumEarningsOfEmployees();
}
