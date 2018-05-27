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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import pl.pwojcik.empmanager.model.Salary;
import pl.pwojcik.empmanager.service.SalaryService;
import pl.pwojcik.empmanager.utils.HelperMethods;
import pl.pwojcik.empmanager.validators.SalaryValidator;

@Controller
@RequestMapping("/salaries")
public class SalaryController {
	private static final int PAGE_SIZE = 5;
	@Autowired
	private SalaryService salaryService;
	
	@Autowired
	private SalaryValidator salaryValidator;


	@InitBinder("salary")
	private void initBinder(WebDataBinder binder) {
	    binder.addValidators(salaryValidator);
	    binder.registerCustomEditor( Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM-dd"), true ));
	    binder.registerCustomEditor( String.class, new StringTrimmerEditor( true ));
	}
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getSalaries(Model model,@RequestParam("page") Optional<Integer> page,
			@RequestParam("sort") Optional<Sort.Direction> sortDirection, @RequestParam("column") Optional<String> col ) {
		int pageNum = 1;
		long noOfPages = 1;
		Iterable<Salary> salaries = null;
		String column =col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);
		
		if(!page.isPresent()) {
			salaries = salaryService.findAll(Sort.by(sortDir,column));
			model.addAttribute("salaries", salaries);
		}else {
			pageNum = page.get() -1;
			noOfPages = salaryService.getNumberOfPages(PAGE_SIZE);
			salaries = salaryService.findPageable(pageNum, PAGE_SIZE, sortDir,column);
			model.addAttribute("salaries", salaries);
		}
		model.addAttribute("sum",String.format("%.2f",salaryService.getSum(salaries)));
		model.addAttribute("average",String.format("%.2f",salaryService.getAverage(salaries)));
		model.addAttribute("lowest",salaryService.getLowest(salaries));
		model.addAttribute("highest",salaryService.getHighest(salaries));
		model.addAttribute("page", pageNum);
		model.addAttribute("noOfPages", noOfPages);
		
		return new ModelAndView("salaries/list");
	}
	
	@RequestMapping(value = "/monthly", method = RequestMethod.GET)
	public String monthlySalaries(Model model,@RequestParam("date_from") Optional<String> dateFrom, 
			@RequestParam("date_to") Optional<String> dateTo) {
		
		model.addAttribute("monthlies", salaryService.getAvgSalaryPerMonth(dateFrom, dateTo));
		return "salaries/monthly";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newSalary(Model model) {
		// setting employee object to bind it to the view...
		model.addAttribute("salary", new Salary());
		model.addAttribute("employees", salaryService.findAllEmployees());
		return "salaries/new";
	}
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(Model model, @ModelAttribute("salary") @Valid Salary salary, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("employees", salaryService.findAllEmployees());
			return new ModelAndView("salaries/new");
		}
		salaryService.saveOrUpdateSalary(salary);
		return new ModelAndView("redirect:/salaries?page=1");
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editSalary(Model model, @RequestParam("salary_id") String salary_id) {
		long id = Long.valueOf(salary_id);
		Salary salary = salaryService.findSalary(id);
		if(salary == null) {
			return "salaries/new";
		}else {
			model.addAttribute("salary", salary);
			model.addAttribute("employees",salaryService.findAllEmployees());	
			return "salaries/edit";
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateSalary(Model model, @ModelAttribute("salary") @Valid Salary salary, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("employees", salaryService.findAllEmployees());
			return new ModelAndView("salaries/edit");
			
		}
		salaryService.saveOrUpdateSalary(salary);
		return new ModelAndView("redirect:/salaries?page=1");
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView deleteEmployee(@RequestParam("salary_id") String salary_id) {
		salaryService.deleteSalary(Long.valueOf(salary_id));

		return new ModelAndView("redirect:/salaries?page=1");
	}
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchEmployees(Model model, @RequestParam("description") Optional<String> description,
			@RequestParam("amount_from") Optional<String> amountFrom,
			@RequestParam("amount_to") Optional<String> amountTo,
			@RequestParam("date_from") Optional<String> dateFrom, @RequestParam("date_to") Optional<String> dateTo,
			@RequestParam("name") Optional<String> name, @RequestParam("surname") Optional<String> surname,
			@RequestParam("sort") Optional<Sort.Direction> sortDirection, @RequestParam("column") Optional<String> col) {

		int pageNum = 1;
		long noOfPages = 1;
		String column =col.orElse("id");
		Sort.Direction sortDir = sortDirection.orElse(Sort.Direction.ASC);

		Iterable<Salary> salaries = salaryService
				.findSalaryByCriteria(description, amountFrom, amountTo, dateFrom, dateTo, name, surname, sortDir, column);
		
		model.addAttribute("salaries", salaries);
		model.addAttribute("sum",String.format("%.2f",salaryService.getSum(salaries)));
		model.addAttribute("average",String.format("%.2f",salaryService.getAverage(salaries)));
		model.addAttribute("lowest",salaryService.getLowest(salaries));
		model.addAttribute("highest",salaryService.getHighest(salaries));
		model.addAttribute("page", pageNum);
		model.addAttribute("noOfPages", noOfPages);

		return "salaries/list";
	}
}
