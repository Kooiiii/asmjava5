package com.poly.asm.service;

import com.poly.asm.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(Integer id);
    List<Product> findByCategoryId(Integer categoryId);
    Product save(Product product);
    void delete(Integer id);

    // ✅ Thêm hàm tìm kiếm theo tên (Controller cần)
    List<Product> searchByName(String keyword);
}