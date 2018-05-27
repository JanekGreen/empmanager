package pl.pwojcik.empmanager.ServiceImpl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.repository.EmployeeRepository;
import pl.pwojcik.empmanager.service.EmployeeService;
import pl.pwojcik.empmanager.utils.HelperMethods;

@Service
public class EmployeeServiceFirstImpl implements EmployeeService {

	@Autowired 
	private EmployeeRepository employeeRepository;
	
	@Override
	public Iterable<Employee> findAll() {
		return employeeRepository.findAll();
	}
	@Override
	public Iterable<Employee> findAll(Sort sort) {
		return employeeRepository.findAll(sort);
	}

	@Override
	public void saveOrUpdateEmployee(Employee employee) {
		employeeRepository.save(employee);
		
	}

	@Override
	public void deleteEmployee(long employeeId) {
		employeeRepository.deleteById(employeeId);
		
	}

	@Override
	public Employee findEmployee(long id) {
		
		if (isEmployeeInDatabase(id)) {
			Employee employee = employeeRepository.findById(id).get();
			return employee;
		}
		return null;
	}

	@Override
	public boolean isEmployeeInDatabase(long id) {
		return employeeRepository.findById(id).isPresent();
	}

	@Override
	public List<Employee> findEmployeesByCriteria(Optional<String> name, Optional<String> surname, Optional<String> email, Optional<String> phone,
			Optional<String> hireDateFrom, Optional<String> hireDateTo,Sort.Direction sortDirection, String column) {
		List<Employee> employees = employeeRepository
				.findUserByQueryCriteria(HelperMethods.getQueryParam(name),
				HelperMethods.getQueryParam(surname), HelperMethods.getQueryParam(email),
				HelperMethods.getQueryParam(phone), HelperMethods.getQueryParam(hireDateFrom, new Date(0)),
				HelperMethods.getQueryParam(hireDateTo, new Date(System.currentTimeMillis())));
		System.err.println("employees  count!"+ employees.size());
		HelperMethods.sortEmployees(employees, column, sortDirection);
		return employees;
	}
	public long getNumberOfPages(int pageSize) {
		long totalSize = employeeRepository.count();
		return totalSize % pageSize == 0 ? (totalSize / pageSize) : (totalSize / pageSize) + 1;
	}

	@Override
	public Iterable<Employee> findPageable(int pageNum, int pageSize) {
		return employeeRepository.findAll(PageRequest.of(pageNum, pageSize));
	}
	@Override
	public Iterable<Employee> findPageable(int pageNum, int pageSize, Sort.Direction sortDirection, String column) {
		return employeeRepository.findAll(PageRequest.of(pageNum, pageSize, sortDirection,column));
	}

	@Override
	public boolean isEmailDatabase(String email) {
		return employeeRepository.findByEmail(email)!=null;
	}
	public long getCount() {
		return employeeRepository.count();
	}

}
