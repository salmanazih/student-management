package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for {@link Student}.
 * Standard CRUD methods are generated automatically.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Finds the students whose last name contains the given text, ignoring case.
     */
    List<Student> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Counts the students grouped by birth year.
     * Each row of the result is {year, count}.
     */
    @Query("select year(s.birthDate), count(s) from Student s where s.birthDate is not null group by year(s.birthDate) order by year(s.birthDate)")
    Collection<Object[]> countStudentsByBirthYear();

    /**
     * Counts the students born in a given year.
     */
    @Query("select count(s) from Student s where year(s.birthDate) = :year")
    long countByBirthYear(@Param("year") int year);
}
