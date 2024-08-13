package com.example.learningcenterapi.security.validator;

import com.example.learningcenterapi.domain.*;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.exception.UserAccessRestrictedException;
import com.example.learningcenterapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccessValidator implements Validator {
    private final CourseRepository courseRepository;
    private final FeedbackRepository feedbackRepository;
    private final HomeworkRepository homeworkRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;


    @Override
    public boolean testAccessById(Long testId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByTestsIdAndInstructorsId(testId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the test.");
            }
            return true;
        }
        else if (principal instanceof Student student) {
            if (!courseRepository.existsByTestsIdAndAndStudentsId(testId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean testAccessByLessonId(Long lessonId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor student) {
            if (!courseRepository.existsByLessonsIdAndInstructorsId(lessonId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean modificationTestAccessById(Long testId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByTestsIdAndInstructorsId(testId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the test.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean courseAccessById(Long courseId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Student student) {
            if (!courseRepository.existsByIdAndStudentsId(courseId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByIdAndInstructorsId(courseId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean courseAccessByInstructorIds(Collection<Long> instructorIds) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (CollectionUtils.isNotEmpty(instructorIds) && !instructorIds.contains(instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor must assign himself to the course to create.");
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean feedbackAccessByCourseId(Long courseId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Student student) {
            if (!courseRepository.existsByIdAndStudentsId(courseId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean feedbackAccessById(Long feedbackId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Student student) {
            if (!feedbackRepository.existsByIdAndStudentId(feedbackId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean homeworkAccess(Long lessonId, Long studentId) {
        User principal = getUserCredentialsOrThrow();
        if (Objects.nonNull(studentId) && principal instanceof Admin) {
            return true;
        } else if (principal instanceof Student student) {
            if (Objects.nonNull(studentId) && !studentId.equals(student.getId())) {
                throw new UserAccessRestrictedException("Incorrect student id provided.");
            } else if (!courseRepository.existsByLessonsIdAndStudentsId(lessonId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean homeworkAccessById(Long homeworkId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Student student) {
            if (!homeworkRepository.existsByIdAndStudentId(homeworkId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean modificationLessonAccessByCourseId(Long courseId) {
        return courseAccessById(courseId);
    }

    @Override
    public boolean modificationLessonAccessById(Long lessonId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByLessonsIdAndInstructorsId(lessonId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean lessonAccessByCourseId(Long courseId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByIdAndInstructorsId(courseId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the course.");
            }
            return true;
        } else if (principal instanceof Student student) {
            if (!courseRepository.existsByIdAndStudentsId(courseId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean lessonAccessById(Long lessonId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByLessonsIdAndInstructorsId(lessonId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the course.");
            }
            return true;
        } else if (principal instanceof Student student) {
            if (!courseRepository.existsByLessonsIdAndStudentsId(lessonId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean submissionAccess(Long lessonId, Long studentId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByLessonsIdAndInstructorsId(lessonId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the course.");
            }
            return true;
        } else if (Objects.nonNull(studentId) && principal instanceof Student student) {
            if (!studentId.equals(student.getId())) {
                throw new UserAccessRestrictedException("Student does not have permissions on requested lesson.");
            }
            return true;
        }
        return false;
    }



    @Override
    public boolean submissionAccess(Long lessonId) {
        return submissionAccess(lessonId, null);
    }

    @Override
    public User getUserCredentialsOrThrow() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .orElseThrow(() -> new UserAccessRestrictedException("Current user is not present in Security Context."));
    }

    public boolean modificationQuestionAccessById(Long questionId) {
        User principal = getUserCredentialsOrThrow();
        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            Long testId = getTestIdByQuestionId(questionId);
            if (!courseRepository.existsByTestsIdAndInstructorsId(testId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the course.");
            }
            return true;
        }
        return false;
    }

    public boolean questionAccessById(Long questionId) {
        User principal = getUserCredentialsOrThrow();
        Long testId = getTestIdByQuestionId(questionId);

        if (principal instanceof Admin) {
            return true;
        } else if (principal instanceof Instructor instructor) {
            if (!courseRepository.existsByTestsIdAndInstructorsId(testId, instructor.getId())) {
                throw new UserAccessRestrictedException("Instructor is not assigned to the course.");
            }
            return true;
        } else if (principal instanceof Student student) {
            if (!courseRepository.existsByTestsIdAndAndStudentsId(testId, student.getId())) {
                throw new UserAccessRestrictedException("Student is not subscribed to the course.");
            }
            return true;
        }
        return false;
    }

    private Long getTestIdByQuestionId(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new SystemException("Question with id: " + questionId + " not found.", NOT_FOUND));
        return question.getTest().getId();
    }
}
