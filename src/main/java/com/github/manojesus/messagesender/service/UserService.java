package com.github.manojesus.messagesender.service;

import com.github.manojesus.messagesender.model.User;
import com.github.manojesus.messagesender.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with email %s does not exists.",username)
        ));
    }

    public User singUp(User user){
        String encodedPass = encoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if(optUser.isPresent()){
            return optUser.get();
        }else{
            throw new UsernameNotFoundException(HttpStatus.NOT_FOUND.toString());
        }
    }
}
