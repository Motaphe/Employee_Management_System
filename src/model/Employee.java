package model;

public class Employee {
    private int empid;
    private String firstName;
    private String lastName;
    private String email;
    private String ssn;
    private double salary;

    public Employee(int empid, String firstName, String lastName, String email, String ssn, double salary) {
        this.empid = empid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.ssn = ssn;
        this.salary = salary;
    }

    // Getters and Setters
    public int getEmpid() {
        return empid;
    }

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
