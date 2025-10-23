package com.poly.asm.controller.Admin;

import com.poly.asm.entity.*;
import com.poly.asm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/products/{productId}/variants")
public class AdminProductVariantController {

    @Autowired private ProductService productService;
    @Autowired private ProductVariantService productVariantService;
    @Autowired private ColorService colorService;
    @Autowired private SizeService sizeService;
    @Autowired private UserService userService;

    private void addAdminUserToModel(Model model, Principal principal) {
        if (principal == null) return;
        userService.findByUsername(principal.getName())
                .ifPresent(user -> model.addAttribute("user", user));
    }

    // Form thêm variant mới
    @GetMapping("/add")
    public String addVariantForm(@PathVariable("productId") Integer productId, Model model, Principal principal) {
        addAdminUserToModel(model, principal);

        // Lấy thông tin sản phẩm và thêm vào model
        Product product = productService.findById(productId);
        if (product == null) {
            return "redirect:/admin/products";
        }

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);

        model.addAttribute("variant", variant);
        model.addAttribute("product", product);
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/add_variant";
    }

    // Lưu variant mới - REDIRECT VỀ TRANG EDIT_PRODUCT
    @PostMapping("/add")
    public String saveVariant(@PathVariable("productId") Integer productId,
                              @ModelAttribute("variant") ProductVariant variant,
                              RedirectAttributes redirectAttrs) {
        Product product = productService.findById(productId);
        variant.setProduct(product);

        productVariantService.save(variant);
        redirectAttrs.addFlashAttribute("success", "Biến thể đã được thêm thành công!");
        return "redirect:/admin/products/edit/" + productId; // REDIRECT VỀ TRANG EDIT_PRODUCT
    }

    // Form sửa variant
    @GetMapping("/edit/{variantId}")
    public String editVariantForm(@PathVariable("productId") Integer productId,
                                  @PathVariable("variantId") Integer variantId,
                                  Model model, Principal principal) {
        addAdminUserToModel(model, principal);

        // Lấy thông tin sản phẩm và thêm vào model
        Product product = productService.findById(productId);
        if (product == null) {
            return "redirect:/admin/products";
        }

        ProductVariant variant = productVariantService.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với id: " + variantId));

        model.addAttribute("variant", variant);
        model.addAttribute("product", product); // THÊM PRODUCT VÀO MODEL
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/edit_variant";
    }

    // Cập nhật variant - REDIRECT VỀ TRANG EDIT_PRODUCT
    @PostMapping("/update/{variantId}")
    public String updateVariant(@PathVariable("productId") Integer productId,
                                @PathVariable("variantId") Integer variantId,
                                @ModelAttribute("variant") ProductVariant variant,
                                RedirectAttributes redirectAttrs) {
        variant.setProduct(productService.findById(productId));
        productVariantService.save(variant);
        redirectAttrs.addFlashAttribute("success", "Biến thể đã được cập nhật thành công!");
        return "redirect:/admin/products/edit/" + productId; // REDIRECT VỀ TRANG EDIT_PRODUCT
    }

    // Ngừng kinh doanh - REDIRECT VỀ TRANG EDIT_PRODUCT
    @GetMapping("/delete/{variantId}")
    public String deleteVariant(@PathVariable("productId") Integer productId,
                                @PathVariable("variantId") Integer variantId,
                                RedirectAttributes redirectAttributes) {
        try {
            ProductVariant variant = productVariantService.findById(variantId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với id: " + variantId));
            variant.setTrangThai("Ngừng kinh doanh");
            productVariantService.save(variant);
            redirectAttributes.addFlashAttribute("success", "Biến thể đã được ngừng kinh doanh!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể ngừng kinh doanh biến thể: " + e.getMessage());
        }
        return "redirect:/admin/products/edit/" + productId; // REDIRECT VỀ TRANG EDIT_PRODUCT
    }

    // Tiếp tục kinh doanh - REDIRECT VỀ TRANG EDIT_PRODUCT
    @GetMapping("/restore/{variantId}")
    public String restoreVariant(@PathVariable("productId") Integer productId,
                                 @PathVariable("variantId") Integer variantId,
                                 RedirectAttributes redirectAttributes) {
        try {
            ProductVariant variant = productVariantService.findById(variantId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với id: " + variantId));
            variant.setTrangThai("Đang kinh doanh");
            productVariantService.save(variant);
            redirectAttributes.addFlashAttribute("success", "Biến thể đã được tiếp tục kinh doanh!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tiếp tục kinh doanh biến thể: " + e.getMessage());
        }
        return "redirect:/admin/products/edit/" + productId; // REDIRECT VỀ TRANG EDIT_PRODUCT
    }
}