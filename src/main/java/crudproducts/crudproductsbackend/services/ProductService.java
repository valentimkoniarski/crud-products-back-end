package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.dto.ProductImage.ProductImageDto;
import crudproducts.crudproductsbackend.dto.UserDto;
import crudproducts.crudproductsbackend.dto.product.ProductDto;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;
    private final static String EMPTY_IMAGE_PRODUCT = "https://ingoodcompany.asia/images/products_attr_img/matrix/default.png";

    public List<ProductDto> findAll(final HttpServletRequest request) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        final Iterable<Product> allByUserId = productRepository.findAllByUserId(idUserByToken);
        return getProductSimplifiedDto(allByUserId);
    }

    public List<ProductDto> findAllDeleted(final HttpServletRequest request) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        final Iterable<Product> allByUserId = productRepository.findAllByUserIdDeleted(idUserByToken);
        return getProductSimplifiedDto(allByUserId);
    }

    public ProductSimplifiedDto findById(final Long productId, final HttpServletRequest request) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        final Product product = productRepository.findByIdAndUserId(productId, idUserByToken);
        return getProductSimplifiedDto(product);
    }

    public ProductSimplifiedDto save(final ProductSimplifiedDto productSimplifiedDto, final HttpServletRequest request) {
        final Product product = modelMapper.map(productSimplifiedDto, Product.class);
        product.setUser(userService.getUserByToken(request));
        productRepository.save(product);
        return getProductSimplifiedDto(product);
    }

    public ProductSimplifiedDto update(final Long productId, final ProductDto productDto, final HttpServletRequest request) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        final Product product = productRepository.findByIdAndUserId(productId, idUserByToken);
        setProduct(productDto, request, product);
        productRepository.save(product);
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

    public ProductSimplifiedDto delete(final Long id, final HttpServletRequest request) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        productRepository.deleteProductByIdAndUserId(id, idUserByToken);

        final Product product = productRepository.getOne(id);
        product.setDeleted(true);
        return getProductSimplifiedDto(product);
    }

    private ProductSimplifiedDto getProductSimplifiedDto(final Product product) {
        final ProductSimplifiedDto productSimplifiedDto = modelMapper.map(product, ProductSimplifiedDto.class);
        final CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
        productSimplifiedDto.setCategoryDto(categoryDto);
        return productSimplifiedDto;
    }

    public List<ProductDto> getProductSimplifiedDto(final Iterable<Product> allProductsByUserId) {
        return StreamSupport.stream(allProductsByUserId.spliterator(), false)
                .map(this::mapProduct)
                .collect(Collectors.toList());
    }

    private ProductDto mapProduct(final Product product) {
        final UserDto userDto = modelMapper.map(product.getUser(), UserDto.class);
        final ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setUserDto(userDto);

        final ProductImage principalTrueByProductId = productImageRepository.findPrincipalTrueByProductId(product.getId());
        if (isNull(principalTrueByProductId)) {
            final ProductImageDto productImageDto = new ProductImageDto();
            productImageDto.setPrincipal(false);
            productImageDto.setUrl(EMPTY_IMAGE_PRODUCT);
            productDto.setProductImageDto(Collections.singletonList(productImageDto));
        } else {
            final ProductImageDto productImageDto = modelMapper.map(principalTrueByProductId, ProductImageDto.class);
            productDto.setProductImageDto(Collections.singletonList(productImageDto));
        }

        return productDto;
    }
}
