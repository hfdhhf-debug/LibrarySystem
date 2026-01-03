package com.example.library.controller;

import com.example.library.dto.ApiResp;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.repo.BookRepository;
import com.example.library.repo.BorrowRecordRepository;
import com.example.library.repo.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final BorrowRecordRepository borrowRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public StatsController(BorrowRecordRepository borrowRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.borrowRepo = borrowRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-books")
    public ApiResp<List<Map<String, Object>>> topBooks(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        LocalDate f = LocalDate.parse(from);
        LocalDate t = LocalDate.parse(to);
        List<Object[]> rows = borrowRepo.topBooks(f, t);
        List<Map<String, Object>> res = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, rows.size()); i++) {
            Long bookId = (Long) rows.get(i)[0];
            Long cnt = (Long) rows.get(i)[1];
            Book b = bookRepo.findById(bookId).orElse(null);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("bookId", bookId);
            m.put("count", cnt);
            if (b != null) {
                m.put("title", b.getTitle());
                m.put("isbn", b.getIsbn());
                m.put("author", b.getAuthor());
            }
            res.add(m);
        }
        return ApiResp.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-users")
    public ApiResp<List<Map<String, Object>>> topUsers(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = "10") int limit
    ) {
        LocalDate f = LocalDate.parse(from);
        LocalDate t = LocalDate.parse(to);
        List<Object[]> rows = borrowRepo.topUsers(f, t);
        List<Map<String, Object>> res = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, rows.size()); i++) {
            Long userId = (Long) rows.get(i)[0];
            Long cnt = (Long) rows.get(i)[1];
            User u = userRepo.findById(userId).orElse(null);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("userId", userId);
            m.put("count", cnt);
            if (u != null) {
                m.put("username", u.getUsername());
                m.put("role", u.getRole().name());
            }
            res.add(m);
        }
        return ApiResp.ok(res);
    }
}
