import java.sql.SQLException;
import java.util.List;

public class EmployeeManagementSystem {
    public static void main(String[] args) {
        try {
            EmployeeService service = new EmployeeService();

            // Insert a new employee
            Employee emp = new Employee(0, "John", "Doe", "john.doe@example.com", "123-45-6789", 60000);
            service.insertEmployee(emp);
            System.out.println("Inserted employee: " + emp.getFirstName() + " " + emp.getLastName());

            // Search for an employee by name
            Employee foundEmp = service.searchEmployeeByFirstName("John").stream().findFirst().orElse(null);
            if (foundEmp != null) {
                System.out.println("Found employee: " + foundEmp.getFirstName() + " " + foundEmp.getLastName() + " (" + foundEmp.getEmail() + ")");
            }

            // Update employee information
            if (foundEmp != null) {
                service.updateEmployee(foundEmp.getEmpid(), "John", "Smith", "john.smith@example.com", "987-65-4321");
                System.out.println("Updated employee name to: John Smith");
            }

            // Update salary for employees within a specified range
            service.updateSalary(58000, 105000, 3.2);
            System.out.println("Updated salaries by 3.2% for employees within the specified range.");

            // Generate pay report by job title
            List<String> report = service.generatePayByJobTitle();
            report.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
