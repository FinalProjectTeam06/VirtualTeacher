package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.exceptions.FileUploadException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.helpers.LectureMapper;
import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Note;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.repositories.contracts.LectureRepository;

import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {


    public static final String MODIFY_THE_LECTURE = "Only creator or admin can modify the lecture.";
    public static final String USER_IS_NOT_ENROLLED = "User is not enrolled in this course";
    public static final String FILE_UPLOAD_ERROR = "File can't be uploaded.";
    private final LectureRepository lectureRepository;

    private final LectureMapper mapper;
    private final AssignmentsHelper assignmentsHelper;


    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, LectureMapper mapper, AssignmentsHelper assignmentsHelper) {
        this.lectureRepository = lectureRepository;
        this.mapper = mapper;
        this.assignmentsHelper = assignmentsHelper;
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
    public List<Lecture> getByCourseId(int id) {
        try {
            return lectureRepository.lecturesByCourseId(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Course", id);
        }
    }

    @Override
    public Lecture create(LectureDto lectureDto, User creator) {
        Lecture lecture = mapper.fromDto(lectureDto, creator);
        checkCreatePermission(lecture, creator);
        return lectureRepository.create(lecture);
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
            Note note = getNote(lectureId, user.getId());
            note.setText(text);
            return lectureRepository.updateNote(note);
        } catch (EntityNotFoundException e) {
            Note note = new Note();
            note.setLecture(getById(lectureId));
            note.setText(text);
            note.setUser(user);
            return lectureRepository.createNote(note);
        }
    }

    @Override
    public Lecture submitAssignment(User user, int lectureId, MultipartFile multipartFile) {
        try {
            Lecture lecture = getById(lectureId);
            String assignmentUrl = assignmentsHelper.uploadAssignment(multipartFile);
            Assignment assignment = new Assignment();
            assignment.setAssignmentUrl(assignmentUrl);
            assignment.setUser(user);
            assignment.setLecture(lecture);
            return lectureRepository.submitAssignment(assignment);
        } catch (IOException e) {
            throw new FileUploadException(FILE_UPLOAD_ERROR, e);
        }
    }


    public void checkPermission(int id, User user) {
        Lecture lecture = getById(id);
        if (user.getId() == lecture.getTeacher().getId()
                ||  user.getRole().getId() != 3) {
            throw new AuthorizationException(MODIFY_THE_LECTURE);

        }
    }

    private void checkCreatePermission(Lecture lecture, User user) {
        if (user.getId() != lecture.getCourse().getCreator().getId()
                ||  user.getRole().getId() != 3) {
            throw new AuthorizationException(MODIFY_THE_LECTURE);

        }
    }


}

