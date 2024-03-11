package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Homework;
import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.HomeworkRequestDTO;
import com.example.learningcenterapi.dto.response.HomeworkResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.HomeworkMapper;
import com.example.learningcenterapi.repository.HomeworkRepository;
import com.example.learningcenterapi.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;

    @Override
    @Transactional(readOnly = true)
    public HomeworkResponseDTO findById(Long homeworkId) {
        log.info("Finding homework with id: {}", homeworkId);
        return homeworkMapper.toResponseDTO(findEntityById(homeworkId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeworkResponseDTO> findByUserId(Long userId) {
        log.info("Finding all homeworks by user id: {}", userId);
        List<Homework> homeworks = homeworkRepository.findByStudentId(userId);
        return homeworkMapper.toResponseDTOList(homeworks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HomeworkResponseDTO> findByUserAndCourseId(Long userId, Long courseId) {
        log.info("Finding all homeworks by user id: {} and course id: {}", userId, courseId);
        checkNull(userId, courseId, "User id", "Course id");
        List<Homework> homeworks = homeworkRepository.findByStudentIdAndCourseId(userId, courseId);
        return homeworkMapper.toResponseDTOList(homeworks);
    }

    @Override
    public HomeworkResponseDTO updateById(HomeworkRequestDTO homeworkDTO, Long homeworkId) {
        log.info("Updating homework with id: {}", homeworkId);
        checkNull(homeworkId, "Homework id");
        Homework homework = findEntityById(homeworkId);
        homework.setFilePath(homeworkDTO.getFilePath());
        return homeworkMapper.toResponseDTO(homework);
    }

    @Override
    public void deleteById(Long homeworkId) {
        log.info("Deleting homework with id: {}", homeworkId);
        Homework homework = findEntityById(homeworkId);
        homeworkRepository.delete(homework);
    }

    @Override
    public HomeworkResponseDTO uploadHomeworkFile(MultipartFile file, User user, Long lessonId) {
        return null;
    }

    @Override
    public byte[] getHomeworkFile(Long homeworkId, Long userId) {
        Homework homework = findEntityById(homeworkId);
        return null;
    }

    private Homework findEntityById(Long homeworkId) {
        checkNull(homeworkId, "Homework id");
        return homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new SystemException("Homework with id: " + homeworkId + " not found", NOT_FOUND));
    }
}
