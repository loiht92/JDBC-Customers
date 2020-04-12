package service;

import model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService {
    public void save(Customer customer);

    public List<Customer> findAll();

    public Customer findById(int id);

    public boolean update(int id,Customer customer) throws SQLException;

    public boolean remove(int id) throws SQLException;
}
