package com.poly.asm.service;

import com.poly.asm.entity.Product;
import java.util.List;

public interface ProductService {

    // Chức năng cho người dùng
    List<Product> findAll();
    Product findById(Integer id);
    List<Product> findByCategoryId(Integer categoryId);

    // Chức năng cho Admin
    Product save(Product product);
    void delete(Integer id);
}