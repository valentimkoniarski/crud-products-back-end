package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.dto.product.ProductDto;
import crudproducts.crudproductsbackend.dto.ProductImage.ProductImageDto;
import crudproducts.crudproductsbackend.dto.UserDto;
import crudproducts.crudproductsbackend.dto.product.ProductSimplifiedDto;
import crudproducts.crudproductsbackend.entities.Category;
import crudproducts.crudproductsbackend.entities.Product;
import crudproducts.crudproductsbackend.entities.ProductImage;
import crudproducts.crudproductsbackend.repositories.ProductImageRepository;
import crudproducts.crudproductsbackend.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;

    public List<ProductDto> findAll(final HttpServletRequest request) {
        Long idUserByToken = userService.getIdUserByToken(request);
        Iterable<Product> allByUserId = repository.findAllByUserId(idUserByToken);
        List<ProductDto> productDtoList = getProductSimplifiedDto(allByUserId);
        return productDtoList;
    }

    public List<ProductDto> findAllDeleted(final HttpServletRequest request) {
        Long idUserByToken = userService.getIdUserByToken(request);
        Iterable<Product> allByUserId = repository.findAllByUserIdDeleted(idUserByToken);
        List<ProductDto> productDtoList = getProductSimplifiedDto(allByUserId);
        return productDtoList;
    }

    public ProductSimplifiedDto findById(final Long productId, final HttpServletRequest request) {
        Long idUserByToken = userService.getIdUserByToken(request);
        Product product = repository.findByIdAndUserId(productId, idUserByToken);
        return getProductSimplifiedDto(product);
    }

    public ProductSimplifiedDto save(final ProductSimplifiedDto productSimplifiedDto, final HttpServletRequest request) {
        productSimplifiedDto.setCategoryDto(productSimplifiedDto.getCategoryDto());
        Product product = modelMapper.map(productSimplifiedDto, Product.class);
        product.setUser(userService.getUserByToken(request));
        repository.save(product);
        return getProductSimplifiedDto(product);
    }

    public ProductSimplifiedDto update(final Long productId, final ProductDto productDto, final HttpServletRequest request) {
        Long idUserByToken = userService.getIdUserByToken(request);
        Product product = repository.findByIdAndUserId(productId, idUserByToken);
        setProduct(productDto, request, product);
        repository.save(product);
        return getProductSimplifiedDto(product);
    }

    private void setProduct(final ProductDto productDto, final HttpServletRequest request, final Product product) {
        product.setName(productDto.getName());
        product.setCreatedBy(productDto.getCreatedBy());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setUser(userService.getUserByToken(request));

        final Category category = modelMapper.map(productDto.getCategoryDto(), Category.class);
        product.setCategory(category);
    }

    public ProductSimplifiedDto delete(Long id, HttpServletRequest request) {
        Long idUserByToken = userService.getIdUserByToken(request);
        repository.deleteProductByIdAndUserId(id, idUserByToken);

        Product product = repository.getOne(id);
        product.setDeleted(true);
        return getProductSimplifiedDto(product);

    }

    public List<ProductDto> getProductSimplifiedDto(final Iterable<Product> allByUserId) {
        return StreamSupport.stream(allByUserId.spliterator(), false)
                .map(product -> {
                    final UserDto userDto = modelMapper.map(product.getUser(), UserDto.class);
                    final ProductDto productDto = modelMapper.map(product, ProductDto.class);

                    productDto.setUserDto(userDto);

                    final ProductImage byPrincipalTrueAndProductId = productImageRepository.findByPrincipalTrueAndProductId(product.getId());

                    if (byPrincipalTrueAndProductId != null) {
                        final ProductImageDto productImageDto = modelMapper.map(byPrincipalTrueAndProductId, ProductImageDto.class);
                        productDto.setProductImageDto(Collections.singletonList(productImageDto));
                    }


                    return productDto;
                })
                .collect(Collectors.toList());
    }

    private ProductSimplifiedDto getProductSimplifiedDto(final Product product) {
        final ProductSimplifiedDto productSimplifiedDto = modelMapper.map(product, ProductSimplifiedDto.class);
        final CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
        productSimplifiedDto.setCategoryDto(categoryDto);
        return productSimplifiedDto;
    }

    private List<ProductImageDto> getProductImageDto(final Product product) {
        final List<ProductImageDto> listProductImagesDto = new ArrayList<>();

        product.getProductImages().forEach(productImage -> {
            final ProductImageDto productImageDto = modelMapper.map(product.getProductImages(), ProductImageDto.class);

            productImageDto.setId(productImage.getId());
            productImageDto.setName(productImage.getName());
            productImageDto.setUrl(productImage.getUrl());
            productImageDto.setType(productImage.getType());
            productImageDto.setPrincipal(productImage.getPrincipal());


            listProductImagesDto.add(productImageDto);
        });
        return listProductImagesDto;
    }


}
