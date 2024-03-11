package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Course;
import com.example.learningcenterapi.domain.Instructor;
import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.CourseRequestDTO;
import com.example.learningcenterapi.dto.response.CourseResponseDTO;
import com.example.learningcenterapi.dto.response.CourseStatusDTO;
import com.example.learningcenterapi.dto.response.minimized.CourseMinimizedDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.CourseMapper;
import com.example.learningcenterapi.repository.CourseRepository;
import com.example.learningcenterapi.repository.StudentRepository;
import com.example.learningcenterapi.repository.SubmissionRepository;
import com.example.learningcenterapi.repository.UserRepository;
import com.example.learningcenterapi.service.InstructorService;
import com.example.learningcenterapi.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Transactional
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InstructorService instructorService;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private CourseResponseDTO courseResponseDTO;
    private CourseMinimizedDTO courseMinimizedDTO;
    private CourseRequestDTO courseRequestDTO;
    private User user;
    private Instructor instructor;
    private Student student;

    @BeforeEach
    void setUp() {
        // Initialize common objects and mock responses here
    }

    @Test
    @DisplayName("findbyid returns correct courseresponsedto for valid course id")
    void testFindByIdReturnsCourseResponseDTOForValidId() {
        // Arrange
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(courseMapper.toResponseDTO(any(Course.class))).thenReturn(courseResponseDTO);

        // Act
        CourseResponseDTO result = courseService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(courseResponseDTO, result);
        verify(courseRepository).findById(1L);
        verify(courseMapper).toResponseDTO(course);
    }

    @Test
    @DisplayName("findall returns a page of courseminimizeddto")
    void testFindAllReturnsPageOfCourseMinimizedDTO() {
        // Arrange
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course));
        when(courseRepository.findAll(any(PageRequest.class))).thenReturn(coursePage);
        when(courseMapper.toMinimizedDTO(any(Course.class))).thenReturn(courseMinimizedDTO);

        // Act
        Page<CourseMinimizedDTO> result = courseService.findAll(PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(courseMinimizedDTO, result.getContent().get(0));
        verify(courseRepository).findAll(any(PageRequest.class));
        verify(courseMapper, times(coursePage.getContent().size())).toMinimizedDTO(any(Course.class));
    }

    @Test
    @DisplayName("findallbystudentid returns list of courseminimizeddto for valid student id")
    void testFindAllByStudentIdReturnsListOfCourseMinimizedDTO() {
        // Arrange
        when(courseRepository.findAllByStudentsId(any(Long.class))).thenReturn(Collections.singletonList(course));
        when(courseMapper.toMinimizedDTOList(anyList())).thenReturn(Collections.singletonList(courseMinimizedDTO));

        // Act
        List<CourseMinimizedDTO> result = courseService.findAllByStudentId(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(courseMinimizedDTO, result.get(0));
        verify(courseRepository).findAllByStudentsId(1L);
        verify(courseMapper).toMinimizedDTOList(Collections.singletonList(course));
    }

    @Test
    @DisplayName("findallbyinstructorid returns list of courseminimizeddto for valid instructor id")
    void testFindAllByInstructorIdReturnsListOfCourseMinimizedDTO() {
        // Arrange
        when(courseRepository.findAllByInstructorsId(any(Long.class))).thenReturn(Collections.singletonList(course));
        when(courseMapper.toMinimizedDTOList(anyList())).thenReturn(Collections.singletonList(courseMinimizedDTO));

        // Act
        List<CourseMinimizedDTO> result = courseService.findAllByInstructorId(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(courseMinimizedDTO, result.get(0));
        verify(courseRepository).findAllByInstructorsId(1L);
        verify(courseMapper).toMinimizedDTOList(Collections.singletonList(course));
    }

    @Test
    @DisplayName("findmycourses returns empty list for user not being an instructor or student")
    void testFindMyCoursesReturnsEmptyListForNonInstructorOrStudent() {
        // Arrange
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        // Act
        List<CourseMinimizedDTO> result = courseService.findMyCourses(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("getusercoursestatus throws exception when student not related to course")
    void testGetUserCourseStatusThrowsExceptionForUnrelatedStudent() {
        // Arrange
        when(studentRepository.existsByIdAndCoursesId(any(Long.class), any(Long.class))).thenReturn(false);

        // Act & Assert
        assertThrows(SystemException.class, () -> courseService.getUserCourseStatus(1L, 1L));
        verify(studentRepository).existsByIdAndCoursesId(1L, 1L);
    }

    @Test
    @DisplayName("save creates and returns courseresponsedto for valid courserequestdto and instructor userid")
    void testSaveCreatesAndReturnsCourseResponseDTOForInstructor() {
        // Arrange
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponseDTO(any(Course.class))).thenReturn(courseResponseDTO);

        // Act
        CourseResponseDTO result = courseService.save(courseRequestDTO, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(courseResponseDTO, result);
        verify(userRepository).findById(1L);
        verify(courseRepository).save(any(Course.class));
        verify(courseMapper).toResponseDTO(course);
    }

    @Test
    @DisplayName("updatebyid updates and returns courseresponsedto for valid courserequestdto and courseid")
    void testUpdateByIdUpdatesAndReturnsCourseResponseDTO() {
        // Arrange
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(courseMapper.toResponseDTO(any(Course.class))).thenReturn(courseResponseDTO);

        // Act
        CourseResponseDTO result = courseService.updateById(courseRequestDTO, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(courseResponseDTO, result);
        verify(courseRepository).findById(1L);
        verify(courseMapper).toResponseDTO(course);
    }

    @Test
    @DisplayName("addstudenttocourse adds student to course successfully")
    void testAddStudentToCourseAddsStudentSuccessfully() {
        // Arrange
        when(studentService.findById(any(Long.class))).thenReturn(student);
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));

        // Act
        courseService.addStudentToCourse(1L, 1L);

        // Assert
        assertTrue(student.getCourses().contains(course));
        verify(studentService).findById(1L);
        verify(courseRepository).findById(1L);
    }

    @Test
    @DisplayName("addinstructortocourse adds instructor to course successfully")
    void testAddInstructorToCourseAddsInstructorSuccessfully() {
        // Arrange
        when(instructorService.findById(any(Long.class))).thenReturn(instructor);
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));

        // Act
        courseService.addInstructorToCourse(1L, 1L);

        // Assert
        assertTrue(instructor.getCourses().contains(course));
        verify(instructorService).findById(1L);
        verify(courseRepository).findById(1L);
    }

    @Test
    @DisplayName("deletebyid deletes a course successfully")
    void testDeleteByIdDeletesCourseSuccessfully() {
        // Arrange
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(any(Course.class));

        // Act
        courseService.deleteById(1L);

        // Assert
        verify(courseRepository).findById(1L);
        verify(courseRepository).delete(course);
    }

    @Test
    @DisplayName("deleteinstructorfromcourse removes instructor from course successfully")
    void testDeleteInstructorFromCourseRemovesInstructorSuccessfully() {
        // Arrange
        when(instructorService.findById(any(Long.class))).thenReturn(instructor);
        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));

        // Act
        courseService.deleteInstructorFromCourse(1L, 1L);

        // Assert
        assertFalse(instructor.getCourses().contains(course));
        verify(instructorService).findById(1L);
        verify(courseRepository).findById(1L);
    }
}
