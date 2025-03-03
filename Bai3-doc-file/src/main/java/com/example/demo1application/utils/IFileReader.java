package com.example.demo1application.utils;

import com.example.demo1application.model.Book;

import java.util.List;

public interface IFileReader {
    List<Book> readFile(String path);
}
