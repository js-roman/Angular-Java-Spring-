package ru.javabegins.springboot.auth.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javabegins.springboot.auth.entity.Activity;
import ru.javabegins.springboot.auth.entity.User;
import ru.javabegins.springboot.auth.repository.ActivityRepository;
import ru.javabegins.springboot.auth.repository.UserRepository;
import ru.javabegins.springboot.util.MyLogger;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@Log
public class UserService {


    private UserRepository userRepository;
    private ActivityRepository activityRepository;

    @Autowired
    public UserService(UserRepository userRepository, ActivityRepository activityRepository) {
        this.userRepository = userRepository;
        this.activityRepository=activityRepository;
    }

    public void save(User user, Activity activity) {
        userRepository.save(user);
        activityRepository.save(activity);
    }


    public boolean isUserExists(String username, String email) {
        if (userRepository.existsUsername(username) || userRepository.existsEmail(email)) return true;
        return false;
    }

    public Optional<Activity> findActivityByUserId(long id){
        return activityRepository.findByUserId(id);
    }


    public Optional<Activity> findActivityByUuid(String uuid){
        return activityRepository.findByUuid(uuid);
    }

    public int activate(String uuid){
        return activityRepository.changeActivated(uuid, true);
    }

    public int deactivate(String uuid){
        return activityRepository.changeActivated(uuid, false);
    }

    public int updatePassword(String username, String password){
        return userRepository.updatePassword(username,password);
    }
}
