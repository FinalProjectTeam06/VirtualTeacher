package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.*;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.EmailService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    public static final String PERMISSION_ERROR = "You don't have permission.";

    private final UserRepository userRepository;
    private final CourseService courseService;

    private final EmailService emailService;

    private final Map<Integer,String> userToActivate;
    private final Map<Integer, Timestamp> codeValidity;

    private final Random random;

    @Autowired
    public UserServiceImpl(UserRepository repository, CourseService courseService, EmailService emailService) {
        this.userRepository = repository;
        this.courseService = courseService;
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
        checkPermission(user, updatedUser.getId());
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
        checkPermission(user, id);
        User userToDelete = getById(id);
        userRepository.deleteUser(userToDelete);
    }

    @Override
    public User enrollCourse(User user, int courseId) {
        Course course = courseService.getById(courseId);
        user.addCourse(course);
        return userRepository.updateUser(user);
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
        emailService.sendMessage(email, "Account activation", String.valueOf(code));
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

    private void checkPermission(User user, int userId) {
        if (!user.getRole().getName().equals("admin") && user.getId() != userId) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }
}