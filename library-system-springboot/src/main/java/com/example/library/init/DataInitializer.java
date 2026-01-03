package com.example.library.init;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import com.example.library.repo.BookRepository;
import com.example.library.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository userRepo, BookRepository bookRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.bookRepo = bookRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // Create default admin if not exists
        userRepo.findByUsername("admin").orElseGet(() -> {
            User admin = new User("admin", encoder.encode("admin123"), User.Role.ADMIN);
            return userRepo.save(admin);
        });

        // Add a sample book to make demo easier
        if (bookRepo.count() == 0) {
            Book b = new Book();
            b.setIsbn("9787111128069");
            b.setTitle("软件工程：实践者的研究方法");
            b.setAuthor("Roger S. Pressman");
            b.setPublisher("机械工业出版社");
            b.setLocation("A1-03");
            b.setTotalStock(5);
            b.setAvailableStock(5);
            bookRepo.save(b);
        }
    }
}
