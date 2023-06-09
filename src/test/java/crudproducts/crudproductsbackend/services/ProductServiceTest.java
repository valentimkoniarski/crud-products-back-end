package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.dto.UserDto;
import crudproducts.crudproductsbackend.dto.product.ProductDto;
import crudproducts.crudproductsbackend.dto.product.ProductSimplifiedDto;
import crudproducts.crudproductsbackend.entities.Category;
import crudproducts.crudproductsbackend.entities.Product;
import crudproducts.crudproductsbackend.entities.User;
import crudproducts.crudproductsbackend.repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Pageable pageable;

    private final static Long USER_ID = 1L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userService.getIdUserByToken(request)).thenReturn(USER_ID);
        productService = new ProductService(productRepository, userService, modelMapper);
    }

    @BeforeEach
    public void setUpMock() {
        final Product product = createProduct();

        doAnswer(invocation -> {
            final User user = invocation.getArgument(0);
            final UserDto userDto = new UserDto();
            userDto.setEmail(user.getEmail());

            final ProductDto productDto = new ProductDto();
            productDto.setUserDto(userDto);
            return productDto;
        }).when(modelMapper).map(product.getUser(), any(User.class));

        doAnswer(invocation -> {
            final ProductDto createProductDto = invocation.getArgument(0);
            final ProductDto productDto = new ProductDto();
            productDto.setId(createProductDto.getId());
            productDto.setName(createProductDto.getName());
            productDto.setPrice(createProductDto.getPrice());
            return productDto;
        }).when(modelMapper).map(product, any(ProductDto.class));

        doAnswer(invocation -> {
            final Category category = invocation.getArgument(0);
            final CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            return categoryDto;
        }).when(modelMapper).map(product.getCategory(), any(CategoryDto.class));
    }

    @Test
    public void testFindAll() {
        final Product product = createProduct();
        final Page<Product> products = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAllByUserId(USER_ID, pageable)).thenReturn(products);
        productService.findAll(request, pageable);
        verify(productRepository, times(1)).findAllByUserId(USER_ID, pageable);
    }

    @Test
    public void testFindAllDeleted() {
        when(productRepository.findAllByUserIdDeleted(USER_ID, pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        productService.findAllDeleted(request, pageable);
        verify(productRepository, times(1)).findAllByUserIdDeleted(USER_ID, pageable);
    }

    @Test
    public void testFindById() {
        when(productRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(createProduct());
        productService.findById(1L, request);
        verify(productRepository, times(1)).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    public void testSave() {
        final ProductSimplifiedDto productSimplifiedDto = new ProductSimplifiedDto();
        productSimplifiedDto.setName("name");
        productSimplifiedDto.setDescription("description");
        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("name");
        productSimplifiedDto.setCategoryDto(categoryDto);

        productService.save(productSimplifiedDto, request);

        final Product product = modelMapper.map(productSimplifiedDto, Product.class);
        // TODO
        // verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdate() {
        final Product product = modelMapper.map(createProductDto(), Product.class);
        when(productRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(product);
        when(userService.getUserByToken(request)).thenReturn(new User());
        productService.update(1L, createProductDto(), request);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testDelete() {
        when(productRepository.getOne(1L)).thenReturn(createProduct());
        productService.delete(1L, request);
        verify(productRepository, times(1)).deleteProductByIdAndUserId(anyLong(), anyLong());
    }

    public Product createProduct() {
        final Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setDescription("description");
        product.setUser(new User());
        product.setCategory(new Category());
        return product;
    }

    public ProductDto createProductDto() {
        final ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("name");
        productDto.setDescription("description");
        productDto.setUserDto(new UserDto());
        productDto.setCategoryDto(new CategoryDto());
        return productDto;
    }
}
