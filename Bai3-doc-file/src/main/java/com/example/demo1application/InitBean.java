package com.example.demo1application;

import com.example.demo1application.model.Book;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitBean {
    public InitBean() {
        System.out.println("this is InitBean");
    }
    @Bean
    public Book getBook() {
        return Book.builder()
                .title("Book 1")
                .author("Author 1")
                .id("1")
                .year(2020)
                .build();
    }
}
