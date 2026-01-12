package com.wilson.orderinventory.product.repository;

import com.wilson.orderinventory.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
