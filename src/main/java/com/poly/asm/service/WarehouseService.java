package com.poly.asm.service;

import com.poly.asm.entity.User;
import com.poly.asm.entity.WarehouseReceipt;
import java.math.BigDecimal;
import java.util.List;

public interface WarehouseService {

    void importWarehouse(Integer productId, Integer sizeId, Integer colorId,
                         Integer quantity, BigDecimal price, User staff);

    List<WarehouseReceipt> findAllReceipts();
}