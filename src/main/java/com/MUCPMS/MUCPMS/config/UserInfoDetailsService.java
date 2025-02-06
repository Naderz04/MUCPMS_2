//package com.MUCPMS.MUCPMS.config;
//
//import com.MUCPMS.MUCPMS.model.User;
//import com.MUCPMS.MUCPMS.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Optional;
//
//@Configuration
//
//public class UserInfoDetailsService implements UserDetailsService {
//
//@Autowired
//    private UserService userService;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user=userService.findUserByUserName(username);
//        return user.map(UserInfoDetails::new).orElseThrow(()-> new UsernameNotFoundException("this user not found in database"));
//    }
//
//}
