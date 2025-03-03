package com.example.demo1application.utils;

import com.example.demo1application.model.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class CsvFileReader implements IFileReader {
    @Override
    public List<Book> readFile(String filePath) {
        List<Book> books = new ArrayList<>();
        log.info("CSV file read");
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            boolean isHeader = true;
            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) { // Bỏ qua dòng tiêu đề
                    isHeader = false;
                    continue;
                }
                if (nextLine.length >= 4) { // Kiểm tra đủ 4 dữ liệu
                    try {
                        Book book = new Book(
                                nextLine[0], // ID
                                nextLine[1], // Tên sách
                                nextLine[2], // Tác giả
                                Integer.parseInt(nextLine[3]) // Năm xuất bản
                        );
                        books.add(book);
                    } catch (NumberFormatException e) {
                        log.warn("Lỗi chuyển đổi năm sách: {}", nextLine[3]);
                    }
                }
            }
        } catch (IOException | CsvValidationException | NumberFormatException e) {
            log.error("Error reading CSV file: {}, Error: {}", filePath, e.getMessage());
        }
        return books;
    }


}
