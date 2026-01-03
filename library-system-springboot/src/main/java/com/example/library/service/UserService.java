package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.exception.BadRequestException;
import com.example.library.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public User register(String username, String rawPassword) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new BadRequestException("用户名已存在");
        }
        String hash = encoder.encode(rawPassword);
        User u = new User(username, hash, User.Role.USER);
        return userRepo.save(u);
    }

    public User getByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("用户不存在"));
    }

    public void disableUser(Long userId) {
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException("用户不存在"));
        u.setEnabled(false);
        userRepo.save(u);
    }
}
