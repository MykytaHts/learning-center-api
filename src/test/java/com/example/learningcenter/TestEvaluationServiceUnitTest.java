package com.example.learningcenter;

import com.example.learningcenterapi.domain.Option;
import com.example.learningcenterapi.domain.Question;
import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.domain.TestAttempt;
import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import com.example.learningcenterapi.domain.enumeration.QuestionType;
import com.example.learningcenterapi.domain.enumeration.TestCompletionStatus;
import com.example.learningcenterapi.dto.request.QuestionResultDTO;
import com.example.learningcenterapi.dto.request.TestResultDTO;
import com.example.learningcenterapi.service.impl.TestEvaluationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class TestEvaluationServiceUnitTest {
    @InjectMocks
    private TestEvaluationServiceImpl testEvaluationService;

    @Test
    void testSaveTestEvaluationResult_Passed() {
        com.example.learningcenterapi.domain.Test test = createTest();
        Student student = createStudent();
        TestResultDTO testResultDTO = createTestResultDTO(true);

        TestAttempt result = testEvaluationService.processResultsAndTestAttempt(testResultDTO, student, test);

        assertNotNull(result);
        assertEquals(TestCompletionStatus.PASSED, result.getStatus());
    }

    @Test
    void testSaveTestEvaluationResult_Failed() {
        com.example.learningcenterapi.domain.Test test = createTest();
        Student student = createStudent();
        TestResultDTO testResultDTO = createTestResultDTO(false);

        TestAttempt result = testEvaluationService.processResultsAndTestAttempt(testResultDTO, student, test);

        assertNotNull(result);
        assertEquals(TestCompletionStatus.FAILED, result.getStatus());
    }

    private com.example.learningcenterapi.domain.Test createTest() {
        com.example.learningcenterapi.domain.Test test = new com.example.learningcenterapi.domain.Test();
        test.setId(1L);
        test.setAvailable(true);

        Question question1 = new Question();
        question1.setId(1L);
        question1.setQuestionComplexity(QuestionComplexity.MEDIUM);
        question1.setQuestionType(QuestionType.SINGLE_ANSWER);
        question1.setOptions(Set.of(createOption(1L, true), createOption(2L, false)));

        Question question2 = new Question();
        question2.setId(2L);
        question2.setQuestionComplexity(QuestionComplexity.HARD);
        question2.setQuestionType(QuestionType.SINGLE_ANSWER);
        question2.setOptions(Set.of(createOption(3L, true), createOption(4L, false)));

        test.setQuestions(List.of(question1, question2));
        return test;
    }

    private Option createOption(Long id, boolean isCorrect) {
        Option option = new Option();
        option.setId(id);
        option.setContent(UUID.randomUUID().toString().replace("-", ""));
        option.setCorrect(isCorrect);
        return option;
    }

    private Student createStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        return student;
    }

    private TestResultDTO createTestResultDTO(boolean isPassed) {
        QuestionResultDTO questionResult1 = new QuestionResultDTO(1L, isPassed ? Set.of(1L) : Set.of(2L));
        QuestionResultDTO questionResult2 = new QuestionResultDTO(2L, isPassed ? Set.of(3L) : Set.of(4L));
        return new TestResultDTO(List.of(questionResult1, questionResult2));
    }
}
