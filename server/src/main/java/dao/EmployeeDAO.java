package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.Employee;

public class EmployeeDAO {
	
	private static Map<Integer,Employee> employeeMap = new HashMap<Integer, Employee>();
	private static int nextId;
	
	static {
		initEmployees();
	}

	private static void initEmployees() {
		Employee emp1 = new Employee(0, "Rafael", "Estudante", "2000-08-29", "2017-03-03");
		Employee emp2 = new Employee(1, "Natan", "Funcionário", "2000-08-29", "2017-03-03");
		Employee emp3 = new Employee(2, "Romeu", "Gerente", "2000-08-29", "2017-03-03");

		employeeMap.put(emp1.getId(), emp1);
		employeeMap.put(emp2.getId(), emp2);
		employeeMap.put(emp3.getId(), emp3);
		nextId = 3;
	}

	public static Employee getEmployee(int id) {
		return employeeMap.get(id);
	}

	public static List<Employee> getFirstNEmployees(int numberEmployees) {
		if(numberEmployees >= 0) {
			if(numberEmployees > employeeMap.size())
				numberEmployees = employeeMap.size();
			return getAllEmployees().subList(0, numberEmployees);
		}
		return null;
	}

	public static List<Employee> getFirstNEmployees(List<Employee> list, int numberEmployees) {
		if(numberEmployees >= 0) {
			if(numberEmployees > list.size())
				numberEmployees = list.size();
			return list.subList(0, numberEmployees);
		}
		return null;
	}
	
	public static List<Employee> getEmployeeByOffice(String office) {
		List<Employee> list = getAllEmployees(); 
		List<Employee> filteredEmployees = new ArrayList<Employee>();
		
		for(Employee emp : list)
			if(emp.getOffice().equals(office))
				filteredEmployees.add(emp);
		return filteredEmployees;
	}
	
	public static Employee addEmployee(String name, String office, String birthDate, String entryDate) {
		Employee emp = new Employee(nextId, name, office, birthDate, entryDate);
		employeeMap.put(emp.getId(), emp);
		nextId++;
		return emp;
	}

	public static Employee updateEmployee(Employee updatedEmp) {
		employeeMap.put(updatedEmp.getId(), updatedEmp);
		return updatedEmp;
	}

	public static void deleteEmployee(int id) {
		if(employeeMap.containsKey(id))
			employeeMap.remove(id);
	}

	public static List<Employee> getAllEmployees() {
		return new ArrayList<Employee>(employeeMap.values());
	}
	
}
