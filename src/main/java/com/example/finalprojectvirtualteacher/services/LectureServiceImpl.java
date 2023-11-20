package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.LectureMapper;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.exceptions.AuthorizationExceptions;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.LectureRepository;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {

    public static final String PERMISSION_ERROR = "You don't have a permission.";

    private final LectureRepository lectureRepository;
    private final LectureMapper mapper;

    private final UserRepository userRepository;


    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, LectureMapper mapper, UserRepository userRepository) {
        this.lectureRepository = lectureRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }


    @Override
    public List<Lecture> getAll() {
        return lectureRepository.getAll();
    }

    @Override
    public Lecture getById(int id) {
        if (lectureRepository.getById(id) == null) {
            throw new EntityNotFoundException("Lecture", "id", id);
        }
        return lectureRepository.getById(id);
    }

    @Override
    public Lecture create(LectureDto lectureDto, User creator) {
        Lecture lecture = mapper.fromDto(lectureDto, creator);
        checkCreatePermission(creator);
        return lectureRepository.create(lecture);
    }

    @Override
    public Lecture update(LectureDto lectureDto, User user, int lectureId) {
        Lecture lecture = getById(lectureId);
        mapper.fromDtoUpdate(lectureDto, lecture);
        checkPermission(lecture, user);
        return lectureRepository.update(lecture);
    }

    @Override
    public void delete(int id, User user) {
        Lecture lecture = getById(id);
        checkPermission(lecture, user);
        lectureRepository.delete(id);
    }

    private void checkCreatePermission(User user) {
        if (!user.getRole().getName().equals("admin")
                && !user.getRole().getName().equals("teacher")) {
            throw new AuthorizationExceptions(PERMISSION_ERROR);
        }
    }

    private void checkPermission(Lecture lecture, User user) {
        if (!lecture.getTeacher().equals(user) && !user.getRole().getName().equals("admin")) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

    @Override
    public void addAssignment(Lecture lecture, String assignment) {
        User creator = lecture.getTeacher();
        if (creator.getId() == lecture.getTeacher().getId()) {
            lecture.setAssignmentUrl(assignment);
        }
        lectureRepository.addAssignment(lecture);
    }


    @Override
    public void uploadSubmission(int userId, int lectureId, String assignment) {
        Lecture lecture = lectureRepository.getById(lectureId);
        User user = userRepository.getById(userId);
        if (user.getRole().getName().equals("student")) {
            lecture.setAssignmentUrl(assignment);
        } else {
            throw new AuthorizationExceptions(PERMISSION_ERROR);
        }

    }
}



