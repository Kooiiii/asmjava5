package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Color;
import com.poly.asm.entity.Product;
import com.poly.asm.entity.ProductVariant;
import com.poly.asm.entity.Size;
import com.poly.asm.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserSession userSession;

    @Autowired private CategoryService categoryService;
    @Autowired private BrandService brandService;
    @Autowired private ColorService colorService;
    @Autowired private SizeService sizeService;

    @GetMapping("/products")
    public String products(@RequestParam(required = false) Integer brandId,
                           @RequestParam(required = false) Integer categoryId,
                           Model model) {

        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());

        List<Product> products;

        if (brandId != null && categoryId != null) {
            products = productService.findByBrandAndCategory(brandId, categoryId);
        } else if (brandId != null) {
            products = productService.findByBrandId(brandId);
        } else if (categoryId != null) {
            products = productService.findByCategoryId(categoryId);
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedBrand", brandId);
        model.addAttribute("selectedCategory", categoryId);

        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }

        // Danh sách biến thể
        List<ProductVariant> variants = product.getVariants();

        // Lọc các màu khả dụng
        Set<Color> availableColors = variants.stream()
                .map(ProductVariant::getColor)
                .collect(Collectors.toSet());

        // Lọc các kích thước khả dụng
        Set<Size> availableSizes = variants.stream()
                .map(ProductVariant::getSize)
                .collect(Collectors.toSet());

        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("product", product);
        model.addAttribute("availableColors", availableColors);
        model.addAttribute("availableSizes", availableSizes);

        // Gửi luôn danh sách variants để JS xử lý chọn màu / size
        model.addAttribute("variants", variants);

        return "product_detail"; // khớp với file product-detail.html
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("keyword", keyword);
        return "product-list";
    }
}
