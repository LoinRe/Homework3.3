package Homework.H3W3Database.repository;

import Homework.H3W3Database.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAgeBetween(Integer min, Integer max);
}
