package com.example.studentmanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Student stored in the database.
 */
@Entity
@Table(name = "students")
@Schema(description = "Student registered in the school")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Generated identifier", example = "1")
    private Integer id;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false, length = 60)
    @Schema(description = "Last name", example = "LACHGAR")
    private String lastName;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false, length = 60)
    @Schema(description = "First name", example = "Mohamed")
    private String firstName;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_date")
    @Schema(description = "Birth date", example = "1985-09-01")
    private Date birthDate;

    public Student() {
    }

    public Student(String lastName, String firstName, Date birthDate) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", lastName='" + lastName + "', firstName='" + firstName + "'}";
    }
}
