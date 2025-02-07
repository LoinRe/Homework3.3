package Homework.H3W3Database.service;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(Student student);

    Student getStudent(Long id);

    Student updateStudent(Student student);

    void removeStudent(Long id);

    List<Student> findByAge(Integer age);

    Faculty getFacultyByStudentId(Long studentId); // Новый метод
}
