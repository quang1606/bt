package com.example.demo1application.service;

import com.example.demo1application.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    List<Book> getBooksById(String id);

    List<Book> getBooksYear();

    List<Book> getBooksKey(String keyword);
}
