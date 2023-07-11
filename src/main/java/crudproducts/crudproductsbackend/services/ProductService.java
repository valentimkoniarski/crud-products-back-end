package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.dto.UserDto;
import crudproducts.crudproductsbackend.dto.product.ProductDto;
import crudproducts.crudproductsbackend.dto.product.ProductSimplifiedDto;
import crudproducts.crudproductsbackend.entities.Category;
import crudproducts.crudproductsbackend.entities.Product;
import crudproducts.crudproductsbackend.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public Page<ProductDto> findAll(final HttpServletRequest request, final Pageable pageable) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        Page<Product> allByUserId = productRepository.findAllByUserId(idUserByToken, pageable);
        return getProductSimplifiedDto(allByUserId);
    }

    public Page<ProductDto> findAllDeleted(final HttpServletRequest request, final Pageable pageable) {
        final Long idUserByToken = userService.getIdUserByToken(request);
        final Page<Product> allByUserId = productRepository.findAllByUserIdDeleted(idUserByToken, pageable);
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

        if (isNull(productDto.getCategoryDto())) {
            product.setCategory(null);
            return;
        } else {
            final Category category = modelMapper.map(productDto.getCategoryDto(), Category.class);
            product.setCategory(category);
        }
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
        setProductSimplifiedDtoCategory(productSimplifiedDto, product);
        return productSimplifiedDto;
    }

    private void setProductSimplifiedDtoCategory(final ProductSimplifiedDto productSimplifiedDto, final Product product) {
        if (isNull(product.getCategory())) {
            return;
        }
        final CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
        productSimplifiedDto.setCategoryDto(categoryDto);
    }

    public Page<ProductDto> getProductSimplifiedDto(Page<Product> productsPage) {
        final List<ProductDto> productDtos = productsPage.getContent()
                .stream()
                .map(this::mapProduct)
                .collect(Collectors.toList());
        return new PageImpl<>(productDtos, productsPage.getPageable(), productsPage.getTotalElements());
    }

    private ProductDto mapProduct(final Product product) {
        final UserDto userDto = modelMapper.map(product.getUser(), UserDto.class);
        final ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setUserDto(userDto);
        return productDto;
    }
}
