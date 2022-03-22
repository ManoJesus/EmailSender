package com.github.manojesus.messagesender.service;

import com.github.manojesus.messagesender.model.User;
import com.github.manojesus.messagesender.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Objects;
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
    public String getUserId(OAuth2User oauthPrincipal, Principal principal) {
        String userId = "";
        if(oauthPrincipal != null && StringUtils.hasText(oauthPrincipal.getAttribute("login"))) {
            userId = Objects.requireNonNull(oauthPrincipal.getAttribute("login")).toString().toLowerCase();
        }else{
            User user = findByEmail(principal.getName());
            userId = user.getFirstName();
        }
        return userId.substring(0,1).toUpperCase() + userId.substring(1);
    }
}
