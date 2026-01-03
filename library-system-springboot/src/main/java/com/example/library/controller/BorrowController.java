package com.example.library.controller;

import com.example.library.dto.ApiResp;
import com.example.library.dto.BorrowDtos;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.repo.UserRepository;
import com.example.library.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowController {

    private final BorrowService borrowService;
    private final UserRepository userRepo;

    public BorrowController(BorrowService borrowService, UserRepository userRepo) {
        this.borrowService = borrowService;
        this.userRepo = userRepo;
    }

    private User currentUser(Authentication auth) {
        return userRepo.findByUsername(auth.getName()).orElseThrow();
    }

    @PostMapping("/borrow")
    public ApiResp<BorrowRecord> borrow(Authentication auth, @Valid @RequestBody BorrowDtos.BorrowReq req) {
        User u = currentUser(auth);
        return ApiResp.ok(borrowService.borrow(u.getId(), req.getBookId()));
    }

    @PostMapping("/return")
    public ApiResp<BorrowRecord> giveBack(Authentication auth, @Valid @RequestBody BorrowDtos.ReturnReq req) {
        User u = currentUser(auth);
        boolean isAdmin = u.getRole() == User.Role.ADMIN;
        return ApiResp.ok(borrowService.giveBack(u.getId(), isAdmin, req.getRecordId()));
    }

    @GetMapping("/borrows/my")
    public ApiResp<Page<BorrowRecord>> my(Authentication auth,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        User u = currentUser(auth);
        return ApiResp.ok(borrowService.myRecords(u.getId(), page, size));
    }
}
