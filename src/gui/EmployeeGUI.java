package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import model.Employee;
import service.EmployeeService;

import java.sql.SQLException;
import java.util.List;

public class EmployeeGUI {
    @FXML
    private TextField firstNameField, lastNameField, emailField, ssnField, salaryField, empIdField;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> empIdColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn, lastNameColumn, emailColumn, ssnColumn;

    @FXML
    private TableColumn<Employee, Double> salaryColumn;

    @FXML
    private Label statusLabel;

    private EmployeeService service;

    private ObservableList<Employee> employeeList;

    public EmployeeGUI() {
        try {
            service = new EmployeeService();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection error appropriately
        }
    }

    @FXML
    private void initialize() {
        // Initialize Table Columns
        empIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEmpid()).asObject());
        firstNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLastName()));
        emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        ssnColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSsn()));
        salaryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSalary()).asObject());

        loadEmployees();

        // Handle row selection
        employeeTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showEmployeeDetails(newValue));
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = service.getAllEmployees();
            employeeList = FXCollections.observableArrayList(employees);
            employeeTable.setItems(employeeList);
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading employees.");
        }
    }

    private void showEmployeeDetails(Employee employee) {
        if (employee != null) {
            empIdField.setText(String.valueOf(employee.getEmpid()));
            firstNameField.setText(employee.getFirstName());
            lastNameField.setText(employee.getLastName());
            emailField.setText(employee.getEmail());
            ssnField.setText(employee.getSsn());
            salaryField.setText(String.valueOf(employee.getSalary()));
        }
    }

    @FXML
    private void handleInsert() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String ssn = ssnField.getText().trim();
        String salaryText = salaryField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || ssn.isEmpty() || salaryText.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            Employee emp = new Employee(0, firstName, lastName, email, ssn, salary);
            service.insertEmployee(emp);
            statusLabel.setText("Employee inserted successfully.");

            loadEmployees();
            handleClear();
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error inserting employee.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid salary format.");
        }
    }

    @FXML
    private void handleUpdate() {
        String empIdText = empIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String ssn = ssnField.getText().trim();

        if (empIdText.isEmpty()) {
            statusLabel.setText("Please enter Emp ID to update.");
            return;
        }

        try {
            int empid = Integer.parseInt(empIdText);
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || ssn.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
                return;
            }

            service.updateEmployee(empid, firstName, lastName, email, ssn);
            statusLabel.setText("Employee updated successfully.");

            loadEmployees();
            handleClear();
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error updating employee.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid Emp ID format.");
        }
    }

    @FXML
    private void handleDelete() {
        String empIdText = empIdField.getText().trim();

        if (empIdText.isEmpty()) {
            statusLabel.setText("Please enter Emp ID to delete.");
            return;
        }

        try {
            int empid = Integer.parseInt(empIdText);
            service.deleteEmployee(empid);
            statusLabel.setText("Employee deleted successfully.");

            loadEmployees();
            handleClear();
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error deleting employee.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid Emp ID format.");
        }
    }

    @FXML
    private void handleSearch() {
        String empIdText = empIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String ssn = ssnField.getText().trim();

        try {
            if (!empIdText.isEmpty()) {
                int empid = Integer.parseInt(empIdText);
                Employee emp = service.searchEmployeeById(empid);
                if (emp != null) {
                    employeeTable.getSelectionModel().select(emp);
                    showEmployeeDetails(emp);
                    statusLabel.setText("Employee found by Emp ID.");
                } else {
                    statusLabel.setText("Employee not found by Emp ID.");
                }
            } else if (!ssn.isEmpty()) {
                Employee emp = service.searchEmployeeBySSN(ssn);
                if (emp != null) {
                    employeeTable.getSelectionModel().select(emp);
                    showEmployeeDetails(emp);
                    statusLabel.setText("Employee found by SSN.");
                } else {
                    statusLabel.setText("Employee not found by SSN.");
                }
            } else if (!firstName.isEmpty()) {
                List<Employee> employees = service.searchEmployeeByFirstName(firstName);
                if (!employees.isEmpty()) {
                    employeeList = FXCollections.observableArrayList(employees);
                    employeeTable.setItems(employeeList);
                    statusLabel.setText("Employees found by First Name.");
                } else {
                    statusLabel.setText("No employees found by First Name.");
                }
            } else {
                statusLabel.setText("Enter Emp ID, First Name, or SSN to search.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error searching employee.");
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid Emp ID format.");
        }
    }

    @FXML
    private void handleClear() {
        empIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        ssnField.clear();
        salaryField.clear();
        statusLabel.setText("");
        employeeTable.getSelectionModel().clearSelection();
        loadEmployees(); // Reload to show all employees if the table was filtered
    }
}
