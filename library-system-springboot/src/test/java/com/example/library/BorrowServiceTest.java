package com.example.library;

import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.repo.BookRepository;
import com.example.library.repo.UserRepository;
import com.example.library.service.BorrowService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BorrowServiceTest {

    @Autowired
    BorrowService borrowService;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    Long userId;
    Long bookId;

    @BeforeEach
    void setup() {
        bookRepo.deleteAll();
        userRepo.deleteAll();

        User user = new User("u1", encoder.encode("123"), User.Role.USER);
        user = userRepo.save(user);
        userId = user.getId();

        Book book = new Book();
        book.setIsbn("isbn1");
        book.setTitle("t1");
        book.setAuthor("a1");
        book.setPublisher("p1");
        book.setLocation("l1");
        book.setTotalStock(1);
        book.setAvailableStock(1);
        book = bookRepo.save(book);
        bookId = book.getId();
    }

    @Test
    void borrow_ok() {
        BorrowRecord br = borrowService.borrow(userId, bookId);
        assertNotNull(br.getId());
        Book b = bookRepo.findById(bookId).orElseThrow();
        assertEquals(0, b.getAvailableStock());
    }

    @Test
    void borrow_stock_insufficient() {
        borrowService.borrow(userId, bookId);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> borrowService.borrow(userId, bookId));
        assertTrue(ex.getMessage().contains("库存不足"));
    }

    @Test
    void borrow_user_blacklisted() {
        User u = userRepo.findById(userId).orElseThrow();
        u.setEnabled(false);
        userRepo.save(u);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> borrowService.borrow(userId, bookId));
        assertTrue(ex.getMessage().contains("黑名单"));
    }
}
