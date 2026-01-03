package com.example.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BookDtos {

    public static class BookUpsertReq {
        @NotBlank
        private String isbn;
        @NotBlank
        private String title;
        @NotBlank
        private String author;
        @NotBlank
        private String publisher;
        @NotBlank
        private String location;
        @Min(0)
        private int totalStock;

        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getPublisher() { return publisher; }
        public void setPublisher(String publisher) { this.publisher = publisher; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public int getTotalStock() { return totalStock; }
        public void setTotalStock(int totalStock) { this.totalStock = totalStock; }
    }
}
