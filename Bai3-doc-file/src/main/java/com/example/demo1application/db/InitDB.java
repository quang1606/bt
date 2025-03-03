package com.example.demo1application.db;

import com.example.demo1application.model.Book;
import com.example.demo1application.utils.IFileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class InitDB implements CommandLineRunner {
    private final IFileReader fileReader;

    public InitDB(@Qualifier("excelFileReader") IFileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Reading data");
        List<Book> books = fileReader.readFile("excel.xlsx");
        log.info("Found {} books", books.size());
    }
}
