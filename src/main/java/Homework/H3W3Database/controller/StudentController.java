package Homework.H3W3Database.controller;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;
import Homework.H3W3Database.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}") // GET http://localhost:8080/students/23
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping  // POST http://localhost:8080/students
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping   // PUT http://localhost:8080/students
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(student);
        if (updatedStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("{id}")  // DELETE http://localhost:8080/students/23
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    //нужно получить факультет студента
    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudentId(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }


    @GetMapping("/get-amount")//количество студентов
    public ResponseEntity<Integer> getAmountOfStudent() {
        return ResponseEntity.ok(studentService.getAmount());
    }

    @GetMapping("/get-average-age")//средний возраст студентов
    public ResponseEntity<Integer> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("/get-5-last-students")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        List<Student> lastFiveStudents = studentService.getLastFiveStudents();
        if (lastFiveStudents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lastFiveStudents);
    }
}

