package service;

import model.Employee;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private Connection connection;

    public EmployeeService() throws SQLException {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Retrieve all employees
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Employee emp = new Employee(
                rs.getInt("empid"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("ssn"),
                rs.getDouble("salary")
            );
            employees.add(emp);
        }
        rs.close();
        stmt.close();
        return employees;
    }

    // Insert a new employee
    public void insertEmployee(Employee emp) throws SQLException {
        String query = "INSERT INTO employees (first_name, last_name, email, ssn, salary) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, emp.getFirstName());
        pstmt.setString(2, emp.getLastName());
        pstmt.setString(3, emp.getEmail());
        pstmt.setString(4, emp.getSsn());
        pstmt.setDouble(5, emp.getSalary());
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Update an existing employee
    public void updateEmployee(int empid, String firstName, String lastName, String email, String ssn) throws SQLException {
        String query = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, ssn = ? WHERE empid = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, email);
        pstmt.setString(4, ssn);
        pstmt.setInt(5, empid);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Delete an employee
    public void deleteEmployee(int empid) throws SQLException {
        String query = "DELETE FROM employees WHERE empid = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, empid);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Search employee by Emp ID
    public Employee searchEmployeeById(int empid) throws SQLException {
        String query = "SELECT * FROM employees WHERE empid = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, empid);
        ResultSet rs = pstmt.executeQuery();

        Employee emp = null;
        if (rs.next()) {
            emp = new Employee(
                rs.getInt("empid"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("ssn"),
                rs.getDouble("salary")
            );
        }
        rs.close();
        pstmt.close();
        return emp;
    }

    // Search employee by SSN
    public Employee searchEmployeeBySSN(String ssn) throws SQLException {
        String query = "SELECT * FROM employees WHERE ssn = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, ssn);
        ResultSet rs = pstmt.executeQuery();

        Employee emp = null;
        if (rs.next()) {
            emp = new Employee(
                rs.getInt("empid"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("ssn"),
                rs.getDouble("salary")
            );
        }
        rs.close();
        pstmt.close();
        return emp;
    }

    // Search employees by First Name
    public List<Employee> searchEmployeeByFirstName(String firstName) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees WHERE first_name LIKE ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, firstName + "%");
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Employee emp = new Employee(
                rs.getInt("empid"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("ssn"),
                rs.getDouble("salary")
            );
            employees.add(emp);
        }
        rs.close();
        pstmt.close();
        return employees;
    }

    // Update salary by percentage within a range
    public void updateSalary(double lowerBound, double upperBound, double percentage) throws SQLException {
        String query = "UPDATE employees SET salary = salary * (1 + ? / 100) WHERE salary >= ? AND salary < ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setDouble(1, percentage);
        pstmt.setDouble(2, lowerBound);
        pstmt.setDouble(3, upperBound);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Generate pay report by job title
    public List<String> generatePayByJobTitle() throws SQLException {
        List<String> report = new ArrayList<>();
        String query = "SELECT jt.job_title, SUM(p.salary) as total_pay " +
                       "FROM employees e " +
                       "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                       "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                       "JOIN payroll p ON e.empid = p.empid " +
                       "GROUP BY jt.job_title";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String line = "Job Title: " + rs.getString("job_title") + ", Total Pay: $" + rs.getDouble("total_pay");
            report.add(line);
        }
        rs.close();
        stmt.close();
        return report;
    }
}