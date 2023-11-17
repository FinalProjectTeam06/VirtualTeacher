package com.virtualteacher.services;

import com.virtualteacher.exceptions.AuthorizationExceptions;
import com.virtualteacher.exceptions.EntityNotFoundException;
import com.virtualteacher.helpers.LectureMapper;
import com.virtualteacher.models.Lecture;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.LectureDto;
import com.virtualteacher.repositories.contracts.LectureRepository;
import com.virtualteacher.services.contacts.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {

    public static final String PERMISSION_ERROR = "You don't have a permission.";
    public static final String MODIFY_THE_LECTURE = "Only creator or admin can modify the lecture.";
    private final LectureRepository lectureRepository;

    private final LectureMapper mapper;


    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, LectureMapper mapper){
        this.lectureRepository =lectureRepository;
        this.mapper = mapper;
    }


    @Override
    public List<Lecture> getAll() {
        return lectureRepository.getAll();
    }

    @Override
    public Lecture getById(int id) {
        if (lectureRepository.getById(id)==null){
            throw new EntityNotFoundException("Lecture","id",id);
        }
        return lectureRepository.getById(id);
    }

    @Override
    public Lecture create(LectureDto lectureDto, User creator) {
        Lecture lecture1 = mapper.fromDto(lectureDto,creator);
        checkPermission(lecture1.getId(),creator);
       return lectureRepository.create(lecture1);
    }

    @Override
    public Lecture update(LectureDto lectureDto, User user, int lectureId) {
        checkPermission(lectureId,user);

        Lecture lecture = getById(lectureId);
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());

        return lectureRepository.update(lecture);
    }

    @Override
    public void delete(int id, User user) {
        checkPermission(id,user);
        lectureRepository.delete(id);

    }


    private void isTeacher(User user){
        if(user.getRole().getName().equals("student")){
            throw new AuthorizationExceptions(PERMISSION_ERROR);
        }
    }

    private void checkPermission(int id,User user){
        Lecture lecture = getById(id);
        if(user.getId() == lecture.getTeacher().getId()
                || !user.getRole().getName().equals("admin")){
            throw new AuthorizationExceptions(MODIFY_THE_LECTURE);

        }
    }

















}
