package Homework.H3W3Database.repository;

import Homework.H3W3Database.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);
}
