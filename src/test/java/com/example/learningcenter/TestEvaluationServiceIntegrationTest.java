package com.example.learningcenter;

import com.example.learningcenter.annotations.DefaultTestConfiguration;
import com.example.learningcenterapi.domain.*;
import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import com.example.learningcenterapi.domain.enumeration.QuestionType;
import com.example.learningcenterapi.dto.request.QuestionResultDTO;
import com.example.learningcenterapi.dto.request.TestResultDTO;
import com.example.learningcenterapi.dto.response.TestAttemptDTO;
import com.example.learningcenterapi.service.impl.TestEvaluationServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.learningcenter.util.EntityUtils.generateCourse;
import static com.example.learningcenter.util.EntityUtils.generateStudent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@DefaultTestConfiguration
@Transactional
@ActiveProfiles("test")
class TestEvaluationServiceIntegrationTest {

    @Autowired
    private TestEvaluationServiceImpl testEvaluationService;

    @Autowired
    private EntityManager entityManager;


    @Test
    void testSaveTestEvaluationResult_Passed() {
        Student student = generateStudent();
        com.example.learningcenterapi.domain.Test test = createTest();
        entityManager.persist(student);
        entityManager.flush();

        TestResultDTO testResultDTO = createTestResultDTO(true, test.getQuestions());

        TestAttemptDTO result = testEvaluationService.saveTestEvaluationResult(test.getId(), testResultDTO, student);

        assertNotNull(result);
        assertEquals("PASSED", result.getStatus().name());
        assertEquals(100.0, result.getGrade());
    }

    @Test
    void testSaveTestEvaluationResult_Failed() {
        Student student = generateStudent();
        com.example.learningcenterapi.domain.Test test = createTest();
        entityManager.persist(student);
        entityManager.flush();

        TestResultDTO testResultDTO = createTestResultDTO(false, test.getQuestions());

        TestAttemptDTO result = testEvaluationService.saveTestEvaluationResult(test.getId(), testResultDTO, student);

        assertNotNull(result);
        assertEquals("FAILED", result.getStatus().name());
        assertEquals(0.0, result.getGrade());
    }

    com.example.learningcenterapi.domain.Test createTest() {
        Course course = generateCourse();
        entityManager.persist(course);

        Lesson lesson = new Lesson();
        lesson.setTitle("Ма кум привіз з Італії ...");
        lesson.setOrderIndex(1);
        lesson.setContent("Купіть мені салямі...");
        lesson.setCourse(course);
        entityManager.persist(lesson);

        com.example.learningcenterapi.domain.Test test = new com.example.learningcenterapi.domain.Test();
        test.setTitle("Testium");
        test.setDescription("Uberinteresting ;)");
        test.setLesson(lesson);
        test.setAvailable(true);
        test.setCourse(course);
        entityManager.persist(test);

        Question question1 = new Question();
        question1.setQuestionComplexity(QuestionComplexity.MEDIUM);
        question1.setQuestionType(QuestionType.SINGLE_ANSWER);
        question1.setOrderIndex(1);
        question1.setDescription("dfsfdsf");
        test.addQuestion(question1);

        Option option1 = createOption(true);
        Option option2 = createOption(false);
        question1.addOption(option1);
        question1.addOption(option2);
        entityManager.persist(question1);

        Question question2 = new Question();
        question2.setQuestionComplexity(QuestionComplexity.HARD);
        question2.setQuestionType(QuestionType.SINGLE_ANSWER);
        question2.setOrderIndex(2);
        question2.setDescription("dfsfdsf");
        test.addQuestion(question2);

        Option option3 = createOption(true);
        Option option4 = createOption(false);
        question2.addOption(option3);
        question2.addOption(option4);
        entityManager.persist(question2);

        return test;
    }

    private Option createOption(boolean isCorrect) {
        Option option = new Option();
        option.setContent(UUID.randomUUID().toString().replace("-", ""));
        option.setCorrect(isCorrect);
        return option;
    }

    TestResultDTO createTestResultDTO(boolean isPassed, Set<Question> questions) {
        List<QuestionResultDTO> questionResults = new ArrayList<>();

        for (Question question: questions) {
            Set<Long> answerIds = question.getOptions()
                    .stream()
                    .filter(o -> o.isCorrect() == isPassed)
                    .map(Option::getId)
                    .collect(Collectors.toSet());
            QuestionResultDTO qDto = QuestionResultDTO.builder()
                    .questionId(question.getId())
                    .optionIds(answerIds)
                    .build();
            questionResults.add(qDto);
        }
        return new TestResultDTO(questionResults);
    }
}
