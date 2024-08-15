package com.example.learningcenter;

import com.example.learningcenter.annotations.DefaultTestConfiguration;
import com.example.learningcenterapi.domain.Option;
import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import com.example.learningcenterapi.domain.enumeration.QuestionType;
import com.example.learningcenterapi.dto.request.*;
import com.example.learningcenterapi.dto.request.update.CourseUpdateDTO;
import com.example.learningcenterapi.dto.request.update.TestUpdateDTO;
import com.example.learningcenterapi.dto.response.CourseResponseDTO;
import com.example.learningcenterapi.dto.response.TestAttemptDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.LessonMinimizedDTO;
import com.example.learningcenterapi.security.configuration.SecurityConfiguration;
import com.example.learningcenterapi.security.dto.AuthResponse;
import com.example.learningcenterapi.security.dto.LoginRequestDTO;
import com.example.learningcenterapi.service.LessonService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.learningcenterapi.domain.enumeration.QuestionComplexity.HARD;
import static com.example.learningcenterapi.domain.enumeration.QuestionComplexity.MEDIUM;
import static com.example.learningcenterapi.domain.enumeration.QuestionType.SINGLE_ANSWER;
import static com.example.learningcenterapi.domain.enumeration.TestCompletionStatus.PASSED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DefaultTestConfiguration
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
@ActiveProfiles("test")
class TestingE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LessonService lessonService;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        SimpleFilterProvider sfp = new SimpleFilterProvider();
        sfp.setFailOnUnknownId(false);
        mapper.setFilterProvider(sfp);
        objectMapper = mapper;
    }


    @Test
    void testUserPassesTest() throws Exception {
        // Step 1: Login as instructor to get JWT token
        String instructorJwt = performLogin("instructor.phantoms@khm.com", "OAEfX4wJoEU2Kqm");

        // Step 2: Create a course as instructor
        CourseResponseDTO courseDTO = createCourse(instructorJwt);

        // Step 3: Update lessons for the created course
        updateLessonsForCourse(courseDTO.getId(), instructorJwt);

        // Step 4: Extract lesson ID from the updated course DTO and create a test for that lesson
        long lessonId = getLessonIdByCourseId(courseDTO);
        TestResponseDTO testDTO = createTest(lessonId, instructorJwt);

        // Step 5: Add questions to the created test
        updateTestWithQuestions(testDTO.getId(), instructorJwt);

        // Step 6: Login as student to get JWT token
        String studentJwt = performLogin("student.phantoms@khm.com", "OAEfX4wJoEU2Kqm");

        // Step 7: Subscribe student to the course
        subscribeToCourse(studentJwt, courseDTO.getId());

        // Step 8: Complete the test as the student
        completeTest(testDTO.getId(), studentJwt);

        // Step 9: Retrieve the test result and verify the test was passed
        TestAttemptDTO testResult = getTestResult(testDTO.getId(), studentJwt);
        assertEquals(PASSED, testResult.getStatus());
    }

    private String performLogin(String email, String password) throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(response, AuthResponse.class);
        return authResponse.getAccessToken();
    }

    private void subscribeToCourse(String jwt, Long courseId) throws Exception {
        mockMvc.perform(post("/api/v1/courses/" + courseId + "/students")
                        .headers(getHttpHeaders(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private CourseResponseDTO createCourse(String jwt) throws Exception {
        CourseRequestDTO courseRequest = new CourseRequestDTO("Test Course", "Description");
        MvcResult result = mockMvc.perform(post("/api/v1/courses")
                        .headers(getHttpHeaders(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courseRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), CourseResponseDTO.class);
    }

    public void updateLessonsForCourse(Long courseId, String jwt) throws Exception {
        Set<LessonRequestDTO> lessons = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            LessonRequestDTO lesson = LessonRequestDTO.builder()
                    .title("Lesson " + i)
                    .content("Content for lesson " + i)
                    .orderIndex(i)
                    .build();
            lessons.add(lesson);
        }

        CourseUpdateDTO courseUpdateDTO = CourseUpdateDTO.builder()
                .lessons(lessons)
                .build();

        mockMvc.perform(put("/api/v1/courses/" + courseId + "/lessons")
                        .headers(getHttpHeaders(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(courseUpdateDTO)))
                .andExpect(status().isOk());
    }

    private TestResponseDTO createTest(Long lessonId, String jwt) throws Exception {
        TestRequestDTO testRequestDTO = TestRequestDTO.builder()
                .title("Test Title")
                .description("Test Description")
                .build();

        String responseContent = mockMvc.perform(post("/api/v1/lessons/" + lessonId + "/tests")
                        .headers(getHttpHeaders(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseContent, TestResponseDTO.class);
    }

    private void updateTestWithQuestions(Long testId, String jwt) throws Exception {
        Set<QuestionRequestDTO> questions = Set.of(
                createQuestionRequest(1, "What is Java?", MEDIUM, SINGLE_ANSWER),
                createQuestionRequest(2, "What is Spring?", HARD, SINGLE_ANSWER)
        );

        TestUpdateDTO testUpdateDTO = TestUpdateDTO.builder()
                .questions(questions)
                .build();

        mockMvc.perform(put("/api/v1/tests/" + testId + "/questions")
                        .headers(getHttpHeaders(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(testUpdateDTO)))
                .andExpect(status().isCreated());
    }

    private Long getLessonIdByCourseId(CourseResponseDTO courseDTO) {
        if (courseDTO == null || courseDTO.getId() == null) {
            return null;
        }
        List<LessonMinimizedDTO> lessons = lessonService.findAllLessonsByCourseId(courseDTO.getId());
        return !lessons.isEmpty() ? lessons.get(0).getId() : null;
    }


    private QuestionRequestDTO createQuestionRequest(int orderIndex, String description,
                                                     QuestionComplexity complexity, QuestionType type) {
        Set<OptionRequestDTO> options = Set.of(
                createOptionRequest(true),
                createOptionRequest(false)
        );

        return QuestionRequestDTO.builder()
                .orderIndex(orderIndex)
                .description(description)
                .questionComplexity(complexity)
                .questionType(type)
                .options(options)
                .build();
    }

    private OptionRequestDTO createOptionRequest(boolean isCorrect) {
        return OptionRequestDTO.builder()
                .content(UUID.randomUUID().toString().replace("-", ""))
                .correct(isCorrect)
                .build();
    }

    @Transactional
    void completeTest(Long testId, String jwt) throws Exception {
        com.example.learningcenterapi.domain.Test test = entityManager.find(com.example.learningcenterapi.domain.Test.class, testId);

        List<QuestionResultDTO> questionResults = test.getQuestions().stream()
                .map(question -> {
                    Set<Long> correctOptionIds = question.getOptions().stream()
                            .filter(Option::isCorrect)
                            .map(Option::getId)
                            .collect(Collectors.toSet());

                    return QuestionResultDTO.builder()
                            .questionId(question.getId())
                            .optionIds(correctOptionIds)
                            .build();
                })
                .collect(Collectors.toList());

        TestResultDTO testResultDTO = new TestResultDTO(questionResults);

        mockMvc.perform(post("/api/v1/tests/" + testId + "/complete")
                        .headers(getHttpHeaders(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testResultDTO)))
                .andExpect(status().isOk());
    }

    private TestAttemptDTO getTestResult(Long testId, String jwt) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/tests/" + testId + "/result")
                        .headers(getHttpHeaders(jwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), TestAttemptDTO.class);
    }

    private HttpHeaders getHttpHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        return headers;
    }
}
