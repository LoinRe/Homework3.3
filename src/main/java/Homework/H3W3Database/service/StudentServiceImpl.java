package Homework.H3W3Database.service;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;
import Homework.H3W3Database.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Создание нового студента: {}", student);
        student.setId(null);
        logger.info("Студент успешно создан. ID нового студента: {}", studentRepository.save(student).getId());
        return studentRepository.save(student);
    }

    @Override
    public Student getStudent(Long id) {
        logger.info("Поиск студента с ID: {}.", id);
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student updateStudent(Student student) {
        logger.info("Обновление студента: {}", student);
        return studentRepository.save(student);
    }

    @Override
    public void removeStudent(Long id) {
        logger.info("Запрос на удаление студента с ID {}.", id);
        studentRepository.deleteById(id);
        logger.info("Студент с ID {} успешно удален.", id);
    }

    public List<Student> findByAge(Integer age) {
        logger.info("Запрос на поиск студентов, у которых возраст {}.", age);
        return studentRepository
                .findAll()
                .stream()
                .filter(student -> Objects.equals(student.getAge(), age))
                .collect(Collectors.toList());
    }

    @Override
    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Запрос на поиск факультета для студента с ID {}.", studentId);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            return student.getFaculty();
        }
        return null;
    }

    // Реализация новых методов
    @Override
    public int getAmount() {
        return studentRepository.getAmountOfStudents();
    }

    @Override
    public int getAverageAge() {
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Student> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents();
    }


    //HW4.6
    @Override
    public void printStudentsNamesWithThreads() {
        List<Student> studentList = studentRepository.findAll();

        System.out.println(studentList.get(0).getName());
        System.out.println(studentList.get(1).getName());

        new Thread(() -> {
            System.out.println(studentList.get(2).getName());
            System.out.println(studentList.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(studentList.get(4).getName());
            System.out.println(studentList.get(5).getName());
        }).start();
    }


    @Override
    public void printStudentsNamesWithSynchronizedThreads() {
        List<Student> studentList = studentRepository.findAll();

        // Основной поток: первые два имени
        printStudentName(studentList);
        printStudentName(studentList);

        // Параллельный поток 1: третий и четвертый студенты
        new Thread(() -> {
            printStudentName(studentList);
            printStudentName(studentList);
        }).start();

        // Параллельный поток 2: пятый и шестой студенты
        new Thread(() -> {
            printStudentName(studentList);
            printStudentName(studentList);
        }).start();
    }

    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public synchronized void printStudentName(List<Student> studentList) {
        int currentIndex = count.getAndIncrement();
        System.out.println(studentList.get(currentIndex).getName() + " count " + currentIndex);
    }
}


