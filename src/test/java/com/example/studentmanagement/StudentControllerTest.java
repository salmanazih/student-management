package com.example.studentmanagement;

import com.example.studentmanagement.controller.StudentController;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests of {@link StudentController}.
 * Mockito replaces the service so the tests never touch a database.
 */
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student newStudent(int id, String lastName) {
        Student student = new Student();
        student.setId(id);
        student.setLastName(lastName);
        student.setFirstName("First");
        return student;
    }

    @Test
    void saveShouldReturnCreatedStatusAndTheSavedStudent() {
        Student student = newStudent(1, "LACHGAR");
        when(studentService.save(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = studentController.save(new Student());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LACHGAR", response.getBody().getLastName());
    }

    @Test
    void deleteShouldReturnNoContentWhenTheStudentExists() {
        when(studentService.delete(1)).thenReturn(true);

        ResponseEntity<Void> response = studentController.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteShouldReturnNotFoundWhenTheStudentDoesNotExist() {
        when(studentService.delete(anyInt())).thenReturn(false);

        ResponseEntity<Void> response = studentController.delete(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findAllShouldReturnEveryStudent() {
        when(studentService.findAll()).thenReturn(List.of(newStudent(1, "A"), newStudent(2, "B")));

        ResponseEntity<List<Student>> response = studentController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void countStudentShouldReturnTheTotal() {
        when(studentService.countStudents()).thenReturn(10L);

        ResponseEntity<Long> response = studentController.countStudent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, response.getBody());
    }

    @Test
    void findByYearShouldReturnTheDistribution() {
        Collection<Object[]> distribution = List.<Object[]>of(new Object[]{1985, 2L});
        when(studentService.countByBirthYear()).thenReturn(distribution);

        ResponseEntity<Collection<Object[]>> response = studentController.findByYear();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateShouldReturnNotFoundWhenTheStudentIsUnknown() {
        when(studentService.findById(42)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Student> response = studentController.update(42, newStudent(42, "Ghost"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(studentService, never()).save(any(Student.class));
    }

    @Test
    void updateShouldOverwriteTheExistingStudent() {
        Student existing = newStudent(3, "OLD");
        when(studentService.findById(3)).thenReturn(java.util.Optional.of(existing));
        when(studentService.save(any(Student.class))).thenAnswer(call -> call.getArgument(0));

        ResponseEntity<Student> response = studentController.update(3, newStudent(0, "NEW"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getId());
        assertEquals("NEW", response.getBody().getLastName());
        assertTrue(true);
    }
}
