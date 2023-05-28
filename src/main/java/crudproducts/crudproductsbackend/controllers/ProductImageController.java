package crudproducts.crudproductsbackend.controllers;

import crudproducts.crudproductsbackend.dto.ProductImage.ProductImageSimplifiedDto;
import crudproducts.crudproductsbackend.services.ProductImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/products-images")
@AllArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/upload/{productId}")
    public void uploadImage(final @RequestParam("productImage") List<MultipartFile> files,
                            final @PathVariable Long productId,
                            final HttpServletRequest request) {
        productImageService.uploadImage(files, productId, request);
    }

    @PutMapping("/{productId}/{productImageId}/image-principal")
    public ResponseEntity principalImage(final @PathVariable(value = "productId") Long productId,
                                         final @PathVariable(value = "productImageId") Long productImageId,
                                         final HttpServletRequest request) {
        productImageService.principalImage(productId, productImageId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductImageSimplifiedDto>> findAllByProductId(
            final @PathVariable(value = "productId") Long productId,
            final HttpServletRequest request) {
        return ResponseEntity.ok(productImageService.findAllByProductId(productId, request));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(final @PathVariable Long id, final HttpServletRequest request) {
        productImageService.delete(id, request);
        return ResponseEntity.noContent().build();
    }

}