package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Homework;
import com.example.learningcenterapi.domain.Lesson;
import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.dto.request.HomeworkRequestDTO;
import com.example.learningcenterapi.dto.response.HomeworkResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.HomeworkMapper;
import com.example.learningcenterapi.repository.HomeworkRepository;
import com.example.learningcenterapi.repository.LessonRepository;
import com.example.learningcenterapi.repository.StudentRepository;
import com.example.learningcenterapi.service.BucketService;
import com.example.learningcenterapi.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HomeworkServiceImpl implements HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkMapper homeworkMapper;
    private final BucketService storageService;

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
        try {
            homeworkRepository.delete(homework);
            storageService.deleteFile(homework.getFilePath());
        } catch (Exception e) {
            log.error("Failed to delete homework with id: " + homeworkId, e);
            throw new SystemException("Failed deleting homework", e);
        }
    }

    @Override
    public HomeworkResponseDTO uploadHomeworkFile(MultipartFile file, Long studentId, Long lessonId) {
        log.info("Uploading homework for lesson with id: {} and student id: {}", lessonId, studentId);
        Student student = findStudentById(studentId);
        Lesson lesson = findLessonById(lessonId);
        String filePath = uploadHomeworkFile(student, lesson, file);
        Homework homework = new Homework();
        homework.setStudent(student);
        homework.setLesson(lesson);
        homework.setFilePath(filePath);
        Homework savedHomework;
        savedHomework = homeworkRepository.save(homework);
        log.info("Successful upload homework for lesson with id: {} and student id: {}", lessonId, studentId);
        return homeworkMapper.toResponseDTO(savedHomework);
    }

    @Override
    public String getHomeworkFileTemporalLink(Long lessonId, Long studentId, String identifier) {
        String fileKey = getFileKey(lessonId, studentId, identifier);
        try {
            return storageService.generateTemporaryLinkForReadingFile(fileKey);
        } catch (Exception e) {
            log.error("Failed to generate temporary link for lesson with id: {}, student id: {} and id: {}",
                    lessonId, studentId, identifier, e);
            throw new SystemException("Failed generating temporary link", e);
        }
    }

    @Override
    public byte[] getHomeworkFile(Long lessonId, Long studentId, String identifier) {
        try {
            return storageService.downloadFile(getFileKey(lessonId, studentId, identifier));
        } catch (Exception e) {
            log.error("Failed to download homework file for lesson with id: {}, student id: {} and identifier: {}",
                    lessonId, studentId, identifier, e);
            throw new SystemException("Failed to retrieve homework", e);
        }
    }

    private String uploadHomeworkFile(Student student, Lesson lesson, MultipartFile file) {
        String fileKey = getFileKey(lesson.getId(), student.getId(), file.getOriginalFilename());

        try {
            storageService.uploadFile(fileKey, file.getBytes());
        } catch (IOException e) {
            log.error("Failed to upload file for lesson with id: {}, student id: {}",
                    lesson.getId(), student.getId(), e);
            throw new SystemException("Failed to upload homework", e);
        }
        return fileKey;
    }

    private String getFileKey(Long lessonId, Long studentId, String identifier) {
        return "homeworks/lessons/%d/students/%d/%s"
                .formatted(lessonId, studentId, identifier);
    }

    private Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() ->
                new SystemException("Student with id: " + studentId + " not found.", BAD_REQUEST));
    }

    private Lesson findLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() ->
                new SystemException("Lesson with id: " + lessonId + " not found.", BAD_REQUEST));
    }

    private Homework findEntityById(Long homeworkId) {
        checkNull(homeworkId, "Homework id");
        return homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new SystemException("Homework with id: " + homeworkId + " not found", NOT_FOUND));
    }
}
