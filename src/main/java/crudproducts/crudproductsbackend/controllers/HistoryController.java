package crudproducts.crudproductsbackend.controllers;

import crudproducts.crudproductsbackend.dto.HistoryDto;
import crudproducts.crudproductsbackend.services.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/products-history")
@AllArgsConstructor
public class HistoryController {

    private final HistoryService service;

    @GetMapping(value = "/{productId}")
    public ResponseEntity<List<HistoryDto>> findAll(@PathVariable(value = "productId") Long productId) {
        return ResponseEntity.ok(service.findAllByProductId(productId));
    }


}
