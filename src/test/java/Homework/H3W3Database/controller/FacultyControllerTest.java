package Homework.H3W3Database.controller;

import Homework.H3W3Database.models.Faculty;
import Homework.H3W3Database.models.Student;
import Homework.H3W3Database.service.FacultyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // !!!!
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;  // !!!!
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    //get
    @Test
    public void testGetFaculty() throws Exception {
        // Arrange
        Faculty faculty = new Faculty("Griff", "brown");
        when(facultyService.getFaculty(anyLong())).thenReturn(faculty);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/1"))
                .andDo(print())

                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Griff"))
                .andExpect(jsonPath("$.color").value("brown"));
    }

    //post
    @Test
    public void testCreateFaculty() throws Exception {
        // Arrange    Given
        final Faculty faculty = new Faculty("Griff", "brown");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        ObjectMapper objectMapper = new ObjectMapper();

        // Act     When
        mockMvc.perform(MockMvcRequestBuilders.post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"Griff\",\"color\":\"brown\"}"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Griff"))
                .andExpect(jsonPath("$.color").value("brown"));
    }

    //put
    @Test
    public void testUpdateFaculty() throws Exception {
        // Arrange    Given
        final Faculty faculty = new Faculty("Griff", "brown");
        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

        ObjectMapper objectMapper = new ObjectMapper();

        // Act     When
        mockMvc.perform(MockMvcRequestBuilders.put("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"name\":\"Griff\",\"color\":\"brown\"}"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Griff"))
                .andExpect(jsonPath("$.color").value("brown"));
    }

    //delete
    @Test
    public void testDeleteFaculty() throws Exception {
        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/faculties/1"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk()); //метод void, можем только статус проверить
    }

    //getStudentsByF
    @Test
    public void testGetStudentsByFacultyId() throws Exception {
        // Успешный случай: студенты есть
        // Arrange
        Faculty faculty = new Faculty("Griff", "brown");
        Student student1 = new Student("Гарри Поттер", 15);
        student1.setFaculty(faculty); // Устанавливаем связь
        Student student2 = new Student("Гермиона Грейнджер", 15);
        student2.setFaculty(faculty); // Устанавливаем связь
        List<Student> students = List.of(student1, student2);

        // Мокируем сервис, чтобы возвращал студентов для любого facultyId
        when(facultyService.getStudentsByFacultyId(anyLong())).thenReturn(students);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/1/students")) // ID не важен из-за anyLong()
                .andDo(print())
                //Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Гермиона Грейнджер"));

        // Случай: студентов нет
        when(facultyService.getStudentsByFacultyId(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/888/students"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

