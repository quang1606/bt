package com.example.baikiemtra.service.impl;

import com.example.baikiemtra.model.PageResponse;
import com.example.baikiemtra.model.Product;
import com.example.baikiemtra.service.PageResponseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageResponseServiceImlp implements PageResponseService {



    @Override
    public int getTotalPages(int x, int y) {
        return (int) Math.ceil((double) x / y);
    }

    @Override
    public <T> List<T> getData(List<T> data, int pageSize, int currentPage) {
        int start = (currentPage-1) * pageSize;
        int end = Math.min(start + pageSize, data.size());
        return data.subList(start, end);
    }
}
