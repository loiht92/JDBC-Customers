package service;

import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceImpl implements CustomerService {
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/use_customers";
    private static final String jdbcUsername = "root";
    private static final String jdbcPassword = "loi123456";
    private static final String INSERT_CUSTOMERS_SQL = "INSERT INTO customers (name, email,address) VALUES (?,?,?);";
    private static final String SELECT_CUSTOMERS_BY_ID = "SELECT c.id, c.name, c.email, c.address FROM customers c WHERE c.id = ?;";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customers";
    private static final String DELETE_CUSTOMERS_SQL = "DELETE FROM customers WHERE id = ?;";
    private static final String UPDATE_CUSTOMERS_SQL = "UPDATE customers SET name = ?, email= ?, address = ? WHERE id = ?;";

    public void CustomerService(){

    }
    protected Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return connection;
    }
    @Override
    public void save(Customer customer) { //them
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMERS_SQL);){
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2,customer.getEmail());
            preparedStatement.setString(3,customer.getAddress());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public List<Customer> findAll() {
        List<Customer> customerList = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMERS);){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                Customer customer = new Customer(id, name,email,address);
                customerList.add(customer);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return customerList;
    }

    @Override
    public Customer findById(int id) {//tim theo id
        Customer customer = null;
        try(Connection connection = getConnection();
           PreparedStatement statement = connection.prepareStatement(SELECT_CUSTOMERS_BY_ID);){
           statement.setInt(1,id);
           ResultSet resultSet = statement.executeQuery();
           while (resultSet.next()){
               String name = resultSet.getString("name");
               String email = resultSet.getString("email");
               String address = resultSet.getString("address");
               customer = new Customer(name,email,address);
           }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public boolean update(int id, Customer customer) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_CUSTOMERS_SQL);) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getAddress());
            statement.setInt(4, id);

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public boolean remove(int id) throws SQLException {
       boolean rowDelete;
       try(Connection connection = getConnection();
       PreparedStatement statement = connection.prepareStatement(DELETE_CUSTOMERS_SQL);) {
       statement.setInt(1, id);
       rowDelete = statement.executeUpdate() > 0;

       }
       return rowDelete;
    }

}
