package com.example.demo1application;

import com.example.demo1application.Controller.BookController;
import com.example.demo1application.model.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {
       ApplicationContext context= SpringApplication.run(Demo1Application.class, args);

        BookController controller = context.getBean(BookController.class);
        System.out.println(controller);

        Book book = context.getBean(Book.class);
        System.out.println(book);

        InitBean initBean = context.getBean(InitBean.class);
        System.out.println(initBean);
    }

}
