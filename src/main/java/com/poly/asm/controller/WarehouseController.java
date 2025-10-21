package com.poly.asm.controller;

import com.poly.asm.dao.ProductRepository;
import com.poly.asm.dao.SizeRepository;
import com.poly.asm.dao.WarehouseService;
import com.poly.asm.dao.WarehouseReceiptRepository;
import com.poly.asm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SizeRepository sizeRepo;

    @Autowired
    private WarehouseReceiptRepository warehouseRepo;

    // ✅ Hiển thị form và danh sách phiếu nhập
    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("warehouseList", warehouseRepo.findAll());
        return "warehouse/import";
    }

    // ✅ Xử lý khi người dùng nhấn "Nhập Kho"
    @PostMapping
    public String importProduct(@RequestParam Integer productId,
                                @RequestParam Integer sizeId,
                                @RequestParam Integer colorId,
                                @RequestParam Integer quantity,
                                @RequestParam BigDecimal price,
                                Model model) {

        // Giả lập nhân viên đang đăng nhập
        User staff = new User();
        staff.setId(2);

        warehouseService.importWarehouse(productId, sizeId, colorId, quantity, price, staff);

        // Sau khi nhập, load lại dữ liệu trên cùng trang
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("warehouseList", warehouseRepo.findAll());
        model.addAttribute("success", true);

        return "warehouse/import";
    }
}
