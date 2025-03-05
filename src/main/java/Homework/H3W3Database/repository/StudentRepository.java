package Homework.H3W3Database.repository;

import Homework.H3W3Database.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAgeBetween(Integer min, Integer max);

    Optional<Student> findStudentById(long id);

    @Query(value = "SELECT COUNT(id) from student", nativeQuery = true)
    int getAmountOfStudents();

    @Query(value = "SELECT avg(age) from student", nativeQuery = true)
    int getAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudents();
}


