package com.poly.asm.controller.Admin;

import com.poly.asm.entity.Product;
import com.poly.asm.service.BrandService;
import com.poly.asm.service.CategoryService;
import com.poly.asm.service.ProductService;
import com.poly.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/products") // Đưa tất cả vào /admin/products
public class AdminProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private BrandService brandService;
    @Autowired private UserService userService;

    // Helper method
    private void addAdminUserToModel(Model model, Principal principal) {
        userService.findByUsername(principal.getName()).ifPresent(user -> {
            model.addAttribute("user", user);
        });
    }

    // GET /admin/products (Trang list dùng chung với customer)
    @GetMapping
    public String listProducts(Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        model.addAttribute("products", productService.findAll());
        return "products"; // Trả về products.html
    }

    // GET /admin/products/add
    @GetMapping("/add")
    public String addProductForm(Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        model.addAttribute("product", new Product());
        // model.addAttribute("categories", categoryService.findAll());
        // model.addAttribute("brands", brandService.findAll());
        return "add_product"; // Trả về add_product.html
    }

    // POST /admin/products/add
    @PostMapping("/add")
    public String saveProduct(@ModelAttribute("product") Product product) {
        // Cần sửa th:action trong add_product.html thành @{/admin/products/add}
        productService.save(product);
        return "redirect:/admin/products";
    }

    // GET /admin/products/edit/{id}
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        model.addAttribute("product", productService.findById(id));
        // model.addAttribute("categories", categoryService.findAll());
        // model.addAttribute("brands", brandService.findAll());
        return "edit_product"; // Trả về edit_product.html
    }

    // POST /admin/products/update
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product) {
        // Cần sửa th:action trong edit_product.html thành @{/admin/products/update}
        productService.save(product); // save() cũng dùng để update
        return "redirect:/admin/products";
    }

    // GET /admin/products/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}