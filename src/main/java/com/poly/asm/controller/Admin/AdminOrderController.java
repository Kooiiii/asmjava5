package com.poly.asm.controller.Admin; // Package Admin

import com.poly.asm.entity.Order;
import com.poly.asm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasAuthority('Admin')") // Đảm bảo chỉ Admin vào được
public class AdminOrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping
    public String listAllOrders(Model model, @RequestParam(required = false) String status) {
        List<Order> orders;

        if (status != null && !status.isEmpty() && !status.equals("")) {

            orders = orderService.findAllOrders().stream()
                    .filter(order -> order.getStatus().equalsIgnoreCase(status))
                    .toList();
            model.addAttribute("currentStatus", status);
        } else {
            orders = orderService.findAllOrders();
            model.addAttribute("currentStatus", "");
        }

        model.addAttribute("orders", orders != null ? orders : new ArrayList<>());
        return "admin/order_list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable("id") Integer orderId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.findById(orderId);
            model.addAttribute("order", order);
            return "admin/order_detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: Không tìm thấy đơn hàng #" + orderId + ". " + e.getMessage());
            return "redirect:/admin/orders";
        }
    }


    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("orderId") Integer orderId,
                                    @RequestParam("status") String newStatus, // Trạng thái mới từ form
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, newStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái đơn hàng #" + orderId + " thành công!");
        } catch (RuntimeException e) { // Bắt lỗi cụ thể hơn nếu cần
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cập nhật trạng thái: " + e.getMessage());
        }
        // Quay lại trang chi tiết đơn hàng vừa cập nhật
        return "redirect:/admin/orders/" + orderId;
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteById(id); // Gọi service xóa
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đơn hàng #" + id + " thành công!");
        } catch (Exception e) { // Bắt lỗi chung
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đơn hàng #" + id + ": " + e.getMessage());
        }
        // Sau khi xóa thì quay về trang danh sách
        return "redirect:/admin/orders";
    }
}