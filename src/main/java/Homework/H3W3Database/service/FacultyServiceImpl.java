package Homework.H3W3Database.service;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;
import Homework.H3W3Database.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyServiceImpl implements FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) { //Если неправильно написано, может все покрашить
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(null);
        logger.info("Факультет успешно создан. ID нового факультета: {}", facultyRepository.save(faculty).getId());
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty getFaculty(Long id) {
        logger.info("Попытка найти факультет с ID: {}", id);
        return facultyRepository.findById(id).orElse(null);
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Попытка изменить факультет: {}", faculty);
        return facultyRepository.save(faculty);
    }

    @Override
    public void removeFaculty(Long id) {
        facultyRepository.deleteById(id);
        logger.info("Факультет с ID {} успешно удален.", id);
    }

    public List<Faculty> findByColor(String color) {
        logger.info("Попытка найти факультет по цвету: {}", color);
        return facultyRepository
                .findAll()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Попытка найти студента по ID факультета: {}", facultyId);
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        if (faculty != null) {
            return faculty.getStudents();
        }
        return null;
    }

    //HW4.5
    @Override
    public String getLongestFacultyName() {
        logger.info("Was invoked method - getLongestFacultyName");
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }

    @Override
    public int getSum() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel() // Параллельная обработка для ускорения
                .reduce(0, Integer::sum);
    }
}
