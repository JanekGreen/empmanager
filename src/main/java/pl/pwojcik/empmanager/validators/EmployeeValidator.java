package pl.pwojcik.empmanager.validators;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.service.EmployeeService;

@Component
public class EmployeeValidator implements Validator{

	@Autowired
	EmployeeService employeeService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Employee.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "hiredate", "user.hiredate.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthdate", "user.birthdate.empty");
		Employee employee = (Employee) target;
		Date now = new Date(System.currentTimeMillis());
	
		if((employee.getBirthdate() == null || employee.getHiredate() == null) || employee.getBirthdate().after(employee.getHiredate()) 
				|| employee.getBirthdate().after(now)) {
			errors.rejectValue("birthdate", "user.birthdate.invalid");
			errors.rejectValue("hiredate", "user.hiredate.invalid");
		}
		
		if(employee.getId() == null && employeeService.isEmailDatabase(employee.getEmail())){
			errors.rejectValue("email", "user.email.exists");
			
		}
		
		
	}

}
