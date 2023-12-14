package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.*;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Role;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.repositories.contracts.AssignmentRepository;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import com.example.finalprojectvirtualteacher.services.contacts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    public static final String PERMISSION_ERROR = "You don't have permission.";
    private final String REGISTRATION_LINK_TEMPLATE = "http://localhost:8080/auth/login?inviteCode=%s";

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    private final RoleService roleService;

    private final AssignmentRepository assignmentRepository;
    private final LectureService lectureService;
    private final CommentService commentService;
    private final NoteService noteService;
    private final EmailService emailService;

    private final Map<Integer,String> userToActivate;
    private final Map<Integer, Timestamp> codeValidity;



    private final Random random;

    @Autowired
    public UserServiceImpl(UserRepository repository, CourseRepository courseRepository, RoleService roleService, AssignmentRepository assignmentRepository, LectureService lectureService, CommentService commentService, NoteService noteService, EmailService emailService) {
        this.userRepository = repository;
        this.courseRepository = courseRepository;
        this.roleService = roleService;
        this.assignmentRepository = assignmentRepository;
        this.lectureService = lectureService;
        this.commentService = commentService;
        this.noteService = noteService;
        this.emailService = emailService;
        random = new Random();
        codeValidity = new HashMap<>();
        userToActivate = new HashMap<>();
    }

    @Override
    public List<User> getAll(UserFilterOptions userFilterOptions) {
        return userRepository.getAll(userFilterOptions);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }
    public List<User> getAllStudents() {
        return userRepository.getAllStudents();
    }

    @Override
    public List<User> getAllTeachers() {
        return userRepository.getAllTeachers();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByEmail(String userEmail) {
        return userRepository.getByEmail(userEmail);
    }

    @Override
    public User create(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getEmail());
        }


        User createdUser = userRepository.create(user);
        sendActivationEmail(createdUser);

        return createdUser;
    }


    @Override
    public User updateUser(User user, User updatedUser, UserDtoUpdate userDtoUpdate) {
        checkPermission(updatedUser.getId(), user);
        if (userDtoUpdate.getFirstName() != null) {
            updatedUser.setFirstName(userDtoUpdate.getFirstName());
        }
        if (userDtoUpdate.getLastName() != null) {
            updatedUser.setLastName(userDtoUpdate.getLastName());
        }
        if (userDtoUpdate.getPassword() != null &&
                userDtoUpdate.getPassword().equals(userDtoUpdate.getPasswordConfirm())) {
            updatedUser.setPassword(userDtoUpdate.getPassword());
        }
        return userRepository.updateUser(updatedUser);
    }

    @Override
    public void deleteUser(int id, User user) {
        checkDeletePermission(user);
        User userToDelete = getById(id);
        deleteAssignmentsFromUserAndLecture(id);
        commentService.deleteAllCommentsFromUserAndLecture(id);
        noteService.deleteAllNotesByUser(id);
        courseRepository.deleteAllRatesFromUser(id);
        lectureService.deleteAllLecturesByUser(id);
        courseRepository.deleteAllCoursesFromUser(id);
        userRepository.deleteUser(userToDelete);
    }

    @Override
    public void deleteAssignmentsFromUserAndLecture(int userId) {
        assignmentRepository.deleteAssignmentsFromUserAndLecture(userId);
    }

    @Override
    public void deleteAllRatesByUser(int userId) {
        userRepository.deleteAllRatesByUser(userId);
    }

    @Override
    public User enrollCourse(User user, int courseId) {
        Course course = courseRepository.getById(courseId);
        user.addCourse(course);
        emailService.sendMessage(user.getEmail(), "Enrollment confirmation!", String.format("Thanks for enrollment of the course: %s", course.getTitle()));
        return userRepository.updateUser(user);
    }

    @Transactional
    @Override
    public void setEnrollmentCourseStatusToFinished(int userId, int courseId){
        userRepository.setCourseEnrollmentStatusToFinished(userId, courseId);
    }

    @Override
    public void setEnrollmentCourseStatusToGraduated(int userId, int courseId) {
        userRepository.setEnrollmentCourseStatusToGraduated(userId, courseId);
    }

    @Override
    public User addProfilePhoto(User user, String url) {
        user.setProfilePictureUrl(url);
        return userRepository.updateUser(user);
    }

    @Override
    public void activateAccount(int code) {
        if (!userToActivate.containsKey(code))
            throw new WrongActivationCodeException("Code not active. Maybe user is activated already?");
        Timestamp now = Timestamp.from(Instant.now());
        if (codeValidity.get(code).before(now)) {
            userToActivate.remove(code);
            codeValidity.remove(code);
            throw new WrongActivationCodeException("Code expired send new code");
        }
        User user = userRepository.getByEmail(userToActivate.get(code));
        user.setActivated(true);
        userToActivate.remove(code);

        codeValidity.remove(code);
        userRepository.updateUser(user);
    }

    @Override
    public void resendActivationCode(String email) {
        User user = userRepository.getByEmail(email);
        if (user.isActivated())
            throw new ForbiddenOperationException("User already activated!");
        sendActivationEmail(user);
    }
    @Override
    public void sendActivationEmail(User user){
        String email = user.getEmail();
        int code = getActivationCode(user);
//        emailService.sendMessage(email, "Account activation", String.valueOf(code));
        emailService.sendUserCreationVerificationCode(user, code);
        System.out.println(code);
    }
    private int getActivationCode(User user) {
        int code = getCode();
        if (userToActivate.containsKey(code))
            getActivationCode(user);
        userToActivate.put(code, user.getEmail());
        codeValidity.put(code, getActivationTime(4));
        return code;
    }
    private int getCode() {
        return 1000 + random.nextInt(2000);
    }
    private Timestamp getActivationTime(int minutes) {
        Timestamp out = Timestamp.from(Instant.now());
        out.setTime(out.getTime() + ((60 * minutes) * 1000));
        return out;
    }
    @Override
    public void inviteFriend(User inviter, String friendEmail) {
        String invitationLink = "http://localhost:8080/auth/register";
        String invitationMessage = String.format("Hello! You have been invited by %s %s to join our site. Click the following link to register: %s",
                inviter.getFirstName(),inviter.getLastName(), invitationLink);

        emailService.sendMessage(friendEmail, "Invitation to Join", invitationMessage);
    }

    @Override
    public void makeAdmin(User admin, int userId) {
        checkMakeAdminPermission(admin);
        User userToMakeAdmin=getById(userId);
        Role role=roleService.getById(3);
        userToMakeAdmin.setRole(role);
        userRepository.updateUser(userToMakeAdmin);
    }

    private void checkMakeAdminPermission(User user) {
        if (user.getRole().getId()!=3) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

    private void checkPermission(int userId, User user) {
        if (user.getRole().getId()!=3 && user.getId() != userId) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

    private void checkDeletePermission(User user) {
        if (user.getRole().getId()!=3) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }
}