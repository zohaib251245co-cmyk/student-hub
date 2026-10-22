package com.studenthub.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required") @Size(max = 100)
    private String name;
    @NotBlank(message = "Roll number is required") @Column(unique = true) @Size(max = 30)
    private String rollNumber;
    @NotBlank(message = "Email is required") @Email(message = "Enter a valid email") @Column(unique = true)
    private String email;
    @Size(max = 20) private String phone;
    @NotBlank(message = "Course is required") private String course;
    private String department;
    @Min(value = 0, message = "Fees cannot be negative") private double fees;
    @Enumerated(EnumType.STRING) private Status status = Status.ACTIVE;

    public enum Status { ACTIVE, INACTIVE }
    public Student() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public double getFees() { return fees; }
    public void setFees(double fees) { this.fees = fees; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
