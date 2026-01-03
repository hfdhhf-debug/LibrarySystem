package com.example.library.repo;

import com.example.library.entity.BorrowRecord;
import com.example.library.entity.BorrowRecord.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    Page<BorrowRecord> findByUserIdOrderByBorrowDateDesc(Long userId, Pageable pageable);

    List<BorrowRecord> findByStatus(Status status);

    @Query("""
        select br.bookId, count(br.id)
        from BorrowRecord br
        where br.borrowDate between :from and :to
        group by br.bookId
        order by count(br.id) desc
    """)
    List<Object[]> topBooks(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
        select br.userId, count(br.id)
        from BorrowRecord br
        where br.borrowDate between :from and :to
        group by br.userId
        order by count(br.id) desc
    """)
    List<Object[]> topUsers(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
