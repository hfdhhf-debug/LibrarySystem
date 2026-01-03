package com.example.library.controller;

import com.example.library.dto.ApiResp;
import com.example.library.dto.BookDtos;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) { this.bookService = bookService; }

    @GetMapping
    public ApiResp<Page<Book>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResp.ok(bookService.search(keyword, page, size));
    }

    @GetMapping("/{id}")
    public ApiResp<Book> get(@PathVariable Long id) {
        return ApiResp.ok(bookService.get(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResp<Book> create(@Valid @RequestBody BookDtos.BookUpsertReq req) {
        return ApiResp.ok(bookService.create(req));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResp<Book> update(@PathVariable Long id, @Valid @RequestBody BookDtos.BookUpsertReq req) {
        return ApiResp.ok(bookService.update(id, req));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResp<String> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ApiResp.ok("deleted");
    }
}
