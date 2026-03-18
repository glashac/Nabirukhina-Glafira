package ru.fa.carrental.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fa.carrental.model.CarCategory;
import ru.fa.carrental.repository.CarCategoryRepository;

import java.util.List;

/**
 * REST controller for car categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CarCategoryRepository categoryRepository;

    public CategoryController(CarCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<CarCategory> listCategories() {
        return categoryRepository.findAll();
    }
}
