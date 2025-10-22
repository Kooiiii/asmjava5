package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import com.poly.asm.entity.User;
import com.poly.asm.entity.Address;
import com.poly.asm.service.InvoiceService;
import com.poly.asm.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserSession userSession;
    private final AddressService addressService;

    @GetMapping("/create")
    public String createInvoiceFromCart() {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        try {
            var invoice = invoiceService.createInvoiceFromCart(currentUser.getId(), "COD");
            return "redirect:/invoice/" + invoice.getId();
        } catch (Exception e) {
            return "redirect:/cart?error=" + e.getMessage();
        }
    }

    @GetMapping("/{id}")
    public String viewInvoice(@PathVariable("id") Integer invoiceId, Model model) {
        User currentUser = userSession.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        try {
            var invoice = invoiceService.getInvoiceByIdAndCustomer(invoiceId, currentUser.getId());
            
            // Lấy địa chỉ từ AddressService hiện có
            String customerAddress = getDefaultAddress(currentUser.getId());
            
            model.addAttribute("invoice", invoice);
            model.addAttribute("user", currentUser);
            model.addAttribute("customerAddress", customerAddress);
            
            return "invoice";
        } catch (Exception e) {
            return "redirect:/cart?error=Hóa đơn không tồn tại";
        }
    }

    // Sửa method helper để lấy địa chỉ mặc định
    private String getDefaultAddress(Integer userId) {
        List<Address> addresses = addressService.findByUserId(userId);
        if (addresses.isEmpty()) {
            return "Chưa có địa chỉ";
        }
        
        // Tìm địa chỉ mặc định - sử dụng getMacDinh() thay vì getIsDefault()
        Address defaultAddress = addresses.stream()
            .filter(address -> {
                // Kiểm tra cả 2 trường hợp vì có thể tên method khác
                try {
                    if (address.getClass().getMethod("getMacDinh") != null) {
                        Boolean macDinh = (Boolean) address.getClass().getMethod("getMacDinh").invoke(address);
                        return macDinh != null && macDinh;
                    }
                } catch (Exception e) {
                    // Nếu không có getMacDinh, thử getIsDefault
                    try {
                        if (address.getClass().getMethod("getIsDefault") != null) {
                            Boolean isDefault = (Boolean) address.getClass().getMethod("getIsDefault").invoke(address);
                            return isDefault != null && isDefault;
                        }
                    } catch (Exception ex) {
                        // Nếu không có cả hai, lấy địa chỉ đầu tiên
                    }
                }
                return false;
            })
            .findFirst()
            .orElse(addresses.get(0));
        
        // Lấy địa chỉ - thử cả getDiaChi() và getAddress()
        try {
            if (defaultAddress.getClass().getMethod("getDiaChi") != null) {
                return (String) defaultAddress.getClass().getMethod("getDiaChi").invoke(defaultAddress);
            }
        } catch (Exception e) {
            try {
                if (defaultAddress.getClass().getMethod("getAddress") != null) {
                    return (String) defaultAddress.getClass().getMethod("getAddress").invoke(defaultAddress);
                }
            } catch (Exception ex) {
                return "Địa chỉ không xác định";
            }
        }
        
        return "Địa chỉ không xác định";
    }
}

