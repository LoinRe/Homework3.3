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

import static org.assertj.core.api.Assertions.assertThat; //!!!


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // Get
    @Test
    public void testGetFaculty() {
        //Arrange
        Faculty newFaculty = testRestTemplate.postForEntity("http://localhost:" + port + "/faculties", new Faculty("Griffindor", "brown"), Faculty.class).getBody();
        //Act
        ResponseEntity<Faculty> facultyResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/faculties/" + newFaculty.getId(), Faculty.class);
        //Assert
        assertThat(facultyResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty faculty = facultyResponseEntity.getBody();
        assertThat(faculty.getColor()).isEqualTo("brown");
        assertThat(faculty.getName()).isEqualTo("Griffindor");
    }

    // Post
    @Test //А так можно написать или корректнее вариант выше?
    public void testCreateFaculty() {
        Faculty newFaculty = new Faculty("Griffindor", "red");

        ResponseEntity<Faculty> response = testRestTemplate.postForEntity("http://localhost:" + port + "/faculties", newFaculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty createdFaculty = response.getBody();
        assertThat(createdFaculty).isNotNull();
        assertThat(createdFaculty.getName()).isEqualTo("Griffindor");
        assertThat(createdFaculty.getColor()).isEqualTo("red");
    }

    //А так можно написать или корректнее вариант выше?
//    @Test
//    public void testCreateFaculty() {
//        Faculty faculty = new Faculty("Griffindor", "red");
//
//        ResponseEntity<Faculty> response = testRestTemplate.postForEntity("http://localhost:" + port + "/faculties", faculty, Faculty.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().getName()).isEqualTo("Griffindor");
//        assertThat(response.getBody().getColor()).isEqualTo("red");
//    }

    // Post
    @Test
    public void testUpdateFaculty() {
        Faculty faculty = testRestTemplate.postForEntity("http://localhost:" + port + "/faculties", new Faculty("Slytherin", "green"), Faculty.class).getBody();

        faculty.setColor("silver");
        testRestTemplate.put("http://localhost:" + port + "/faculties", faculty);

        ResponseEntity<Faculty> updatedResponse = testRestTemplate.getForEntity("http://localhost:" + port + "/faculties/" + faculty.getId(), Faculty.class);

        assertThat(updatedResponse.getBody().getColor()).isEqualTo("silver");
    }

    // Delete
    @Test
    public void testDeleteFaculty() {
        Faculty faculty = testRestTemplate.postForEntity("http://localhost:" + port + "/faculties", new Faculty("Hufflepuff", "yellow"), Faculty.class).getBody();

        testRestTemplate.delete("http://localhost:" + port + "/faculties/" + faculty.getId());

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity("http://localhost:" + port + "/faculties/" + faculty.getId(), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // Получение студентов факультета
    @Test
    public void testGetFacultyStudents() {
        // Создаем факультет
        Faculty faculty = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/faculties", new Faculty("Test Faculty", "white"), Faculty.class).getBody();

        // Создаем студента и привязываем к факультету
        Student student = new Student("Test Student", 20);
        student.setFaculty(faculty);
        testRestTemplate.postForEntity("http://localhost:" + port + "/students", student, Student.class);

        // Получаем студентов факультета
        ResponseEntity<Student[]> response = testRestTemplate.getForEntity("http://localhost:" + port + "/faculties/" + faculty.getId() + "/students", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1); // Теперь список не пуст
    }
}


