package com.yapp.project.organization.controller;

import com.yapp.project.organization.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@Api(tags = "카테고리 정보")
public class CategoryController {
    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 리스트")
    @GetMapping
    public List<String> findAll() {
        return categoryService.findAll();
    }
}
