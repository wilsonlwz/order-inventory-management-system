package com.wilson.orderinventory.service;

import com.wilson.orderinventory.dto.ProductResponseDTO;
import com.wilson.orderinventory.product.Product;
import com.wilson.orderinventory.product.repository.ProductRepository;
import com.wilson.orderinventory.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_returnsProductList() {
        // Arrange
        Product p1 = new Product("Laptop", new BigDecimal("3500.00") , 35);
        Product p2 = new Product("Mouse", new BigDecimal("250.00"), 50);

        when(productRepository.findAll())
                .thenReturn(List.of(p1, p2));

        // Act
        List<ProductResponseDTO> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Mouse", result.get(1).getName());

        verify(productRepository).findAll();
    }
}

