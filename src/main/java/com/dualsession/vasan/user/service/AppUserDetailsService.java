package com.dualsession.vasan.user.service;

import com.dualsession.vasan.user.model.AppUserDetails;
import com.dualsession.vasan.user.model.User;
import com.dualsession.vasan.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;



    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);

    }

    public void save(User user) {
        this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new AppUserDetails(user);
    }
}
