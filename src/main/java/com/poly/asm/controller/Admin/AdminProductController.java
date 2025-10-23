package com.poly.asm.controller.Admin;

import com.poly.asm.entity.Product;
import com.poly.asm.entity.ProductVariant;
import com.poly.asm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private BrandService brandService;
    @Autowired private ColorService colorService;
    @Autowired private SizeService sizeService;
    @Autowired private UserService userService;

    private void addAdminUserToModel(Model model, Principal principal) {
        if (principal == null) return;
        userService.findByUsername(principal.getName()).ifPresent(user -> model.addAttribute("user", user));
    }

    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(required = false) String brandId,
                               @RequestParam(required = false) String categoryId,
                               Model model, Principal principal) {
        addAdminUserToModel(model, principal);

        Integer brand = (brandId != null && !brandId.isEmpty()) ? Integer.parseInt(brandId) : null;
        Integer category = (categoryId != null && !categoryId.isEmpty()) ? Integer.parseInt(categoryId) : null;

        int pageSize = 9;
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Product> productPage = (brand == null && category == null)
                ? productService.findAll(pageable)
                : productService.findByBrandAndCategory(brand, category, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("user", userService.findByUsername(principal.getName()).orElse(null));

        return "products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        Product product = new Product();
        product.ensurePrimaryVariant(); // prepare variants[0] for form binding
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/add_product";
    }

    @PostMapping("/add")
    public String saveProduct(@ModelAttribute("product") Product product,
                              RedirectAttributes redirectAttrs,
                              Model model,
                              Principal principal) {
        addAdminUserToModel(model, principal);

        if (product.getVariants() != null) {
            for (ProductVariant v : product.getVariants()) {
                if (v.getColor() == null || v.getSize() == null) {
                    redirectAttrs.addFlashAttribute("error", "Please select color and size for the variant.");
                    return "redirect:/admin/products/add";
                }
                v.setProduct(product);
            }
        }

        try {
            productService.save(product);
            redirectAttrs.addFlashAttribute("success", "Product saved.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttrs.addFlashAttribute("error", "Database error: " + ex.getMostSpecificCause().getMessage());
            return "redirect:/admin/products/add";
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        Product product = productService.findById(id);
        product.ensurePrimaryVariant();
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("colors", colorService.findAll());
        model.addAttribute("sizes", sizeService.findAll());
        return "admin/edit_product";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product,
                                RedirectAttributes redirectAttrs,
                                Model model,
                                Principal principal) {
        addAdminUserToModel(model, principal);

        if (product.getVariants() != null) {
            for (ProductVariant v : product.getVariants()) {
                if (v.getColor() == null || v.getSize() == null) {
                    redirectAttrs.addFlashAttribute("error", "Please select color and size for the variant.");
                    return "redirect:/admin/products/edit/" + (product.getId() != null ? product.getId() : "");
                }
                v.setProduct(product);
            }
        }

        try {
            productService.save(product);
            redirectAttrs.addFlashAttribute("success", "Product updated.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttrs.addFlashAttribute("error", "Database error: " + ex.getMostSpecificCause().getMessage());
            return "redirect:/admin/products/edit/" + (product.getId() != null ? product.getId() : "");
        }
        return "redirect:/admin/products/edit/" + (product.getId() != null ? product.getId() : "");
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Principal principal, Model model, RedirectAttributes redirectAttrs) {
        addAdminUserToModel(model, principal);
        try {
            productService.delete(id);
            redirectAttrs.addFlashAttribute("success", "Product deleted.");
        } catch (IllegalStateException | DataIntegrityViolationException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/products";
    }
}
