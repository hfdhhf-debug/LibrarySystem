package com.example.library.service;

import com.example.library.dto.BookDtos;
import com.example.library.entity.Book;
import com.example.library.exception.NotFoundException;
import com.example.library.repo.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepo;

    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book create(BookDtos.BookUpsertReq req) {
        Book b = new Book();
        b.setIsbn(req.getIsbn());
        b.setTitle(req.getTitle());
        b.setAuthor(req.getAuthor());
        b.setPublisher(req.getPublisher());
        b.setLocation(req.getLocation());
        b.setTotalStock(req.getTotalStock());
        b.setAvailableStock(req.getTotalStock());
        return bookRepo.save(b);
    }

    public Book update(Long id, BookDtos.BookUpsertReq req) {
        Book b = bookRepo.findById(id).orElseThrow(() -> new NotFoundException("图书不存在"));
        b.setIsbn(req.getIsbn());
        b.setTitle(req.getTitle());
        b.setAuthor(req.getAuthor());
        b.setPublisher(req.getPublisher());
        b.setLocation(req.getLocation());

        // If totalStock changes, adjust availableStock conservatively:
        int borrowed = b.getTotalStock() - b.getAvailableStock();
        b.setTotalStock(req.getTotalStock());
        int newAvailable = Math.max(0, req.getTotalStock() - borrowed);
        b.setAvailableStock(newAvailable);

        return bookRepo.save(b);
    }

    public void delete(Long id) {
        if (!bookRepo.existsById(id)) {
            throw new NotFoundException("图书不存在");
        }
        bookRepo.deleteById(id);
    }

    public Book get(Long id) {
        return bookRepo.findById(id).orElseThrow(() -> new NotFoundException("图书不存在"));
    }

    public Page<Book> search(String keyword, int page, int size) {
        return bookRepo.search(keyword, PageRequest.of(page, size));
    }

    public Book save(Book b) { return bookRepo.save(b); }
}
