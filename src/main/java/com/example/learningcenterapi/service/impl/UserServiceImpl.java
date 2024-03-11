package com.example.learningcenterapi.service.impl;


import com.example.learningcenterapi.domain.Instructor;
import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.domain.enumeration.Role;
import com.example.learningcenterapi.dto.request.UserRequestDTO;
import com.example.learningcenterapi.dto.response.UserResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.InstructorMapper;
import com.example.learningcenterapi.mapper.StudentMapper;
import com.example.learningcenterapi.mapper.UserMapper;
import com.example.learningcenterapi.repository.InstructorRepository;
import com.example.learningcenterapi.repository.StudentRepository;
import com.example.learningcenterapi.repository.UserRepository;
import com.example.learningcenterapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final UserMapper userMapper;
    private final InstructorMapper instructorMapper;
    private final StudentMapper studentMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        log.debug("Finding user with id: {}", id);
        User user = findEntityById(id);
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAllInstructors(Pageable pageable) {
        log.debug("Finding all instructors");
        Page<Instructor> instructors = instructorRepository.findAll(pageable);
        return instructors.map(instructorMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAllStudents(Pageable pageable) {
        log.debug("Finding all students");
        Page<Student> students = studentRepository.findAll(pageable);
        return students.map(studentMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAllUsers(Pageable pageable) {
        log.debug("Finding all users");
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllInstructorsByCourseId(Long courseId) {
        log.debug("Finding all instructors by course id: {}", courseId);
        List<Instructor> instructors = instructorRepository.findAllByCoursesId(courseId);
        return instructorMapper.toResponseDTOList(instructors);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllStudentsByCourseId(Long courseId) {
        log.debug("Finding all students by course id: {}", courseId);
        List<Student> students = studentRepository.findAllByCoursesId(courseId);
        return studentMapper.toResponseDTOList(students);
    }

    @Override
    public UserResponseDTO save(UserRequestDTO userDTO) {
        log.debug("Saving user: {}", userDTO);
        User user = userMapper.fromRequestDTO(userDTO);
        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateById(UserRequestDTO userDTO, Long userId) {
        log.debug("Updating user: {}", userDTO);
        User existingUser = setUpdatableFields(userDTO, userId);
        return userMapper.toResponseDTO(userRepository.save(existingUser));
    }

    @Override
    public UserResponseDTO assignRoleForUser(Long userId, Role role) {
        log.debug("Assigning role `{}` to user with id: {}", role, userId);
        User user = findEntityById(userId);
        user.setRole(role);
        user = userRepository.saveAndFlush(user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting user with id: {}", id);
        User user = findEntityById(id);
        if (user instanceof Instructor instructor) {
            instructorRepository.delete(instructor);
        } else if (user instanceof Student student) {
            studentRepository.delete(student);
        } else {
            userRepository.delete(user);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO mapToResponse(User user) {
        log.debug("Mapping user to response: {}", user);
        return userMapper.toResponseDTO(user);
    }

    private User setUpdatableFields(UserRequestDTO userDTO, Long userId) {
        User existingUser = findEntityById(userId);
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        return existingUser;
    }

    private User findEntityById(Long userId) {
        checkNull(userId, "User id");
        return userRepository.findById(userId)
                .orElseThrow(() -> new SystemException("User with id: " + userId + " not found", NOT_FOUND));
    }
}
