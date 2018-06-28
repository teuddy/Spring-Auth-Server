package com.socialweb.demo.service;

import com.socialweb.demo.model.User;
import com.socialweb.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PrincipalService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(s,s).orElseThrow(()-> new UsernameNotFoundException("Not Found"));
        return UserPrincipal.create(user);
    }
}
