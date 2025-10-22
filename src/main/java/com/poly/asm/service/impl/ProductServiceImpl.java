package com.poly.asm.service.impl;

import com.poly.asm.dao.ProductRepository;
import com.poly.asm.entity.Product;
import com.poly.asm.entity.ProductVariant;
import com.poly.asm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
    }

    @Override
    public List<Product> findByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> findByBrandId(Integer brandId) {
        return productRepository.findByBrandId(brandId);
    }

    @Override
    public List<Product> findByBrandAndCategory(Integer brandId, Integer categoryId) {
        return productRepository.findByBrandIdAndCategoryId(brandId, categoryId);
    }


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findByBrandId(Integer brandId, Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable);
    }

    @Override
    public Page<Product> findByCategoryId(Integer categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> findByBrandAndCategory(Integer brandId, Integer categoryId, Pageable pageable) {
        return productRepository.findByBrandIdAndCategoryId(brandId, categoryId, pageable);
    }

    @Override
    public Page<Product> searchByName(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.searchByName(keyword, pageable);
    }

    @Override
    public void deleteVariant(Integer productId, Integer variantId) {
        Product product = findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm.");
        }

        ProductVariant variantToRemove = product.getVariants()
                .stream()
                .filter(v -> v.getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy biến thể cần xóa."));

        product.getVariants().remove(variantToRemove);
        save(product);
    }

    @Override
    public void addVariant(Integer productId) {
        Product product = findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm.");
        }

        ProductVariant newVariant = new ProductVariant();
        newVariant.setProduct(product);
        product.getVariants().add(newVariant);
        save(product);
    }
}