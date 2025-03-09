package com.example.baikiemtra.utils.file;

import com.example.baikiemtra.model.Product;

import java.util.List;

public interface IFileReader {
 public List<Product> readFile(String filePath);
}
