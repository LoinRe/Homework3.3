package Homework.H3W3Database.service;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;

import java.util.List;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty getFaculty(Long id);

    Faculty updateFaculty(Faculty faculty);

    void removeFaculty(Long id);

    List<Faculty> findByColor(String color);

    List<Student> getStudentsByFacultyId(Long facultyId); // Новый метод
}
