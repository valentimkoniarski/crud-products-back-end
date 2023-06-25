package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.entities.Category;
import crudproducts.crudproductsbackend.exceptions.CategoryDeleteException;
import crudproducts.crudproductsbackend.repositories.CategoryRepository;
import crudproducts.crudproductsbackend.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    public List<CategoryDto> findAll(final HttpServletRequest request) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        final Iterable<Category> allByUserId = categoryRepository.findAllByUserId(idUserByToken);
        return getCategoryDtoList(allByUserId);
    }

    private List<CategoryDto> getCategoryDtoList(final Iterable<Category> allByUserId) {
        return StreamSupport.stream(allByUserId.spliterator(), false)
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    public CategoryDto findById(final Long id, final HttpServletRequest request) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        final Optional<Category> categoryById = categoryRepository.findByIdAndUserId(id, idUserByToken);

        if (isNull(categoryById)) {
            throw new RuntimeException("Category not found");
        }

        return modelMapper.map(categoryById.get(), CategoryDto.class);
    }

    public CategoryDto save(final CategoryDto categoryDto, final HttpServletRequest request) {
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setUser(userService.getUserByToken(request));
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    public void delete(final Long id, final HttpServletRequest request) throws CategoryDeleteException {
        try {
            if (existsProductByCategory(id, request)) {
                throw new CategoryDeleteException();
            }
            final Long idUserByToken = userService.getIdUserByToken(request);
            categoryRepository.deleteByIdAndUserId(id, idUserByToken);
        } catch (CategoryDeleteException e) {
            throw new CategoryDeleteException();
        }
    }

    public boolean existsProductByCategory(final Long categoryId, final HttpServletRequest request) {
        return productRepository.existsByCategoryIdAndUserId(categoryId, userService.getIdUserByToken(request));
    }
}
