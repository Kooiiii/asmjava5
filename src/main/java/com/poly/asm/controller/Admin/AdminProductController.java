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
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private BrandService brandService;
    @Autowired private UserService userService;

    private void addAdminUserToModel(Model model, Principal principal) {
        userService.findByUsername(principal.getName()).ifPresent(user -> {
            model.addAttribute("user", user);
        });
    }


    @GetMapping
    public String listProducts(Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        return "add_product";
    }

    @PostMapping("/add")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        addAdminUserToModel(model, principal);
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        return "edit_product";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}