package com.example.demo1application.db;

import com.example.demo1application.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDB {

    public static List<Book> books = new ArrayList<>(List.of(new Book( "1","de men phieu luu ky","to hoai",2006),
            new Book( "2","dat rung phuong nam","to ",2009),
            new Book( "3","conan","to hoai",2004),
            new Book( "4","doremon","quang",2007)));
}
