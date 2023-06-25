package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.dto.CategoryDto;
import crudproducts.crudproductsbackend.entities.Category;
import crudproducts.crudproductsbackend.exceptions.CategoryDeleteException;
import crudproducts.crudproductsbackend.repositories.CategoryRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CategoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private HttpServletRequest request;

    private final static Long USER_ID = 1L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userService.getIdUserByToken(request)).thenReturn(1L);
        categoryService = new CategoryService(categoryRepository, userService, productRepository, modelMapper);
    }

    @BeforeEach
    public void setUpMock() {
        final List<Category> categories = createCategory();

        doAnswer(invocation -> {
            final Category category = invocation.getArgument(0);
            final CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            return categoryDto;
        }).when(modelMapper).map(categories.get(0), any(CategoryDto.class));
    }

    @Test
    public void testFindAll() {
        when(userService.getIdUserByToken(request)).thenReturn(USER_ID);
        when(categoryRepository.findAllByUserId(USER_ID)).thenReturn(createCategory());
        categoryService.findAll(request);
        verify(categoryRepository, times(1)).findAllByUserId(USER_ID);
    }

    @Test
    public void testFindById() {
        when(userService.getIdUserByToken(request)).thenReturn(USER_ID);
        when(categoryRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.ofNullable(createCategory().get(0)));
        categoryService.findById(1L, request);
        verify(categoryRepository, times(1)).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    public void testFindByIdException() {
        when(userService.getIdUserByToken(request)).thenReturn(USER_ID);
        when(categoryRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(null);
        assertThrows(RuntimeException.class, () -> categoryService.findById(1L, request));
    }

    @Test
    public void testSave() {
        when(userService.getIdUserByToken(request)).thenReturn(USER_ID);
        when(categoryRepository.save(createCategory().get(0))).thenReturn(createCategory().get(0));
        final CategoryDto categoryDto = modelMapper.map(createCategory().get(0), CategoryDto.class);
        categoryService.save(categoryDto, request);
        verify(categoryRepository, times(1)).save(createCategory().get(0));
    }

    @Test
    public void testDelete() throws CategoryDeleteException {
        when(userService.getIdUserByToken(request)).thenReturn(USER_ID);
        when(categoryRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.ofNullable(createCategory().get(0)));
        categoryService.delete(1L, request);
        verify(categoryRepository, times(1)).deleteByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    public void testDeleteWithProduct() {
        when(userService.getIdUserByToken(request)).thenReturn(USER_ID);
        when(categoryRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.ofNullable(createCategory().get(0)));
        when(productRepository.existsByCategoryIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        assertThrows(CategoryDeleteException.class, () -> categoryService.delete(anyLong(), request));
        verify(categoryRepository, never()).deleteByIdAndUserId(anyLong(), anyLong());
    }

    public static List<Category> createCategory() {
        final Category category = new Category();
        category.setId(1L);
        category.setName("category");
        return Collections.singletonList(category);
    }

}
