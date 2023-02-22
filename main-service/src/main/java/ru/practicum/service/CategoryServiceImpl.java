package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.dto.NewCategoryDto;
import ru.practicum.storage.CategoryRepository;
import ru.practicum.storage.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category addCategory(NewCategoryDto dto) {
        return categoryRepository.save(Category.builder()
                .name(dto.getName())
                .build());
    }

    @Override
    public void deleteCategory(long catId) {
        Optional<Category> oCategory = categoryRepository.findById(catId);
        if (oCategory.isEmpty()) {
            throw new NotFoundException();
        }
        if (eventRepository.existsByCategory(catId)) {
            throw new ConflictException("Category have events");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public Category patchCategory(long catId, Category category) {
        category.setId(catId);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        PageRequest pageRequest = PageRequest.of((from / size), size, Sort.by(Sort.Direction.ASC, "id"));

        return categoryRepository.findAll(pageRequest).stream().collect(Collectors.toList());
    }

    @Override
    public Category getCategory(long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new NotFoundException();
        }

    }
}
