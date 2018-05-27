package pl.pwojcik.empmanager.validators;

import java.sql.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.pwojcik.empmanager.model.Salary;

@Component
public class SalaryValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Salary.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "salary.date.empty");
		
		Date now = new Date(System.currentTimeMillis());
		Salary salary = (Salary) target;
		if(salary.getAmount() == null || salary.getAmount()<= 0) {
			errors.rejectValue("amount", "salary.amount.invalid");
		}
		if(salary.getDate() == null || salary.getDate().before(salary.getEmployee().getHiredate())) {
			errors.rejectValue("date", "salary.date.invalid");
		}
		
/*		if(salary.getDate() == null || salary.getDate().after(now)) {
			errors.rejectValue("date", "salary.date.invalid");
		}*/
		
	}

}
