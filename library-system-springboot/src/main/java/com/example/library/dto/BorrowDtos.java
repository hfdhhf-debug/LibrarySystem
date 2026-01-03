package com.example.library.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowDtos {

    public static class BorrowReq {
        @NotNull
        private Long bookId;

        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
    }

    public static class ReturnReq {
        @NotNull
        private Long recordId;

        public Long getRecordId() { return recordId; }
        public void setRecordId(Long recordId) { this.recordId = recordId; }
    }
}
