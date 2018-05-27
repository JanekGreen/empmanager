package pl.pwojcik.empmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.pwojcik.empmanager.model.Salary;
import pl.pwojcik.empmanager.service.SalaryService;

@Controller
@RequestMapping("/")
public class DashboardController {
	
	@Autowired
	private SalaryService salaryService;
	
	@RequestMapping("/")
	public String dashboard(Model model) {
		Iterable<Salary> salaries = salaryService.findAll();
		model.addAttribute("salaries", salaries);
		model.addAttribute("sum",String.format("%.2f",salaryService.getSum(salaries)));
		model.addAttribute("average",String.format("%.2f",salaryService.getAverage(salaries)));
		model.addAttribute("lowest",salaryService.getLowest(salaries));
		model.addAttribute("highest",salaryService.getHighest(salaries));
		
		return "dashboard";
	}
}
