package com.example.demo1application.repository.impl;

import com.example.demo1application.db.BookDB;
import com.example.demo1application.model.Book;
import com.example.demo1application.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepositoryImpl  implements BookRepository {
    @Override
    public List<Book> finAll() {
        return BookDB.books;
    }

    @Override
    public List<Book> finId(String id) {
        for (Book book : BookDB.books) {
            if (book.getId().equals(id)) {
                return BookDB.books;
            }
        }
        return null;
    }

    @Override
    public List<Book> getBooksYear() {
        BookDB.books.sort((b1,b2)->b1.getYear()-b2.getYear());
        return BookDB.books;
    }

    @Override
    public List<Book> getBooksKey(String keyword) {
//        List<Book> rs = new ArrayList<>();
//            for (Book book : BookDB.books) {
//                if (book.getAuthor().contains(keyword)) {
//                    rs.add(book);
//                }
//            }
//            return rs;
            List<Book> list = BookDB.books.stream().filter(s -> s.getTitle().contains(keyword)).collect(Collectors.toList());
            return list;

    }


}
