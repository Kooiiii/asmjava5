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

    // Danh sách biến thể của sản phẩm
    @GetMapping
    public String listVariants(@PathVariable("productId") Integer productId, Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        Product product = productService.findById(productId);
        List<ProductVariant> variants = productVariantService.findByProductId(productId);
        model.addAttribute("product", product);
        model.addAttribute("variants", variants);
        return "admin/variants"; // file HTML để hiển thị danh sách variant
    }

    // Form thêm variant mới
    @GetMapping("/add")
    public String addVariantForm(@PathVariable("productId") Integer productId, Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        ProductVariant variant = new ProductVariant();
        variant.setProduct(productService.findById(productId));
        model.addAttribute("variant", variant);
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/add_variant";
    }

    // Lưu variant mới
    @PostMapping("/add")
    public String saveVariant(@PathVariable("productId") Integer productId,
                              @ModelAttribute("variant") ProductVariant variant,
                              RedirectAttributes redirectAttrs) {
        Product product = productService.findById(productId);
        variant.setProduct(product);

        productVariantService.save(variant);
        redirectAttrs.addFlashAttribute("success", "Variant added successfully!");
        return "redirect:/admin/products/" + productId + "/variants";
    }

    // Form sửa variant
    @GetMapping("/edit/{variantId}")
    public String editVariantForm(@PathVariable("productId") Integer productId,
                                  @PathVariable("variantId") Integer variantId,
                                  Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        ProductVariant variant = productVariantService.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với id: " + variantId));
        model.addAttribute("variant", variant);
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/edit_variant";
    }

    // Cập nhật variant
    @PostMapping("/update/{variantId}")
    public String updateVariant(@PathVariable("productId") Integer productId,
                                @PathVariable("variantId") Integer variantId,
                                @ModelAttribute("variant") ProductVariant variant,
                                RedirectAttributes redirectAttrs) {
        variant.setProduct(productService.findById(productId));
        productVariantService.save(variant);
        redirectAttrs.addFlashAttribute("success", "Variant updated successfully!");
        return "redirect:/admin/products/" + productId + "/variants";
    }
    //Ngừng kinh Doanh
    @GetMapping("/delete/{variantId}")
    public String deleteVariant(@PathVariable("productId") Integer productId,
                                @PathVariable("variantId") Integer variantId,
                                RedirectAttributes redirectAttributes) {
        try {
            ProductVariant variant = productVariantService.findById(variantId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với id: " + variantId));
            variant.setTrangThai("Ngừng kinh doanh"); // soft delete
            productVariantService.save(variant);
            redirectAttributes.addFlashAttribute("success", "Biến thể đã được ngừng kinh doanh!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể ngừng kinh doanh biến thể: " + e.getMessage());
        }
        return "redirect:/admin/products/edit/" + productId;
    }
    //Tiếp tục kinh doanh
    @GetMapping("/restore/{variantId}")
    public String restoreVariant(@PathVariable("productId") Integer productId,
                                 @PathVariable("variantId") Integer variantId,
                                 RedirectAttributes redirectAttributes) {
        try {
            ProductVariant variant = productVariantService.findById(variantId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với id: " + variantId));
            variant.setTrangThai("Đang kinh doanh"); // restore
            productVariantService.save(variant);
            redirectAttributes.addFlashAttribute("success", "Biến thể đã được tiếp tục kinh doanh!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tiếp tục kinh doanh biến thể: " + e.getMessage());
        }
        return "redirect:/admin/products/edit/" + productId;
    }
}
