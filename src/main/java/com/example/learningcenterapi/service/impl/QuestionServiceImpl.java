package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Question;
import com.example.learningcenterapi.domain.Test;
import com.example.learningcenterapi.dto.request.OptionRequestDTO;
import com.example.learningcenterapi.dto.request.QuestionRequestDTO;
import com.example.learningcenterapi.dto.request.update.TestUpdateDTO;
import com.example.learningcenterapi.dto.response.QuestionResponseDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.OptionMapper;
import com.example.learningcenterapi.mapper.QuestionMapper;
import com.example.learningcenterapi.mapper.TestMapper;
import com.example.learningcenterapi.repository.QuestionRepository;
import com.example.learningcenterapi.repository.TestRepository;
import com.example.learningcenterapi.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

import static com.example.learningcenterapi.domain.enumeration.QuestionType.MULTI_ANSWER;
import static com.example.learningcenterapi.domain.enumeration.QuestionType.SINGLE_ANSWER;
import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final TestMapper testMapper;
    private final QuestionMapper questionMapper;
    private final OptionMapper optionMapper;

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDTO findById(Long questionId) {
        log.info("Method findById called with id: {}", questionId);
        return questionMapper.toResponseDTO(findEntityById(questionId));
    }

    @Override
    public QuestionResponseDTO updateById(QuestionRequestDTO questionDTO, Long questionId) {
        log.info("Method updateById called with questionDTO: {} and id: {}", questionDTO, questionId);
        checkNull(questionId, "Question id");
        Question question = findEntityById(questionId);
        setUpdatableFields(question, questionDTO);
        return questionMapper.toResponseDTO(question);
    }

    @Override
    public TestResponseDTO updateQuestionsForTest(TestUpdateDTO testDTO, Long testId) {
        log.info("Updating questions for test by id: {}", testId);
        Test test = findTestById(testId);
        test.getQuestions().clear();
        addQuestionsToTest(testDTO.getQuestions(), test);
        return testMapper.toResponseDTO(test);
    }

    @Override
    public TestResponseDTO addQuestionToTest(QuestionRequestDTO questionDTO, Long testId) {
        log.info("Adding question to test by id: {}", testId);
        if (questionDTO.getId() != null) {
            throw new SystemException("Question id must be null", BAD_REQUEST);
        }
        validateQuestion(questionDTO);
        Test test = findTestById(testId);
        test.addQuestion(questionMapper.fromRequestDTO(questionDTO));
        return testMapper.toResponseDTO(test);
    }

    @Override
    public void deleteById(Long questionId) {
        log.info("Method deleteById called with id: {}", questionId);
        Question question = findEntityById(questionId);
        question.getTest().removeQuestion(question);
    }

    private void addQuestionsToTest(Set<QuestionRequestDTO> questions, Test test) {
        for (QuestionRequestDTO questionDTO : questions) {
            validateQuestion(questionDTO);
            Question question;
            if (Objects.isNull(questionDTO.getId())) {
                question = questionMapper.fromRequestDTO(questionDTO);
            } else {
                question = findEntityById(questionDTO.getId());
                setUpdatableFields(question, questionDTO);
            }
            test.addQuestion(question);
        }
    }

    private void validateQuestion(QuestionRequestDTO question) {
        int correctOptions = question.getOptions().stream()
                .filter(OptionRequestDTO::isCorrect)
                .toList()
                .size();
        if (question.getQuestionType() == MULTI_ANSWER && correctOptions < 2) {
            throw new SystemException("Multi answer question must have at least 2 correct options", BAD_REQUEST);
        } else if (question.getQuestionType() == SINGLE_ANSWER && correctOptions != 1) {
            throw new SystemException("Single answer question must have exactly 1 correct option", BAD_REQUEST);
        } else if (correctOptions == 0) {
            throw new SystemException("Question must have at least 1 correct option", BAD_REQUEST);
        }
    }

    private void setUpdatableFields(Question question, QuestionRequestDTO questionDTO) {
        question.setDescription(questionDTO.getDescription());
        question.setOrderIndex(questionDTO.getOrderIndex());
        question.setQuestionType(questionDTO.getQuestionType());
        question.setQuestionComplexity(questionDTO.getQuestionComplexity());
        question.setOptions(optionMapper.fromRequestDTOToSet(questionDTO.getOptions()));
    }

    private Test findTestById(Long testId) {
        return testRepository.findById(testId).orElseThrow(
                () -> new SystemException("Test not found with id: " + testId, NOT_FOUND));
    }

    private Question findEntityById(Long questionId) {
        checkNull(questionId, "Question id");
        return questionRepository.findById(questionId).orElseThrow(
                () -> new SystemException("Question not found with id: " + questionId + "", NOT_FOUND));
    }
}
