package pl.pwojcik.empmanager.ServiceImpl;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.model.EmployeeSumSalary;
import pl.pwojcik.empmanager.model.MonthlyAverage;
import pl.pwojcik.empmanager.model.Salary;
import pl.pwojcik.empmanager.repository.EmployeeRepository;
import pl.pwojcik.empmanager.repository.SalaryRepository;
import pl.pwojcik.empmanager.service.SalaryService;
import pl.pwojcik.empmanager.utils.HelperMethods;

@Service
public class SalaryServiceFirstImpl implements SalaryService {
	@Autowired
	private SalaryRepository salaryRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Override
	public Iterable<Salary> findAll() {
		return salaryRepository.findAll();
	}
	@Override
	public Iterable<Salary> findPageable(int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return salaryRepository.findAll(PageRequest.of(pageNum, pageSize));
	}
	@Override
	public void saveOrUpdateSalary(Salary salary) {
		salaryRepository.save(salary);
		
	}
	@Override
	public void deleteSalary(long salaryId) {
		salaryRepository.deleteById(salaryId);
		
	}
	@Override
	public Salary findSalary(long id) {
		if(isSalaryInDatabase(id)) {
			Salary salary = salaryRepository.findById(id).get();
			return salary;
		}
		return null;
	}
	@Override
	public boolean isSalaryInDatabase(long id) {
		return salaryRepository.findById(id).isPresent();
	}
	@Override
	public long getNumberOfPages(int pageSize) {
		long totalSize = salaryRepository.count();
		return totalSize % pageSize == 0 ? (totalSize / pageSize) : (totalSize / pageSize) + 1;
	}
	@Override
	public Iterable<Employee> findAllEmployees() {
		return employeeRepository.findAll();
	}
	@Override
	public Iterable<Salary> findSalaryByCriteria(Optional<String> description, Optional<String> amountFrom, 
			 Optional<String> amountTo, Optional<String> dateFrom, Optional<String> dateTo, Optional<String> name, Optional<String> surname,
			 Sort.Direction sortDirection, String column) {
		System.err.println("service !datefrom:! "+dateFrom+"!date to!"+ dateTo);   
		List<Salary> salaries = salaryRepository.findSalaryByQueryCriteria(HelperMethods.getQueryParam(description), HelperMethods.getQueryParam(amountFrom, 1.0d), HelperMethods.getQueryParam(amountTo,Double.MAX_VALUE), HelperMethods.getQueryParam(dateFrom, new Date(0)), HelperMethods.getQueryParam(dateTo, new Date(System.currentTimeMillis())), 
				 HelperMethods.getQueryParam(name), HelperMethods.getQueryParam(surname));
		   HelperMethods.sortSalaries(salaries, column, sortDirection);
		   System.err.println("salaries  count!"+ salaries.size());
		  return salaries;
	}
	@Override
	public Iterable<Salary> findPageable(int pageNum, int pageSize, Direction sortDirection, String column) {
		return salaryRepository.findAll(PageRequest.of(pageNum, pageSize, sortDirection,column));
	}
	@Override
	public Iterable<Salary> findAll(Sort sort) {
		return salaryRepository.findAll(sort);
	}
	
	
	
	
	
	@Override
	public double getSum(Iterable<Salary> salaries) {
		double sum = 0;
		for(Salary s : salaries) {
			sum+=s.getAmount();
		}
		return sum;
	}
	@Override
	public double getAverage(Iterable<Salary> salaries) {
		double sum = 0;
		int count = 0;
		
		for(Salary s : salaries) {
			sum+=s.getAmount();
			count++;
		}
		return sum/count;
	}
	@Override
	public Salary getLowest(Iterable<Salary> salaries) {
		Salary minimalSalary = null;
		double minimalAmount = Double.MAX_VALUE;
		for(Salary s: salaries) {
			if(s.getAmount()<minimalAmount) {
				minimalSalary = s;
				minimalAmount = s.getAmount();
			}
		}
		return minimalSalary;
	}
	@Override
	public Salary getHighest(Iterable<Salary> salaries) {
		Salary maxSalary = null;
		double maxAmount = Double.MIN_VALUE;
		for(Salary s: salaries) {
			if(s.getAmount()>maxAmount) {
				maxSalary = s;
				maxAmount = s.getAmount();
			}
		}
		return maxSalary;
	}
	@Override
	public List<MonthlyAverage> getAvgSalaryPerMonth(Optional<String> dateFrom, Optional<String> dateTo) {
		
		Date dateFrom_= HelperMethods.getQueryParam(dateFrom, new Date(0));
		Date dateTo_= HelperMethods.getQueryParam(dateTo, new Date(System.currentTimeMillis()));
	
		List<Object[]> dbRawData = salaryRepository.getAvgSalaryPerMonth(dateFrom_, dateTo_);
		List<MonthlyAverage> result = new ArrayList<>();
		for(Object[] row : dbRawData) {
			// row[0] - String header, // row[1] - full date (for sql query)  row[2] average  
			result.add(new MonthlyAverage(String.valueOf(row[0]),Double.valueOf(String.valueOf(row[2]))));
		}
		return result;
	}
	public List<EmployeeSumSalary> getSumEarningsOfEmployees(){
		List<Object[]> dbRawData = salaryRepository.getSumEarningsOfEmployees();
		List<EmployeeSumSalary> result = new ArrayList<>();
		for(Object[] row : dbRawData) {
			// row[0] - Long employee id, // row[1] - String name  row[2] String surname, Double sum salary  
			result.add(new EmployeeSumSalary(Long.valueOf(String.valueOf(row[0])),String.valueOf(row[1]), String.valueOf(row[2]), Double.valueOf(String.valueOf(row[3]))) );
		}
		
		return result;
		
	} 

}
