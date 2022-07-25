package ee.kull.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.kull.sms.entity.Student;
import ee.kull.sms.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private StudentService service;



    @Test
    void shouldGetListOfStudents() throws Exception {
        List<Student> list = new ArrayList<>();
        list.add(new Student());
        given(service.getAllStudents()).willReturn(list);
        this.mockMvc.perform(get("/students")).andExpect(status().isOk());
    }


    @Test
    void saveStudent() throws Exception {

        given(service.saveStudent(any(Student.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldGetStudentById() throws Exception {
        Student student = Student.builder()
                .id(1l)
                .firstName("Svetlana")
                .lastName("Len")
                .email("len@test.com")
                .build();
        given(service.getStudentById(student.getId())).willReturn(Optional.of(student).get());

        this.mockMvc.perform(get("/students/edit/{id}", student.getId())).andExpect(status().isOk());
    }

    @Test
    void updateStudentIfExist() throws Exception {
        Student student = Student.builder()
                .id(1l)
                .firstName("Svetlana")
                .lastName("Len")
                .email("len@test.com")
                .build();

        given(service.getStudentById(student.getId())).willReturn(Optional.of(student).get());
        given(service.updateStudent(any(Student.class))).willAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteStudentifExist() throws Exception {
        Student student = Student.builder()
                .id(1l)
                .firstName("Svetlana")
                .lastName("Len")
                .email("len@test.com")
                .build();

        given(service.getStudentById(student.getId())).willReturn(Optional.of(student).get());
        doNothing().when(service).deleteStudentById(student.getId());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/students/{id}", student.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());


    }
}