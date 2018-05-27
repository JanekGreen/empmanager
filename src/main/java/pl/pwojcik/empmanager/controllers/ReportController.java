package pl.pwojcik.empmanager.controllers;

import org.springframework.web.bind.annotation.RestController;

import pl.pwojcik.empmanager.documents.CsvDocumentGenerator;
import pl.pwojcik.empmanager.documents.PdfDocumentGenerator;
import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.model.EmployeeSumSalary;
import pl.pwojcik.empmanager.model.MonthlyAverage;
import pl.pwojcik.empmanager.model.Salary;
import pl.pwojcik.empmanager.service.EmployeeService;
import pl.pwojcik.empmanager.service.SalaryService;
import pl.pwojcik.empmanager.utils.HelperMethods;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/reports")
public class ReportController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SalaryService salaryService;

	@RequestMapping(value = "/pdf/employees", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> employeesPdfReport(@RequestParam("name") Optional<String> name,
			@RequestParam("surname") Optional<String> surname,
			@RequestParam("hiredate_from") Optional<String> hireDateFrom,
			@RequestParam("hiredate_to") Optional<String> hireDateTo, @RequestParam("email") Optional<String> email,
			@RequestParam("phone") Optional<String> phone, @RequestParam("sort") Optional<Sort.Direction> sortDirection,
			@RequestParam("column") Optional<String> col) throws IOException {

		String column = col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);

		List<Employee> employees = employeeService.findEmployeesByCriteria(name, surname, email, phone, hireDateFrom,
				hireDateTo, sortDir, column);

		ByteArrayInputStream bis = PdfDocumentGenerator.employeesReport(employees);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=employeesReport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/pdf/salaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> salariesPdfReport(
			@RequestParam("description") Optional<String> description,
			@RequestParam("amount_from") Optional<String> amountFrom,
			@RequestParam("amount_to") Optional<String> amountTo, @RequestParam("date_from") Optional<String> dateFrom,
			@RequestParam("date_to") Optional<String> dateTo, @RequestParam("name") Optional<String> name,
			@RequestParam("surname") Optional<String> surname,
			@RequestParam("sort") Optional<Sort.Direction> sortDirection, @RequestParam("column") Optional<String> col)
			throws IOException {

		String column = col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);

		Iterable<Salary> salaries = salaryService.findSalaryByCriteria(description, amountFrom, amountTo, dateFrom,
				dateTo, name, surname, sortDir, column);

		ByteArrayInputStream bis = PdfDocumentGenerator.salariesReport(salaries);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=salariesReport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	
	@RequestMapping(value = "/csv/employees", method = RequestMethod.GET, produces = "text/csv")
	public void employeesCsv(HttpServletResponse response, @RequestParam("name") Optional<String> name,
			@RequestParam("surname") Optional<String> surname,
			@RequestParam("hiredate_from") Optional<String> hireDateFrom,
			@RequestParam("hiredate_to") Optional<String> hireDateTo, @RequestParam("email") Optional<String> email,
			@RequestParam("phone") Optional<String> phone, @RequestParam("sort") Optional<Sort.Direction> sortDirection,
			@RequestParam("column") Optional<String> col) throws IOException {

		String column = col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);
		
        response.setHeader("Content-Disposition", "attachment; filename=employees.csv");
		List<Employee> employees = employeeService.findEmployeesByCriteria(name, surname, email, phone, hireDateFrom,
				hireDateTo, sortDir, column);

		CsvDocumentGenerator.getEmployeesCsv(response.getWriter() , employees);
		

	}
	@RequestMapping(value = "/csv/salaries", method = RequestMethod.GET, produces = "text/csv")
	public void salariesCsv(HttpServletResponse response, @RequestParam("description") Optional<String> description,
			@RequestParam("amount_from") Optional<String> amountFrom,
			@RequestParam("amount_to") Optional<String> amountTo, @RequestParam("date_from") Optional<String> dateFrom,
			@RequestParam("date_to") Optional<String> dateTo, @RequestParam("name") Optional<String> name,
			@RequestParam("surname") Optional<String> surname,
			@RequestParam("sort") Optional<Sort.Direction> sortDirection, @RequestParam("column") Optional<String> col) throws IOException {

		String column = col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);
		
        response.setHeader("Content-Disposition", "attachment; filename=salaries.csv");
        Iterable<Salary> salaries = salaryService.findSalaryByCriteria(description, amountFrom, amountTo, dateFrom,
				dateTo, name, surname, sortDir, column);
		CsvDocumentGenerator.getSalariesCsv(response.getWriter() , HelperMethods.toList(salaries));
		
	}
	@RequestMapping(value = "/monthly/average", method = RequestMethod.GET)
	public Iterable<MonthlyAverage> monthlySalaries(@RequestParam("date_from") Optional<String> dateFrom, 
			@RequestParam("date_to") Optional<String> dateTo) {
		
		return salaryService.getAvgSalaryPerMonth(dateFrom, dateTo);
	}
	@RequestMapping(value = "/sum/salary", method = RequestMethod.GET)
	public Iterable<EmployeeSumSalary> sumSalaries() {
		
		return salaryService.getSumEarningsOfEmployees();
	}

}
