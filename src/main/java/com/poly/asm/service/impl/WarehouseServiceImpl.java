package com.poly.asm.service.impl;

import com.poly.asm.dao.*;
import com.poly.asm.entity.*;
import com.poly.asm.service.WarehouseService; // Import interface mới
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service // Giữ nguyên @Service
public class WarehouseServiceImpl implements WarehouseService { // Implement interface

    @Autowired private ProductRepository productRepo;
    @Autowired private SizeRepository sizeRepo;
    @Autowired private ColorRepository colorRepo;
    @Autowired private ProductVariantRepository variantRepo;
    @Autowired private WarehouseReceiptRepository warehouseRepo;

    @Override // Thêm @Override
    @Transactional
    public void importWarehouse(Integer productId, Integer sizeId, Integer colorId,
                                Integer quantity, BigDecimal price, User staff) {
        // ... (Logic cũ giữ nguyên) ...
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Sản phẩm không tìm thấy ID = " + productId));
        Size size = sizeRepo.findById(sizeId).orElseThrow(() -> new RuntimeException("Kích thước không tìm thấy ID = " + sizeId));
        Color color = colorRepo.findById(colorId).orElseThrow(() -> new RuntimeException("Màu sắc không tìm thấy ID = " + colorId));
        Optional<ProductVariant> existingVariant = variantRepo.findByProductAndSizeAndColor(product, size, color);
        ProductVariant variant;
        if (existingVariant.isPresent()) {
            variant = existingVariant.get();
            variant.setQuantity(variant.getQuantity() + quantity);
            // Có thể cần cập nhật giá nhập mới nhất cho biến thể ở đây? Hoặc giữ nguyên giá bán?
            // variant.setPrice(price); // Tạm thời comment, vì price này là giá nhập
        } else {
            variant = new ProductVariant();
            variant.setProduct(product);
            variant.setSize(size);
            variant.setColor(color);
            variant.setQuantity(quantity);
            // Giá bán của biến thể mới nên lấy từ đâu? Tạm gán bằng giá nhập
            variant.setPrice(price);
        }
        variantRepo.save(variant);
        WarehouseReceipt receipt = new WarehouseReceipt();
        receipt.setProductVariant(variant);
        receipt.setQuantity(quantity);
        receipt.setPrice(price); // Lưu giá nhập vào phiếu
        receipt.setImportDate(new Date());
        receipt.setStaff(staff);
        warehouseRepo.save(receipt);
    }

    // ✅ Implement hàm lấy danh sách phiếu nhập
    @Override
    public List<WarehouseReceipt> findAllReceipts() {
        // Sắp xếp theo ngày mới nhất lên đầu (tùy chọn)
        // return warehouseRepo.findAll(Sort.by(Sort.Direction.DESC, "importDate"));
        return warehouseRepo.findAll();
    }
}