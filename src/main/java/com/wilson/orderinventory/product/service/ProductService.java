package com.wilson.orderinventory.product.service;

import com.wilson.orderinventory.dto.ProductRequestDTO;
import com.wilson.orderinventory.dto.ProductResponseDTO;
import com.wilson.orderinventory.exception.ProductNotFoundException;
import com.wilson.orderinventory.product.Product;
import com.wilson.orderinventory.product.ProductRequest;
import com.wilson.orderinventory.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    // Get all products
    public List<ProductResponseDTO> getAllProducts()
    {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Get one product
    public ProductResponseDTO getProduct(Long id)
    {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        return toDTO(product);
    }

    // Create Product
    public ProductResponseDTO createProduct(ProductRequestDTO dto)
    {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    // Update Product
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto)
    {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    // Delete Product
    public void deleteProduct(Long id)
    {
        if (!productRepository.existsById(id))
        {
            throw new ProductNotFoundException(id);
        }
        else
        {
            productRepository.deleteById(id);
        }
    }

    // Mapping Entity -> DTO
    private ProductResponseDTO toDTO(Product product)
    {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());

        return dto;
    }

    // Mapping DTO -> Entity
    private Product fromDTO(ProductRequestDTO dto)
    {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        return product;
    }
}
