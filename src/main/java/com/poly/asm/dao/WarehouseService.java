package com.poly.asm.dao;

import com.poly.asm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SizeRepository sizeRepo;

    @Autowired
    private ColorRepository colorRepo;

    @Autowired
    private ProductVariantRepository variantRepo;

    @Autowired
    private WarehouseReceiptRepository warehouseRepo;

    /**
     * ✅ Hàm nhập kho (tạo hoặc cập nhật biến thể + thêm phiếu nhập)
     */
    @Transactional
    public void importWarehouse(Integer productId, Integer sizeId, Integer colorId,
                                Integer quantity, BigDecimal price, User staff) {

        // 1️⃣ Lấy dữ liệu gốc từ DB
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID = " + productId));

        Size size = sizeRepo.findById(sizeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước ID = " + sizeId));

        Color color = colorRepo.findById(colorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc ID = " + colorId));

        // 2️⃣ Kiểm tra xem biến thể đã tồn tại chưa
        Optional<ProductVariant> existingVariant = variantRepo.findByProductAndSizeAndColor(product, size, color);

        ProductVariant variant;
        if (existingVariant.isPresent()) {
            variant = existingVariant.get();
            variant.setQuantity(variant.getQuantity() + quantity); // cộng dồn số lượng
            variant.setPrice(price);
        } else {
            variant = new ProductVariant();
            variant.setProduct(product);
            variant.setSize(size);
            variant.setColor(color);
            variant.setQuantity(quantity);
            variant.setPrice(price);
        }

        // 3️⃣ Lưu biến thể
        variantRepo.save(variant);

        // 4️⃣ Tạo phiếu nhập kho
        WarehouseReceipt receipt = new WarehouseReceipt();
        receipt.setProductVariant(variant);
        receipt.setQuantity(quantity);
        receipt.setPrice(price);
        receipt.setImportDate(new Date());
        receipt.setStaff(staff);

        warehouseRepo.save(receipt);
    }
}
