package com.poly.asm.dao;

import com.poly.asm.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // tìm sản phẩm theo tên (không phân biệt người dùng ghi chữ hoa thường).
    List<Product> findByNameContainingIgnoreCase(String keyword);
    // Tìm sản phẩm theo danh mục sản phẩm
    List<Product> findByCategoryId(Integer categoryId);
    // Tìm sản phẩm theo id của thương hiệu
    List<Product> findByBrandId(Integer brandId);
    // lấy sản phẩm theo tên danh mục sản phẩm
    @Query("SELECT p FROM Product p WHERE p.category.name = ?1")
    List<Product> findAllByCategoryName(String categoryName);
    List<Product> findByBrandIdAndCategoryId(Integer brandId, Integer categoryId);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByBrandId(Integer brandId, Pageable pageable);

    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    Page<Product> findByBrandIdAndCategoryId(Integer brandId, Integer categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByName(@org.springframework.data.repository.query.Param("keyword") String keyword, Pageable pageable);
}
