package com.example.netologydiplom.services;

import com.example.netologydiplom.dto.request.RegisterRequest;
import com.example.netologydiplom.entyties.User;
import com.example.netologydiplom.repositories.RoleRepository;
import com.example.netologydiplom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public Boolean createNewUser(RegisterRequest registerRequest) {
        if (findByUsername(registerRequest.login()).isPresent()) {
            return false;
        }
        User user = new User();
        user.setUsername(registerRequest.login());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRoles(List.of(roleRepository.findRoleByName("ROLE_USER").get()));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)));
        return user;
    }

}
