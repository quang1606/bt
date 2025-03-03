package com.example.demo1application.model;

import com.example.demo1application.Controller.BookController;
import lombok.*;
import org.springframework.context.annotation.Bean;
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String title;
    private String author;
    private int year;


}
