package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.LectureMapper;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.LectureRepository;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {

    public static final String PERMISSION_ERROR = "You don't have a permission.";
    public static final String MODIFY_THE_LECTURE = "Only creator or admin can modify the lecture.";
    public static final String USER_IS_NOT_ENROLLED = "User is not enrolled in this course";
    private final LectureRepository lectureRepository;

    private final LectureMapper mapper;


    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, LectureMapper mapper) {
        this.lectureRepository = lectureRepository;
        this.mapper = mapper;
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
        Lecture lecture1 = mapper.fromDto(lectureDto, creator);
        checkPermission(lecture1.getId(), creator);
        return lectureRepository.create(lecture1);
    }

    @Override
    public Lecture update(LectureDto lectureDto, User user, int lectureId) {
        checkPermission(lectureId, user);

        Lecture lecture = getById(lectureId);
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());

        return lectureRepository.update(lecture);
    }

    @Override
    public void delete(int id, User user) {
        checkPermission(id, user);
        lectureRepository.delete(id);

    }

    @Override
    public Note getNote(int lectureId, int userId) {
        return lectureRepository.getNote(lectureId, userId);
    }

    @Override
    public Note createNote(int lectureId, User user, String text) {
        if (!user.getCourses().contains(getById(lectureId).getCourse())) {
            throw new EntityNotFoundException(USER_IS_NOT_ENROLLED);
        }
        try {
            Note note=getNote(lectureId, user.getId());
            note.setText(text);
            return lectureRepository.updateNote(note);
        }catch (EntityNotFoundException e){
            Note note = new Note();
            note.setLecture(getById(lectureId));
            note.setText(text);
            note.setUser(user);
            return lectureRepository.createNote(note);
        }
    }

    private void isTeacher(User user) {
        if (user.getRole().getName().equals("student")) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

    private void checkPermission(int id, User user) {
        Lecture lecture = getById(id);
        if (user.getId() == lecture.getTeacher().getId()
                || !user.getRole().getName().equals("admin")) {
            throw new AuthorizationException(MODIFY_THE_LECTURE);

        }
    }



}
