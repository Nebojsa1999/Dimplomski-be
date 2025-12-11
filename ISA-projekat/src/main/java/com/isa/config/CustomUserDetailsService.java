package com.isa.config;

import com.isa.domain.model.User;
import com.isa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findOneByEmail(username);
        org.springframework.security.core.userdetails.User.UserBuilder builder;

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("USERNAME_NOT_FOUND");
        }

        builder = org.springframework.security.core.userdetails.User.withUsername(username);
        String password = user.get().getPassword();

        builder.password(password);
        builder.authorities(user.get().getRole().toString());


        return builder.build();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

}