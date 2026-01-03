package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.NotFoundException;
import com.example.library.repo.BorrowRecordRepository;
import com.example.library.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BorrowService {

    private final BookService bookService;
    private final BorrowRecordRepository borrowRepo;
    private final UserRepository userRepo;

    private final int defaultDays;

    public BorrowService(BookService bookService,
                         BorrowRecordRepository borrowRepo,
                         UserRepository userRepo,
                         @Value("${app.borrow.defaultDays}") int defaultDays) {
        this.bookService = bookService;
        this.borrowRepo = borrowRepo;
        this.userRepo = userRepo;
        this.defaultDays = defaultDays;
    }

    @Transactional
    public BorrowRecord borrow(Long userId, Long bookId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException("用户不存在"));
        if (!user.isEnabled()) {
            throw new BadRequestException("用户已被禁用/黑名单，无法借书");
        }

        Book book = bookService.get(bookId);
        if (book.getAvailableStock() <= 0) {
            throw new BadRequestException("库存不足");
        }

        book.setAvailableStock(book.getAvailableStock() - 1);
        bookService.save(book);

        BorrowRecord br = new BorrowRecord();
        br.setUserId(userId);
        br.setBookId(bookId);
        br.setBorrowDate(LocalDate.now());
        br.setDueDate(LocalDate.now().plusDays(defaultDays));
        br.setStatus(BorrowRecord.Status.BORROWED);
        return borrowRepo.save(br);
    }

    @Transactional
    public BorrowRecord giveBack(Long actorUserId, boolean isAdmin, Long recordId) {
        BorrowRecord br = borrowRepo.findById(recordId)
                .orElseThrow(() -> new NotFoundException("借阅记录不存在"));

        if (!isAdmin && !br.getUserId().equals(actorUserId)) {
            throw new BadRequestException("无权限归还他人的借阅记录");
        }
        if (br.getReturnDate() != null) {
            throw new BadRequestException("该记录已归还");
        }

        br.setReturnDate(LocalDate.now());
        if (LocalDate.now().isAfter(br.getDueDate())) {
            br.setStatus(BorrowRecord.Status.OVERDUE);
        } else {
            br.setStatus(BorrowRecord.Status.RETURNED);
        }
        BorrowRecord saved = borrowRepo.save(br);

        Book book = bookService.get(br.getBookId());
        book.setAvailableStock(book.getAvailableStock() + 1);
        bookService.save(book);

        return saved;
    }

    public Page<BorrowRecord> myRecords(Long userId, int page, int size) {
        return borrowRepo.findByUserIdOrderByBorrowDateDesc(userId, PageRequest.of(page, size));
    }
}
