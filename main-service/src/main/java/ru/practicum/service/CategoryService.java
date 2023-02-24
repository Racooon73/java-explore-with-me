package ru.practicum.service;

import ru.practicum.model.Category;
import ru.practicum.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    Category addCategory(NewCategoryDto dto);

    void deleteCategory(long catId);

    Category patchCategory(long catId, NewCategoryDto category);

    List<Category> getCategories(int from, int size);

    Category getCategory(long catId);
}
