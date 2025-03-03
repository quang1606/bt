package com.example.demo1application.utils;

import com.example.demo1application.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class ExcelFileReader implements IFileReader{
    @Override
    public List<Book> readFile(String path) {
        List<Book> books = new ArrayList<>();
        log.info("Read Excel file");
        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua tiêu đề cột

                Book book = Book.builder()
                        .id(row.getCell(0).getStringCellValue())
                        .title(row.getCell(1).getStringCellValue())
                        .author(row.getCell(2).getStringCellValue())
                        .year((int) row.getCell(3).getNumericCellValue())
                        .build();
                books.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }
}
