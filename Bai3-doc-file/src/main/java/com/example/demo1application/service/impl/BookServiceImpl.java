package com.example.demo1application.service.impl;

import com.example.demo1application.model.Book;
import com.example.demo1application.repository.BookRepository;
import com.example.demo1application.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private  final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.finAll();
    }

    @Override
    public List<Book> getBooksById(String id) {

        //C1 querytruc tiep tu db
        //c2 lay toan bo roi for
        return bookRepository.finId(id);
    }

    @Override
    public List<Book> getBooksYear() {
        return bookRepository.getBooksYear();
    }

    @Override
    public List<Book> getBooksKey(String keyword) {
        return bookRepository.getBooksKey(keyword);
    }
}
