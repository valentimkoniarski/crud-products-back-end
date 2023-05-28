package crudproducts.crudproductsbackend.controllers;

import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.exceptions.CategoryDeleteException;
import crudproducts.crudproductsbackend.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService service;

    private final Logger logger = Logger.getLogger(CategoryController.class.getName());

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll(final HttpServletRequest request) {
        return ResponseEntity.ok(service.findAll(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(final @PathVariable(value = "id") Long CategoryId,
                                                final HttpServletRequest request) {
        return ResponseEntity.ok(service.findById(CategoryId, request));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> save(final @RequestBody CategoryDto categoryDto,
                                            final HttpServletRequest request) {
        return ResponseEntity.ok(service.save(categoryDto, request));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(final @PathVariable Long id, final HttpServletRequest request) throws CategoryDeleteException {
        service.delete(id, request);
        logger.log(Level.INFO, "Category deleted. Category ID: {0}", id);
        return ResponseEntity.noContent().build();
    }
}
