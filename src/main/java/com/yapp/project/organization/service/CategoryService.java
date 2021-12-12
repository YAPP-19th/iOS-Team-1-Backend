package com.yapp.project.organization.service;

import com.yapp.project.organization.domain.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    public List<String> findAll() {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < Category.size(); i++) {
            Category category = Category.indexToCategory(i);
            categories.add(category.toString());
        }
        return categories;
    }
}
