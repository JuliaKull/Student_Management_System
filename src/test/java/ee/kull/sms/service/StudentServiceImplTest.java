package ee.kull.sms.service;

import ee.kull.sms.entity.Student;
import ee.kull.sms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StudentServiceImplTest {

    @Mock
    StudentRepository repository;

    @InjectMocks
    StudentServiceImpl service;

    @Test
    void getAllStudentsFromRepository() {

        List<Student> list = new ArrayList<>();
        list.add(new Student());
        given(repository.findAll()).willReturn(list);

        List<Student> expectedList = service.getAllStudents();

        assertEquals(expectedList, list);
        verify(repository).findAll();
    }

    @Test
    void saveStudentAndReturnFromRepository() {

        Student student = Student.builder()
                .id(1l)
                .firstName("Nina")
                .lastName("Loseva")
                .email("nina@gmail.com")
                .build();

        when(repository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);
        Student createdStudent = service.saveStudent(student);

        assertThat(createdStudent.getFirstName()).isSameAs(student.getFirstName());
        assertThat(createdStudent.getLastName()).isSameAs(student.getLastName());
        assertThat(createdStudent.getEmail()).isSameAs(student.getEmail());

        verify(repository).save(student);
    }

    @Test
    void getStudentByIdIfExist() {
        Student student = Student.builder()
                .id(1l)
                .firstName("TestName")
                .build();
        when(repository.findById(student.getId())).thenReturn(Optional.of(student));

        Student expectedStudent = service.getStudentById(student.getId());

        assertThat(expectedStudent).isSameAs(student);

        verify(repository).findById(student.getId());
    }


    @Test
    void whenGivenIdShouldUpdateStudentIfExist() {
        Student student = Student.builder()
                .id(1l)
                .firstName("Test")
                .build();

        when(repository.findById(student.getId())).thenReturn(Optional.of(student));

        Student studentUpdated=service.getStudentById(student.getId());
        studentUpdated.setFirstName("New Test");

        given(repository.findById(student.getId())).willReturn(Optional.of(student));

        service.updateStudent(studentUpdated);
        verify(repository).save(studentUpdated);
        verify(repository).findById(student.getId());

    }

    @Test
    void findStudentByIdAndDelete() {
        Student student = Student.builder()
                .id(1l)
                .firstName("Nina")
                .build();

        when(repository.findById(student.getId())).thenReturn(Optional.of(student));

        service.deleteStudentById(student.getId());

        verify(repository).deleteById(student.getId());
    }


    @Test
    void shouldThrowExceptionWhenStudentDoesntExist() {
        Student student = Student.builder()
                .id(100l)
                .firstName("TestName")
                .build();
        given(repository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        repository.deleteById(student.getId());
    }
}