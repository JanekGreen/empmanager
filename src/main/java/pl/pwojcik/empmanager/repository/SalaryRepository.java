package pl.pwojcik.empmanager.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import pl.pwojcik.empmanager.model.Salary;

public interface SalaryRepository extends PagingAndSortingRepository<Salary, Long>{
	@Query("Select s from Salary s left join Employee e on s.employee.id = e.id where description like ?1 and s.amount >= ?2 and s.amount<= ?3 and s.date >= ?4 and s.date <=?5 and name like ?6 and surname like ?7")
	List<Salary> findSalaryByQueryCriteria(String description, double amountFrom, 
			double amountTo, Date dateFrom, Date dateTo, String name, String surname);
	@Query(value="select date_format(date, '%M %Y') as header, date , AVG(amount) as average from salaries group by date_format(date, '%M %Y') HAVING date >=?1 and date <= ?2", nativeQuery=true)
	List<Object[]> getAvgSalaryPerMonth(Date dateFrom, Date dateTo); 
	@Query(value="SELECT employees.id, employees.name, employees.surname, SUM(salaries.amount) FROM salaries LEFT JOIN employees ON salaries.employee_id = employees.id GROUP BY employees.id", nativeQuery=true)
	List<Object[]> getSumEarningsOfEmployees(); 

}
