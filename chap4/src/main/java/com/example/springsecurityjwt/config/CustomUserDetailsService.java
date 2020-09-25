package com.example.springsecurityjwt.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles;
        if(username.equals("admin"))
        {
//            admin:admin
            roles= Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User("admin","$2a$10$0Nv/Qa7m8DdjWpBS2XRZWeP8rWDB7OdScb2grQSRDS9I9fWWlBNG2",roles);
        }
        if(username.equals("user"))
        {
//            user:user
            roles= Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            return new User("user","$2a$10$5VikX1NNQFL9f.N7Ta5wVuBL5HuPi7ro5Q3UZYGVOCURwiotGrVCS",roles);
        }
        throw new UsernameNotFoundException("User not found with the name "+ username);
    }
}
