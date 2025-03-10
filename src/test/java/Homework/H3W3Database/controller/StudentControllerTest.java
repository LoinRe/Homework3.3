package Homework.H3W3Database.controller;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;
import Homework.H3W3Database.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put; //для 2 варианта теста put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    //get
    @Test
    public void testGetStudent() throws Exception {
        Student student = new Student();
        student.setName("Draco Malfoy");

        when(studentService.getStudent(anyLong())).thenReturn(student);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Draco Malfoy"));
    }

    //post
    @Test
    public void testCreateStudent() throws Exception {
        // Arrange
        final Student student = new Student("Draco", 15);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

//        ObjectMapper objectMapper = new ObjectMapper(); // Не нужен тут?

        // Act     When
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"Draco\",\"age\":15}"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Draco"))
                .andExpect(jsonPath("$.age").value(15));
    }

    //put
    @Test
    public void testUpdateStudent() throws Exception {
        // Arrange
        final Student student = new Student("Draco", 15);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

//        ObjectMapper objectMapper = new ObjectMapper(); // Не нужен тут?

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"Draco\",\"age\":15}"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Draco"))
                .andExpect(jsonPath("$.age").value(15));
    }

    //put2
    @Test
    public void testUpdateStudent2() throws Exception {
        // Arrange
        Student student = new Student("Draco", 15);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(student);

        // Act
        mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Draco"))
                .andExpect(jsonPath("$.age").value(15));
    }

    //delete
    @Test
    public void testDeleteStudent() throws Exception {
        // Arrange
        doNothing().when(studentService).removeStudent(anyLong()); // Явно указывает Mockito, что метод removeStudent() не должен делать ничего (актуально для void-методов)

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk()); //метод void, можем только статус проверить
    }

    //getFacultyBySt
    @Test
    public void testGetFacultyByStudentId() throws Exception {
        // Успешный случай: факультет есть
        // Arrange
        Faculty faculty = new Faculty("Griff", "red");
        // Мокируем сервис, чтобы возвращал факультет для любого studentId
        when(studentService.getFacultyByStudentId(anyLong())).thenReturn(faculty);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1/faculty")) // ID не важен из-за anyLong()
                .andDo(print())
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Griff"))
                .andExpect(jsonPath("$.color").value("red"));

        // Случай: факультет не наййден
        when(studentService.getFacultyByStudentId(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/1/faculty"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    //Новые тесты
    // Тест для GET /students/get-amount
    @Test
    public void testGetAmountOfStudents() throws Exception {
        when(studentService.getAmount()).thenReturn(42); // Предположим, что в базе 42 студента

        mockMvc.perform(MockMvcRequestBuilders.get("/students/get-amount"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(42)); // Проверяем, что возвращается число 42
    }

    // Тест для GET /students/get-average-age
    @Test
    public void testGetAverageAge() throws Exception {
        when(studentService.getAverageAge()).thenReturn(20); // Предположим, средний возраст 20

        mockMvc.perform(MockMvcRequestBuilders.get("/students/get-average-age"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(20)); // Проверяем, что возвращается число 20
    }

    // Тест для GET /students/get-5-last-students (успешный случай)
    @Test
    public void testGetLastFiveStudentsSuccess() throws Exception {
        Student student1 = new Student("Harry Potter", 17);
        Student student2 = new Student("Hermione Granger", 18);
        List<Student> lastFiveStudents = List.of(student1, student2); // Меньше 5 для примера
        when(studentService.getLastFiveStudents()).thenReturn(lastFiveStudents);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/get-5-last-students"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)) // Проверяем длину списка
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[0].age").value(17))
                .andExpect(jsonPath("$[1].name").value("Hermione Granger"))
                .andExpect(jsonPath("$[1].age").value(18));
    }

    // Тест для GET /students/get-5-last-students (случай, когда студентов нет)
    @Test
    public void testGetLastFiveStudentsEmpty() throws Exception {
        // Arrange
        when(studentService.getLastFiveStudents()).thenReturn(Collections.emptyList());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/students/get-5-last-students"))
                .andDo(print())
                // Assert
                .andExpect(status().isNoContent());
    }
}
