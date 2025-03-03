package com.example.demo1application.repository;

import com.example.demo1application.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> finAll();
    List<Book> finId(String id);

    List<Book> getBooksYear();

    List<Book> getBooksKey(String keyword);
}
