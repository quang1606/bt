package com.example.baikiemtra.db;

import com.example.baikiemtra.model.Product;
import com.example.baikiemtra.utils.file.IFileReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Slf4j
@Configuration
public class InitDB implements CommandLineRunner {
    private final IFileReader fileReader;
    public static List<Product> products;
    public InitDB(IFileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Reading json file");
        this.products = fileReader.readFile("json.json");
        log.info("Found {} product", products.size());
    }
}

