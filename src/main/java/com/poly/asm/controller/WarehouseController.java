package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.dao.*;
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
    private UserSession userSession;
    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SizeRepository sizeRepo;

    @Autowired
    private WarehouseReceiptRepository warehouseRepo;
    @Autowired
    private ColorRepository colorRepo;
    // ✅ Hiển thị form và danh sách phiếu nhập
    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("colors", colorRepo.findAll()); // ✅ thêm dòng này
        model.addAttribute("warehouseList", warehouseRepo.findAll());
        return "warehouse/import";
    }

    @PostMapping
    public String importProduct(@RequestParam Integer productId,
                                @RequestParam Integer sizeId,
                                @RequestParam Integer colorId,
                                @RequestParam Integer quantity,
                                @RequestParam BigDecimal price,
                                Model model) {

        // ✅ Lấy nhân viên đang đăng nhập
        User staff = userSession.getCurrentUser();
        if (staff == null) {
            model.addAttribute("errorMessage", "Vui lòng đăng nhập trước khi nhập kho!");
            return "warehouse/import";
        }

        warehouseService.importWarehouse(productId, sizeId, colorId, quantity, price, staff);

        // Load lại dữ liệu
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("colors", colorRepo.findAll());
        model.addAttribute("warehouseList", warehouseRepo.findAll());
        model.addAttribute("successMessage", "Nhập kho thành công!");

        return "warehouse/import";
    }
}
