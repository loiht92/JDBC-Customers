package controller;

import model.Customer;
import service.CustomerService;
import service.CustomerServiceImpl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CustomersServlet", urlPatterns = "/customers")
public class CustomersServlet extends HttpServlet {
    private CustomerService customerService = new CustomerServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                createCustomer(request,response);
                break;
            case "edit":
                try {
                    updateCustomer(request,response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "delete":
                try {
                    deleteCustomer(request,response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                showCreateForm(request,response);
                break;
            case "edit":
                showEditForm(request,response);
                break;
            case "delete":
                showDeleteCustomer(request,response);
                break;
            default:
                ListCustomers(request, response);
                break;
        }
    }

    private void ListCustomers(HttpServletRequest request, HttpServletResponse response) {
        List<Customer> customers = this.customerService.findAll();
        request.setAttribute("customers", customers);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("customer/list.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    private void createCustomer(HttpServletRequest request,HttpServletResponse response){
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        Customer customer = new Customer(name,email,address);
        this.customerService.save(customer);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("create.jsp");
        request.setAttribute("message", "New customer was created");
        try {
            requestDispatcher.forward(request,response);
        }catch (IOException | ServletException e){
            e.printStackTrace();
        }
    }

   private void showCreateForm(HttpServletRequest request, HttpServletResponse response){
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("customer/create.jsp");
        try {
            requestDispatcher.forward(request,response);
        }catch (IOException | ServletException e){
            e.printStackTrace();
        }
   }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        Customer customer = this.customerService.findById(id);
        RequestDispatcher requestDispatcher;
        if(customer == null){
            requestDispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            customer.setName(name);
            customer.setEmail(email);
            customer.setAddress(address);
            this.customerService.update(id, customer);
            request.setAttribute("customer", customer);
            request.setAttribute("message", "Customer information was updated");
            requestDispatcher = request.getRequestDispatcher("customer/edit.jsp");
        }
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
    private void showEditForm(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher requestDispatcher;
        if (customer == null){
            requestDispatcher = request.getRequestDispatcher("error-404.jsp");
        }else {
            request.setAttribute("customer", customer);
            requestDispatcher = request.getRequestDispatcher("customer/edit.jsp");
        }
        try {
            requestDispatcher.forward(request,response);
        }catch (IOException | ServletException e){
            e.printStackTrace();
        }
    }
    private void deleteCustomer (HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher requestDispatcher;
        if (customer == null){
            requestDispatcher = request.getRequestDispatcher("error-404.jsp");
        }else {
            this.customerService.remove(id);
            try {
                response.sendRedirect("/customers");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void showDeleteCustomer (HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher requestDispatcher;
        if (customer == null){
            requestDispatcher = request.getRequestDispatcher("error-404.jsp");
        }else {
            request.setAttribute("customer", customer);
            requestDispatcher = request.getRequestDispatcher("customer/delete.jsp");
        }
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
