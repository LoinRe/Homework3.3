package Homework.H3W3Database.service;

import Homework.H3W3Database.models.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty getFaculty(Long id);

    Faculty updateFaculty(Faculty faculty);

    void removeFaculty(Long id);

    Collection<Faculty> findByColor(String color);
}
