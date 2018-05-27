package pl.pwojcik.empmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import pl.pwojcik.empmanager.model.Employee;

public interface EmployeeService {
	Iterable<Employee> findAll();	
	Iterable<Employee> findPageable(int pageNum, int pageSize);
	Iterable<Employee> findPageable(int pageNum, int pageSize, Sort.Direction sortDirection, String column);
	void saveOrUpdateEmployee(Employee employee);
	void deleteEmployee(long employeeId);
	Employee findEmployee(long id);
	boolean isEmployeeInDatabase(long id);
	boolean isEmailDatabase(String email);
	long getNumberOfPages(int pageSize);
	long getCount();
	List<Employee> findEmployeesByCriteria(Optional<String> name, Optional<String> surname, 
			Optional<String> email,Optional<String> phone, Optional<String> hireDateFrom, Optional<String> hireDateTo,
			Sort.Direction sortDirection, String column);
	Iterable<Employee> findAll(Sort sort);
	
	
	
	

}
