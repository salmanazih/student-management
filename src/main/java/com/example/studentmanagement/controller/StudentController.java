package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API exposing the student management operations.
 * Every endpoint is documented with OpenAPI annotations so it shows up in Swagger UI.
 */
@RestController
@RequestMapping("/students")
@Tag(name = "Students", description = "Student management endpoints")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Create a student", description = "Persists a new student and returns it with its generated identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    @PostMapping("/save")
    public ResponseEntity<Student> save(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentService.save(student), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a student", description = "Replaces the data of an existing student")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(
            @Parameter(description = "Identifier of the student") @PathVariable int id,
            @Valid @RequestBody Student student) {
        return studentService.findById(id)
                .map(existing -> {
                    student.setId(id);
                    return ResponseEntity.ok(studentService.save(student));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a student", description = "Deletes the student with the given identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifier of the student") @PathVariable("id") int id) {
        return studentService.delete(id)
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "List all students")
    @ApiResponse(responseCode = "200", description = "Students returned")
    @GetMapping("/all")
    public ResponseEntity<List<Student>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @Operation(summary = "Find a student by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Count the students")
    @ApiResponse(responseCode = "200", description = "Number of students returned")
    @GetMapping("/count")
    public ResponseEntity<Long> countStudent() {
        return ResponseEntity.ok(studentService.countStudents());
    }

    @Operation(summary = "Count the students per birth year")
    @ApiResponse(responseCode = "200", description = "Distribution returned as [year, count] pairs")
    @GetMapping("/byYear")
    public ResponseEntity<Collection<Object[]>> findByYear() {
        return ResponseEntity.ok(studentService.countByBirthYear());
    }

    @Operation(summary = "Count the students born in a given year")
    @GetMapping("/countByYear")
    public ResponseEntity<Long> countByYear(@RequestParam int year) {
        return ResponseEntity.ok(studentService.countByBirthYear(year));
    }

    @Operation(summary = "Search the students by last name")
    @GetMapping("/search")
    public ResponseEntity<List<Student>> search(@RequestParam String lastName) {
        return ResponseEntity.ok(studentService.searchByLastName(lastName));
    }
}
