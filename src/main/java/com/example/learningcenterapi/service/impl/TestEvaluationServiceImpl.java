package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.*;
import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import com.example.learningcenterapi.domain.enumeration.TestCompletionStatus;
import com.example.learningcenterapi.dto.request.QuestionResultDTO;
import com.example.learningcenterapi.dto.request.TestResultDTO;
import com.example.learningcenterapi.dto.response.TestAttemptDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.TestAttemptMapper;
import com.example.learningcenterapi.repository.TestAttemptRepository;
import com.example.learningcenterapi.repository.TestRepository;
import com.example.learningcenterapi.service.TestEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

import static com.example.learningcenterapi.domain.enumeration.QuestionType.SINGLE_ANSWER;
import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TestEvaluationServiceImpl implements TestEvaluationService {
    private final static int MAX_SCORE = 100;
    private final static int MIN_SCORE = 0;
    private final static int MIN_SUCCESS_SCORE = 80;

    private final TestAttemptRepository testAttemptRepository;
    private final TestRepository testRepository;
    private final TestAttemptMapper testAttemptMapper;

    @Override
    @Transactional(readOnly = true)
    public TestAttemptDTO findAttemptByTestAndStudent(Long testId, Long userId) {
        log.info("Finding test attempt for test with id: {} and student with id: {}", testId, userId);
        checkNull(testId, userId, "Test id", "User id");
        TestAttempt testAttempt = testAttemptRepository.findById_TestIdAndId_StudentId(testId, userId)
                .orElseThrow(() -> new SystemException("Student has not attempted this test yet", BAD_REQUEST));
        return testAttemptMapper.toDTO(testAttempt);
    }

    @Override
    public TestAttemptDTO saveTestEvaluationResult(Long testId, TestResultDTO testResultDTO, User user) {
        log.info("Saving test evaluation result for test with id: {}", testId);
        checkNull(testId, "Test id");
        Test test = findTestById(testId);
        checkTestAvailability(test);
        checkValidResults(test, testResultDTO);
        TestAttempt testAttempt = processResultsAndTestAttempt(testResultDTO, (Student) user, test);
        return testAttemptMapper.toDTO(testAttemptRepository.save(testAttempt));
    }

    public TestAttempt processResultsAndTestAttempt(TestResultDTO testResultDTO, Student student, Test test) {
        double userScore = calculateUserScore(test, testResultDTO);
        int totalTestScore = getMaxScore(test.getQuestions());
        double scorePercentage = (userScore / totalTestScore) * MAX_SCORE;
        TestCompletionStatus completionStatus = getTestCompletionStatus(scorePercentage);
        log.info("User score: {}, total test score: {}, score percentage: {}", userScore, totalTestScore, scorePercentage);
        return new TestAttempt(student, test, scorePercentage, completionStatus);
    }

    private double calculateUserScore(Test test, TestResultDTO testResultDTO) {
        Map<Long, Question> questionsMap = getQuestionsMap(test.getQuestions());
        double userScore = 0;

        for (QuestionResultDTO questionResult : testResultDTO.getQuestionResults()) {
            userScore += calculateQuestionScore(questionResult, questionsMap);
        }
        return userScore;
    }

    private double calculateQuestionScore(QuestionResultDTO questionResult, Map<Long, Question> questionsMap) {
        Question question = questionsMap.get(questionResult.getQuestionId());
        validateQuestionResult(question, questionResult);
        int scoreForRightAnswer = question.getQuestionComplexity().getValue();
        Set<Option> correctOptions = question.getOptions().stream()
                .filter(Option::isCorrect)
                .collect(toSet());
        return isCorrectAnswer(correctOptions, questionResult.getOptionIds()) ? scoreForRightAnswer : MIN_SCORE;
    }

    private TestCompletionStatus getTestCompletionStatus(double testGrade) {
        return testGrade >= MIN_SUCCESS_SCORE
                ? TestCompletionStatus.PASSED
                : TestCompletionStatus.FAILED;
    }

    private boolean isCorrectAnswer(Set<Option> correctOptions, Set<Long> answerIds) {
        if (correctOptions.size() != answerIds.size()) {
            return false;
        }
        return correctOptions.stream()
                .map(Option::getId)
                .allMatch(answerIds::contains);
    }

    private Map<Long, Question> getQuestionsMap(Set<Question> questions) {
        return questions.stream().collect(toMap(Question::getId, identity()));
    }

    private int getMaxScore(Set<Question> questions) {
        return questions.stream()
                .map(Question::getQuestionComplexity)
                .mapToInt(QuestionComplexity::getValue)
                .sum();
    }

    private void validateQuestionResult(Question question, QuestionResultDTO questionResult) {
        if (question == null) {
            throw new SystemException("Question with %d is not related to the test."
                    .formatted(questionResult.getQuestionId()), BAD_REQUEST);
        }
        if (question.getQuestionType() == SINGLE_ANSWER && questionResult.getOptionIds().size() > 1) {
            throw new SystemException("Wrong number of answers have been sent.", BAD_REQUEST);
        }
    }

    private Test findTestById(Long testId) {
        return testRepository.findById(testId).orElseThrow(() -> new SystemException(
                "Test with id: " + testId + " not found", NOT_FOUND));
    }

    private void checkTestAvailability(Test test) {
        if (!test.isAvailable()) {
            throw new SystemException("Test is not available", BAD_REQUEST);
        }
    }


    private void checkValidResults(Test test, TestResultDTO testResultDTO) {
        if (testResultDTO.getQuestionResults().size() != test.getQuestions().size()) {
            throw new SystemException("Wrong number of answers have been sent.", BAD_REQUEST);
        }
    }
}
