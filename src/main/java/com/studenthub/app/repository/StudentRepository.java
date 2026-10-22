package com.studenthub.app.repository;

import com.studenthub.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByRollNumber(String rollNumber);
    boolean existsByEmail(String email);
    @Query("select s from Student s where lower(s.name) like lower(concat('%', :q, '%')) or lower(s.rollNumber) like lower(concat('%', :q, '%')) or lower(s.email) like lower(concat('%', :q, '%')) or lower(s.course) like lower(concat('%', :q, '%'))")
    List<Student> search(@Param("q") String q);
    long countByStatus(Student.Status status);
}
