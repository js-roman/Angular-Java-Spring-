package ru.javabegins.springboot.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabegins.springboot.auth.entity.User;
import ru.javabegins.springboot.auth.repository.UserRepository;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional=userRepository.findByUsername(username);
        if(!userOptional.isPresent()) {
            userOptional=userRepository.findByEmail(username);
        }
        if(!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImpl(userOptional.get());
    }
}
