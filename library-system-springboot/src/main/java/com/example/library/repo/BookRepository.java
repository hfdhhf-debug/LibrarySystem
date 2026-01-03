package com.example.library.repo;

import com.example.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
        select b from Book b
        where (:kw is null or :kw = '' or
              lower(b.isbn) like lower(concat('%', :kw, '%')) or
              lower(b.title) like lower(concat('%', :kw, '%')) or
              lower(b.author) like lower(concat('%', :kw, '%')) or
              lower(b.publisher) like lower(concat('%', :kw, '%')) or
              lower(b.location) like lower(concat('%', :kw, '%')))
    """)
    Page<Book> search(@Param("kw") String keyword, Pageable pageable);
}
