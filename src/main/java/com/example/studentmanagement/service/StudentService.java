package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business layer for students.
 * The controller only talks to this service, never to the repository directly.
 */
@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public boolean delete(int id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            return false;
        }
        studentRepository.delete(student.get());
        return true;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(int id) {
        return studentRepository.findById(id);
    }

    public long countStudents() {
        return studentRepository.count();
    }

    public Collection<Object[]> countByBirthYear() {
        return studentRepository.countStudentsByBirthYear();
    }

    public long countByBirthYear(int year) {
        return studentRepository.countByBirthYear(year);
    }

    public List<Student> searchByLastName(String lastName) {
        return studentRepository.findByLastNameContainingIgnoreCase(lastName);
    }
}
