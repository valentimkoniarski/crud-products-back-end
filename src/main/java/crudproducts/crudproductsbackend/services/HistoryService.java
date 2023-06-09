package crudproducts.crudproductsbackend.services;

import crudproducts.crudproductsbackend.dto.HistoryDto;
import crudproducts.crudproductsbackend.entities.History;
import crudproducts.crudproductsbackend.entities.Product;
import crudproducts.crudproductsbackend.enums.ProductStatusEnum;
import crudproducts.crudproductsbackend.repositories.HistoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@AllArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final ModelMapper modelMapper;

    public void postPersist(final Object object, final ProductStatusEnum productStatusEnum) {

        if (object instanceof Product) {
            History history = new History();
            history.setDate(LocalDateTime.now());
            Product product = (Product) object;
            history.setResponseUser(product.getUser().getFirstName() + " " + product.getUser().getLastName());
            history.setProduct(product);
            history.setProductStatusEnum(productStatusEnum);

            historyRepository.save(history);
        }

    }

    public List<HistoryDto> findAllByProductId(final Long productId) {
        final Iterable<History> historyByProductId = historyRepository.findAllByProductId(productId);

        final List<HistoryDto> historyDtoList = StreamSupport.stream(historyByProductId.spliterator(), false)
                .map(history -> modelMapper.map(history, HistoryDto.class))
                .collect(Collectors.toList());

        return historyDtoList;
    }
}
