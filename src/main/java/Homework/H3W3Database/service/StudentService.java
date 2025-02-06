package Homework.H3W3Database.service;

import Homework.H3W3Database.models.Student;

import java.util.Collection;

public interface StudentService {
    Student createStudent(Student student);

    Student getStudent(Long id);

    Student updateStudent(Student student);

    void removeStudent(Long id);

    Collection<Student> findByAge(Integer age);
}
