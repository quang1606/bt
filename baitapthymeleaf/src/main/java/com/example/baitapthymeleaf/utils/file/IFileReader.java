package com.example.baitapthymeleaf.utils.file;

import com.example.baitapthymeleaf.model.Person;

import java.util.List;

public interface IFileReader {
    List<Person> read(String filePath);
}
