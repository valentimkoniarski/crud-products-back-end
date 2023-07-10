package crudproducts.crudproductsbackend.controllers;

import crudproducts.crudproductsbackend.dto.product.ProductDto;
import crudproducts.crudproductsbackend.dto.product.ProductSimplifiedDto;
import crudproducts.crudproductsbackend.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(final HttpServletRequest request, final Pageable pageable) {
        return ResponseEntity.ok(service.findAll(request, pageable));
    }

    @GetMapping("/products-deleted")
    public ResponseEntity<Page<ProductDto>> findAllDeleted(final HttpServletRequest request, final Pageable pageable) {
        return ResponseEntity.ok(service.findAllDeleted(request, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSimplifiedDto> findById(final @PathVariable(value = "id") Long productId,
                                                         final HttpServletRequest request) {
        return ResponseEntity.ok(service.findById(productId, request));
    }

    @PostMapping
    public ResponseEntity<ProductSimplifiedDto> save(final @RequestBody ProductSimplifiedDto productSimplifiedDto,
                                                     final HttpServletRequest request) {
        return ResponseEntity.ok(service.save(productSimplifiedDto, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSimplifiedDto> update(final @PathVariable(value = "id") Long productId,
                                                       final @RequestBody ProductDto productDto,
                                                       final HttpServletRequest request) {

        ProductSimplifiedDto updatedProduct = service.update(productId, productDto, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductSimplifiedDto> delete(final @PathVariable Long id, final HttpServletRequest request) {
        service.delete(id, request);
        return ResponseEntity.noContent().build();
    }

}
