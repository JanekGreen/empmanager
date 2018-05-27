package pl.pwojcik.empmanager.documents;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

import pl.pwojcik.empmanager.model.Employee;
import pl.pwojcik.empmanager.model.Salary;

import java.io.PrintWriter;
import java.util.List;


public class CsvDocumentGenerator {

	public static void getEmployeesCsv(PrintWriter writer, List<Employee> employees) {

		try {
			
			ColumnPositionMappingStrategy<Employee> mapStrategy = new ColumnPositionMappingStrategy<Employee>();

			String[] columns = new String[] { "id", "name", "surname", "birthdate", "hiredate", "phone", "email", "address" };
			mapStrategy.setColumnMapping(columns);
			mapStrategy.setType(Employee.class);
			mapStrategy.generateHeader();

			@SuppressWarnings("unchecked")
			StatefulBeanToCsv<Employee> btcsv = new StatefulBeanToCsvBuilder<>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
					.withMappingStrategy(mapStrategy)
					.withSeparator(',')
					.build();


			btcsv.write(employees);
		
		} catch (CsvException ex) {

		}
	}
	public static void getSalariesCsv(PrintWriter writer, List<Salary> salaries) {

		try {
			
			ColumnPositionMappingStrategy<Salary> mapStrategy = new ColumnPositionMappingStrategy<Salary>();

			String[] columns = new String[] { "id", "amount", "date", "description", "employee_id" };
			mapStrategy.setColumnMapping(columns);
			mapStrategy.setType(Salary.class);
			mapStrategy.generateHeader();

			@SuppressWarnings("unchecked")
			StatefulBeanToCsv<Salary> btcsv = new StatefulBeanToCsvBuilder<>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
					.withMappingStrategy(mapStrategy)
					.withSeparator(',')
					.build();


			btcsv.write(salaries);
		
		} catch (CsvException ex) {

		}
	}

}
