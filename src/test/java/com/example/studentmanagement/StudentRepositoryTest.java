package com.example.studentmanagement;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests of the repository and service layers backed by an in-memory H2 database.
 */
@DataJpaTest
@Import(StudentService.class)
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    private static Date toDate(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Test
    void saveShouldPersistAndGenerateAnIdentifier() {
        Student saved = studentRepository.save(new Student("ALAMI", "Sara", toDate(1990, 4, 18)));

        assertTrue(saved.getId() > 0);
        assertEquals(1, studentRepository.count());
    }

    @Test
    void countStudentsByBirthYearShouldGroupTheStudents() {
        studentRepository.save(new Student("A", "A", toDate(1985, 1, 1)));
        studentRepository.save(new Student("B", "B", toDate(1985, 6, 1)));
        studentRepository.save(new Student("C", "C", toDate(1990, 3, 1)));

        var distribution = studentService.countByBirthYear();

        assertEquals(2, distribution.size());
        assertEquals(2L, studentService.countByBirthYear(1985));
        assertEquals(1L, studentService.countByBirthYear(1990));
    }

    @Test
    void searchByLastNameShouldIgnoreCase() {
        studentRepository.save(new Student("BENNANI", "Youssef", toDate(1988, 2, 2)));

        assertEquals(1, studentService.searchByLastName("benn").size());
    }

    @Test
    void deleteShouldReturnFalseWhenTheStudentIsUnknown() {
        assertTrue(!studentService.delete(1234));
    }
}
