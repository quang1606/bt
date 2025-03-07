package com.example.baitapthymeleaf.utils.file;

import com.example.baitapthymeleaf.model.Person;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class CSVFileReader implements IFileReader{
    @Override
    public List<Person> read(String filePath) {
            List<Person> people = new ArrayList<>();
            log.info("Đang đọc file CSV: {}", filePath);
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                String[] nextLine;
                boolean isHeader = true;
                int lineNumber = 0;
                while ((nextLine = reader.readNext()) != null) {
                    lineNumber++;
                    // Bỏ qua dòng tiêu đề
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    // Kiểm tra dữ liệu hợp lệ
                    if (nextLine.length < 7 || nextLine[0].isBlank() || nextLine[1].isBlank() ||
                            nextLine[2].isBlank() || nextLine[3].isBlank() ||
                            nextLine[4].isBlank() || nextLine[5].isBlank() || nextLine[6].isBlank()) {
                        log.warn("Dữ liệu không hợp lệ tại dòng {}: {}", lineNumber, (Object) nextLine);
                        continue;
                    }
                    try {
                        Person person = new Person(
                                nextLine[0].trim(), // ID
                                nextLine[1].trim(), // Họ tên
                                nextLine[2].trim(), // Nghề nghiệp
                                nextLine[3].trim(), // Giới tính
                                nextLine[4].trim(), // Thành phố
                                Double.parseDouble(nextLine[5].trim()), // Lương
                                nextLine[6].trim() // Ngày sinh
                        );
                        people.add(person);
                    } catch (NumberFormatException e) {
                        log.warn("Lỗi chuyển đổi dữ liệu tại dòng {}: {}", lineNumber, nextLine[5]);
                    }
                }
            } catch (IOException | CsvValidationException e) {
                log.error("Lỗi khi đọc file CSV: {}, Lỗi: {}", filePath, e.getMessage());
            }
            log.info("Đọc file hoàn tất, tổng số người: {}", people.size());
            return people;
        }
    }

