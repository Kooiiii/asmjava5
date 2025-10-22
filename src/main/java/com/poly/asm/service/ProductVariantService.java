package com.poly.asm.service;

import com.poly.asm.entity.Product;
import com.poly.asm.entity.ProductVariant;
import java.util.List;
import java.util.Optional;

public interface ProductVariantService {

    List<ProductVariant> findByProductId(Integer productId);

    Optional<ProductVariant> findByProductAndSizeAndColor(Product product, Integer sizeId, Integer colorId);

    Optional<ProductVariant> findById(Integer id);

    ProductVariant save(ProductVariant variant);

    void delete(Integer id);

    void softDelete(Integer id);


}
