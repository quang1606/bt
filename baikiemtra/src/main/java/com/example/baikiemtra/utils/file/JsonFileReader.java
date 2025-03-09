package com.example.baikiemtra.utils.file;

import com.example.baikiemtra.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JsonFileReader implements IFileReader {

    @Override
    public List<Product> readFile(String filePath) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filePath), new TypeReference<List<Product>>() {
            });
        }catch (IOException e){
            log.error("Error reading json file: {}, Error: {}", filePath, e.getMessage());
            return List.of();
        }

    }
}

