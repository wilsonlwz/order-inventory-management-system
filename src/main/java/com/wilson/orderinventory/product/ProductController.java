package com.wilson.orderinventory.product;

import com.wilson.orderinventory.dto.ProductRequestDTO;
import com.wilson.orderinventory.dto.ProductResponseDTO;
import com.wilson.orderinventory.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/allProducts")
    public List<ProductResponseDTO> getAllProducts()
    {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id)
    {
        return productService.getProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createProduct")
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductRequestDTO requestDTO)
    {
        return productService.createProduct(requestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateProduct")
    public ProductResponseDTO updateProduct(@RequestParam Long id, @Valid @RequestBody ProductRequestDTO requestDTO)
    {
        return productService.updateProduct(id, requestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteProduct")
    public void deleteProduct(@RequestParam Long id)
    {
        productService.deleteProduct(id);
    }

    @GetMapping
    public ResponseEntity<String> getProducts()
    {
        return ResponseEntity.ok("Access granted to protected products");
    }
}
