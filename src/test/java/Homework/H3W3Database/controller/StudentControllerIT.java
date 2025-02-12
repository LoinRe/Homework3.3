package Homework.H3W3Database.controller;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // Get
    @Test
    public void testGetStudent() {
        Student student = testRestTemplate.postForEntity("http://localhost:" + port + "/students", new Student("Hermione Granger", 15), Student.class).getBody();

        ResponseEntity<Student> response = testRestTemplate.getForEntity("http://localhost:" + port + "/students/" + student.getId(), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAge()).isEqualTo(15);
    }

    // Post
    @Test
    public void testCreateStudent() {
        Student student = new Student("Harry Potter", 15);

        ResponseEntity<Student> response = testRestTemplate.postForEntity("http://localhost:" + port + "/students", student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
    }

    // Put
    @Test
    public void testUpdateStudent() {
        Student student = testRestTemplate.postForEntity("http://localhost:" + port + "/students", new Student("Ron Weasley", 14), Student.class).getBody();

        student.setAge(15);
        testRestTemplate.put("http://localhost:" + port + "/students", student);

        ResponseEntity<Student> updatedResponse = testRestTemplate.getForEntity("http://localhost:" + port + "/students/" + student.getId(), Student.class);

        assertThat(updatedResponse.getBody().getAge()).isEqualTo(15);
    }

    // Delete
    @Test
    public void testDeleteStudent() {
        Student student = testRestTemplate.postForEntity("http://localhost:" + port + "/students", new Student("Draco Malfoy", 15), Student.class).getBody();

        testRestTemplate.delete("http://localhost:" + port + "/students/" + student.getId());

        ResponseEntity<Student> response = testRestTemplate.getForEntity("http://localhost:" + port + "/students/" + student.getId(), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // Получение факультета студента
    @Test
    public void testGetStudentFaculty() {
        Student student = testRestTemplate.postForEntity("http://localhost:" + port + "/students", new Student("Neville Longbottom", 15), Student.class).getBody();

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity("http://localhost:" + port + "/students/" + student.getId() + "/faculty", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
