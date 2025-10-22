package com.poly.asm.service.impl;

import com.poly.asm.dao.ProductVariantRepository;
import com.poly.asm.dao.WarehouseReceiptRepository;
import com.poly.asm.entity.Color;
import com.poly.asm.entity.Product;
import com.poly.asm.entity.ProductVariant;
import com.poly.asm.entity.Size;
import com.poly.asm.service.ColorService;
import com.poly.asm.service.ProductVariantService;
import com.poly.asm.service.SizeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private WarehouseReceiptRepository warehouseRepo;

    @Autowired
    private ColorService colorService;

    @Autowired
    private SizeService sizeService;

    @Override
    public List<ProductVariant> findByProductId(Integer productId) {
        return productVariantRepository.findByProductId(productId);
    }

    @Override
    public Optional<ProductVariant> findByProductAndSizeAndColor(Product product, Integer sizeId, Integer colorId) {
        Size size = sizeService.findById(sizeId);
        Color color = colorService.findById(colorId);
        return productVariantRepository.findByProductAndSizeAndColor(product, size, color);
    }

    @Override
    public Optional<ProductVariant> findById(Integer id) {
        return productVariantRepository.findById(id);
    }

    @Override
    public ProductVariant save(ProductVariant variant) {
        return productVariantRepository.save(variant);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        warehouseRepo.deleteAllByProductVariantId(id);
        productVariantRepository.deleteById(id);
    }

    @Override
    public void softDelete(Integer id) {
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể"));
        variant.setTrangThai("Ngừng kinh doanh");
        productVariantRepository.save(variant);
    }
}
