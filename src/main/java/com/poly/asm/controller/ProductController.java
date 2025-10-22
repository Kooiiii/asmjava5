package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.Color;
import com.poly.asm.entity.Product;
import com.poly.asm.entity.ProductVariant;
import com.poly.asm.entity.Size;
import com.poly.asm.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private UserSession userSession;
    @Autowired private CategoryService categoryService;
    @Autowired private BrandService brandService;
    @Autowired private ColorService colorService;
    @Autowired private SizeService sizeService;

    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) String brandId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        Integer brand = (brandId != null && !brandId.isEmpty()) ? Integer.parseInt(brandId) : null;
        Integer category = (categoryId != null && !categoryId.isEmpty()) ? Integer.parseInt(categoryId) : null;

        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());

        Pageable pageable = PageRequest.of(page - 1, 9);
        Page<Product> productPage;

        if (brand == null && category == null) {
            productPage = productService.findAll(pageable);
        } else if (brand != null && category == null) {
            productPage = productService.findByBrandId(brand, pageable);
        } else if (brand == null) {
            productPage = productService.findByCategoryId(category, pageable);
        } else {
            productPage = productService.findByBrandAndCategory(brand, category, pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedCategory", category);

        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }

        // Lọc chỉ các biến thể đang kinh doanh (trạng thái khác "Ngừng kinh doanh")
        List<ProductVariant> activeVariants = product.getVariants().stream()
                .filter(variant -> variant.getTrangThai() == null ||
                        !variant.getTrangThai().equals("Ngừng kinh doanh"))
                .collect(Collectors.toList());

        // Kiểm tra nếu không có biến thể nào đang kinh doanh
        if (activeVariants.isEmpty()) {
            model.addAttribute("error", "Sản phẩm hiện không khả dụng");
            return "redirect:/products";
        }

        // Lọc các màu khả dụng từ các biến thể đang kinh doanh
        Set<Color> availableColors = activeVariants.stream()
                .map(ProductVariant::getColor)
                .collect(Collectors.toSet());

        // Lọc các kích thước khả dụng từ các biến thể đang kinh doanh
        Set<Size> availableSizes = activeVariants.stream()
                .map(ProductVariant::getSize)
                .collect(Collectors.toSet());

        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("product", product);
        model.addAttribute("availableColors", availableColors);
        model.addAttribute("availableSizes", availableSizes);

        // Gửi danh sách variants đang kinh doanh để JS xử lý chọn màu / size
        model.addAttribute("variants", activeVariants);

        return "product_detail";
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String keyword,
                                 @RequestParam(defaultValue = "1") int page,
                                 Model model) {
        Pageable pageable = PageRequest.of(page - 1, 9);
        Page<Product> productPage = productService.searchByName(keyword, pageable);

        model.addAttribute("user", userSession.getCurrentUser());
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "product-list";
    }
}