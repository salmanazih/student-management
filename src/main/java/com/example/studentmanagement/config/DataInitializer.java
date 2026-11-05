package com.example.studentmanagement.config;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Seeds a few students on startup so the API can be tried immediately.
 * Disabled during the automated tests through the "!test" profile.
 */
@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    CommandLineRunner initStudents(StudentRepository studentRepository) {
        return args -> {
            if (studentRepository.count() > 0) {
                return;
            }

            List<Student> students = List.of(
                    new Student("LACHGAR", "Mohamed", toDate(1985, 9, 1)),
                    new Student("ALAMI", "Sara", toDate(1990, 4, 18)),
                    new Student("BENNANI", "Youssef", toDate(1985, 12, 5)));

            studentRepository.saveAll(students);
            studentRepository.findAll().forEach(System.out::println);
        };
    }

    private static Date toDate(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
