package Homework.H3W3Database.service;

import Homework.H3W3Database.exception.FacultyNotFoundException;
import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty getFaculty(Long id) {
//        return facultyRepository.findById(id)
//                .orElseThrow(() -> new FacultyNotFoundException("Faculty not found"));
        return facultyRepository.findById(id).orElse(null);
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public void removeFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository
                .findAll()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .collect(Collectors.toList());
    }
}
