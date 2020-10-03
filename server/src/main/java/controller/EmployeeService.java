package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import dao.EmployeeDAO;
import model.Employee;

@WebServlet("/api/employees/*")
public class EmployeeService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmployeeService() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		//GET BY ID
		if(pathInfo != null) {
			String[] params = pathInfo.split("/");

			if(params.length > 0) {
				Employee emp = EmployeeDAO.getEmployee(Integer.parseInt(params[1]));
				
				if(emp != null) {
					try {
						String employeeStr = new Gson().toJson(emp);
						
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().print(employeeStr);
						response.getWriter().flush();
					} catch (Exception e) {
					}
				}
				return;
			}			
		}
		
		List<Employee> list = null;
		String queriesList = request.getQueryString();
		
		if(queriesList != null) {
			String queries[] = queriesList.split("&");
			
			for(String query : queries) {
				String[] aux = query.split("=");
				
				if(aux[0].equals("firstNEmployees")) {
					int number = Integer.parseInt(aux[1]);
					if(list == null)
						list = EmployeeDAO.getFirstNEmployees(number);
					else 
						list = EmployeeDAO.getFirstNEmployees(list, number);
				}
				else if(aux[0].equals("office")) 
					list = EmployeeDAO.getEmployeeByOffice(aux[1]);
			}
			
		}
		else {
			list = EmployeeDAO.getAllEmployees();
		}
		
		try {
			String listStr = new Gson().toJson(list);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(listStr);
			response.getWriter().flush();
		} catch (Exception e) {
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuffer strBuffer = new StringBuffer();
		String line;
		
		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null)
				strBuffer.append(line);
			
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		JSONObject jsonObj = null;
		String  employeeStr = null;
		
		try {
			jsonObj = new JSONObject(strBuffer.toString());
			Employee emp = EmployeeDAO.addEmployee(jsonObj.getString("name"), jsonObj.getString("office"), 
					jsonObj.getString("birthDate"), jsonObj.getString("entryDate"));
			
			employeeStr = new Gson().toJson(emp);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (JSONException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(employeeStr);
		response.getWriter().flush();
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		if(pathInfo != null) {
			String[] params = pathInfo.split("/");
			
			if(params.length > 0) {
				
				Gson gson = new Gson();
				String employeeString = "";
				
				try {
					Employee emp = new Gson().fromJson(request.getReader(), Employee.class);
					EmployeeDAO.updateEmployee(emp);
					
					employeeString = gson.toJson(emp);
					response.setStatus(HttpServletResponse.SC_OK);
				} catch (JSONException e) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(employeeString);
				response.getWriter().flush();
			}
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
	
		if(pathInfo != null) {
			String[] params = pathInfo.split("/");
			
			if(params.length > 0) {
				EmployeeDAO.deleteEmployee(Integer.parseInt(params[1]));
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().flush();
			}
		}
	}

}
