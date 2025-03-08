package com.example.baitapthymeleaf.db;

import com.example.baitapthymeleaf.model.Person;
import com.example.baitapthymeleaf.utils.file.IFileReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Slf4j
@Configuration
public class InitDB implements CommandLineRunner {
    private final IFileReader fileReader;
    @Getter
    public static List<Person> people;
    public InitDB(IFileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Reading data");
        this.people = fileReader.read("data2.csv");
        log.info("Found {} people", people.size());

    }
}
