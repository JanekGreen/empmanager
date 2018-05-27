package pl.pwojcik.empmanager.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import pl.pwojcik.empmanager.model.Employee;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

	@Query("select e from Employee e where e.name like ?1 and e.surname like ?2 and e.email like ?3 and e.phone like ?4 and e.hiredate between ?5 and ?6")
	List<Employee> findUserByQueryCriteria(String name, String surname, String email, String phone, Date dateFrom, Date dateTo);
	Employee findByEmail(String email);
}
