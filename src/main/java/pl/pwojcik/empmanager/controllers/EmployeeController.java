package pl.pwojcik.empmanager.controllers;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.service.EmployeeService;
import pl.pwojcik.empmanager.validators.EmployeeValidator;


@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	private final static int PAGE_SIZE = 5;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	private EmployeeValidator employeeValidator;
	
	@InitBinder("employee")
	private void initBinder(WebDataBinder binder) {
	    binder.addValidators(employeeValidator);
	    binder.registerCustomEditor( Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM-dd"), true ));
	    binder.registerCustomEditor( String.class, new StringTrimmerEditor( true ));
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getEmployees(Model model, @RequestParam("page") Optional<Integer> page, 
			@RequestParam("sort") Optional<Sort.Direction> sortDirection, @RequestParam("column") Optional<String> col ) {
		
		int pageNum = 1;
		long noOfPages = 1;
		String column =col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);
		
		if (!page.isPresent()) {
				model.addAttribute("employees", employeeService.findAll(Sort.by(sortDir,column)));

		} else {
			pageNum = page.get() - 1;
			noOfPages = employeeService.getNumberOfPages(PAGE_SIZE);
			model.addAttribute("employees", employeeService.findPageable(pageNum, PAGE_SIZE,sortDir,column));		
		}
		model.addAttribute("page", pageNum);
		model.addAttribute("noOfPages", noOfPages);

		return "employees/list";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "employees/new";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(Model model, @ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult ) {
		
		if (bindingResult.hasErrors())
			return new ModelAndView("employees/new");
		
		employeeService.saveOrUpdateEmployee(employee);
		/*model.addAttribute("employees", employeeService.findAll());
		model.addAttribute("page", 1);
		model.addAttribute("noOfPages", 1);*/

		return new ModelAndView("redirect:/employees?page=1");
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView deleteEmployee(@RequestParam("employee_id") String employee_id) {
		employeeService.deleteEmployee(Long.valueOf(employee_id));

		return new ModelAndView("redirect:/employees?page=1");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editEployee(Model model, @RequestParam("employee_id") String employee_id) {
		long id = Long.valueOf(employee_id);
		Employee employee = employeeService.findEmployee(id);
		
		if(employee == null) {
			return "employees/new";
		}
		model.addAttribute("employee", employee);
		return "employees/edit";
			
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateEployee(Model model, @ModelAttribute("employee") @Valid  Employee employee, BindingResult bindingResult) {
			
		if(bindingResult.hasErrors()) {
				return new ModelAndView("employees/edit");
		}
		employeeService.saveOrUpdateEmployee(employee);	
		/*model.addAttribute("employees", employeeService.findAll());
		model.addAttribute("page", 1);
		model.addAttribute("noOfPages", 1);*/
		return new ModelAndView("redirect:/employees?page=1");

	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchEmployees(Model model, @RequestParam("name") Optional<String> name,
			@RequestParam("surname") Optional<String> surname,
			@RequestParam("hiredate_from") Optional<String> hireDateFrom,
			@RequestParam("hiredate_to") Optional<String> hireDateTo, @RequestParam("email") Optional<String> email,
			@RequestParam("phone") Optional<String> phone, @RequestParam("sort") Optional<Sort.Direction> sortDirection, @RequestParam("column") Optional<String> col ) {

		int pageNum = 1;
		long noOfPages = 1;
		String column =col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);

		List<Employee> employees = employeeService
				.findEmployeesByCriteria(name, surname, email, phone, hireDateFrom, hireDateTo, sortDir, column);
		
	
		model.addAttribute("employees", employees);
		model.addAttribute("page", pageNum);
		model.addAttribute("noOfPages", noOfPages);

		return "employees/list";
	}
	
	@RequestMapping(value="{id}/details", method= RequestMethod.GET)
	public ModelAndView employeeDetails(Model model, @PathVariable long id) {
		if(!employeeService.isEmployeeInDatabase(id)) {
			return new ModelAndView("redirect:/employees?page=1");
		}
		Employee employee = employeeService.findEmployee(id);
		
		model.addAttribute("salaries", employee.getSalaries());
		model.addAttribute("employee", employee);
		return new ModelAndView("employees/details");
	}
	
}
