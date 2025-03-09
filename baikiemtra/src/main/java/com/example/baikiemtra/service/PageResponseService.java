package com.example.baikiemtra.service;

import com.example.baikiemtra.model.Product;

import java.util.List;

public interface PageResponseService {
    public int getTotalPages(int x, int y);
    public <T> List<T> getData(List<T> data, int pageSize, int currentPage);
}
