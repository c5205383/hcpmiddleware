package com.hcp.objective.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.common.ExcludeForTest;
import com.hcp.objective.jpa.bean.Person;
import com.hcp.objective.persistence.DataSourceConfig;

@RestController
@ExcludeForTest
public class PersonController {
	public static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	
	//@Autowired  
	private  HttpServletRequest request;
	
	//@Autowired  
	private  DataSourceConfig dataSourceConfig;
	
	//@Autowired 
	private HttpServletResponse response;
	
	@RequestMapping(value = "/getPersonsJson")
	public @ResponseBody String getPersonJson(String json) {
		  // Append table that lists all persons
		EntityManager em = null;
        try {
            em = dataSourceConfig.entityManagerFactory().createEntityManager();
            List<Person> resultList = em.createNamedQuery("AllPersons").getResultList();

            JSONObject templateJsonObj = null;
            JSONArray jsonArr = new JSONArray();
      
            for (Person p : resultList) {
            	templateJsonObj = new JSONObject(p);
            	jsonArr.put(templateJsonObj);
            }
           // return jsonArr.toString();
         
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
        } finally {
            em.close();
        }
		return "";
	}
	
	@RequestMapping(value = "/getPersonsJsonInfo")
	public @ResponseBody void getPersonJsonInfo(String json) {
		  // Append table that lists all persons
		EntityManager em = null;
        try {
            em = dataSourceConfig.entityManagerFactory().createEntityManager();
            List<Person> resultList = em.createNamedQuery("AllPersons").getResultList();

            response.getWriter().println(
                    "<p><table border=\"1\"><tr><th colspan=\"3\">"
                            + (resultList.isEmpty() ? "" : resultList.size() + " ")
                            + "Entries in the Database</th></tr>");
            if (resultList.isEmpty()) {
                response.getWriter().println("<tr><td colspan=\"3\">Database is empty</td></tr>");
            } else {
                response.getWriter().println("<tr><th>First name</th><th>Last name</th><th>Id</th></tr>");
            }
      
            for (Person p : resultList) {
                response.getWriter().println(
                        "<tr><td>" + p.getName() + "</td><td>"
                                + p.getEmail() + "</td><td>====</td></tr>");
            }
            response.getWriter().println("</table></p>");
            appendAddForm(response);
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
        } finally {
            em.close();
        }
		//return "";
	}
	@RequestMapping(value = "/addPersonInfo")
    public void doAdd(HttpServletRequest request) throws ServletException, IOException, SQLException {
		EntityManager em = null;
        // Extract name of person to be added from request
        String firstName = request.getParameter("FirstName");
        String lastName = request.getParameter("LastName");

        try {
        	em = dataSourceConfig.entityManagerFactory().createEntityManager();
            if (firstName != null && lastName != null && !firstName.trim().isEmpty() && !lastName.trim().isEmpty()) {
                Person person = new Person();
                person.setName(firstName);
                person.setEmail(lastName);
                em.getTransaction().begin();
                em.persist(person);
                em.getTransaction().commit();
            }
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
        }  finally {
            em.close();
        }
        response.sendRedirect("getPersonsJsonInfo");
    }
	
	 private void appendAddForm(HttpServletResponse response) throws IOException {
	        // Append form through which new persons can be added
	        response.getWriter().println(
	                "<p><form action=\"addPersonInfo\" method=\"post\">" + "First name:<input type=\"text\" name=\"FirstName\">"
	                        + "&nbsp;Last name:<input type=\"text\" name=\"LastName\">"
	                        + "&nbsp;<input type=\"submit\" value=\"Add Person====\">" + "</form></p>");
	    }
	 
	 
	
}
