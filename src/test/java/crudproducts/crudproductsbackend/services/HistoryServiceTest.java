package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.entities.History;
import crudproducts.crudproductsbackend.repositories.HistoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HistoryServiceTest {

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private HistoryService historyService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userService.getIdUserByToken(request)).thenReturn(1L);
        historyService = new HistoryService(historyRepository, modelMapper);
    }

    @Test
    public void testeFindAllByProductId() {

        when(historyRepository.findAllByProductId(anyLong())).thenReturn(createHistory());

        historyService.findAllByProductId(1L);
    }

    public List<History> createHistory() {
        final History history = new History();
        history.setDate(LocalDateTime.now());
        return Collections.singletonList(history);
    }
}
