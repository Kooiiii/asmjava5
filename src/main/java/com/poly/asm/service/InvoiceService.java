package com.poly.asm.service;

import com.poly.asm.dao.*;
import com.poly.asm.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository; // Thêm dependency này

    @Transactional
    public Invoice createInvoiceFromCart(Integer customerId, String paymentMethod) {
        User customer = userRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        // Tạo hóa đơn
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setStaff(userRepository.findById(2).orElse(null)); // staff1
        invoice.setCreatedDate(LocalDateTime.now());
        invoice.setPaymentMethod(paymentMethod);
        invoice.setStatus("Chờ duyệt");

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Tạo chi tiết hóa đơn
        for (CartItem cartItem : cartItems) {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice(savedInvoice);
            invoiceItem.setProductVariant(cartItem.getProductVariant());
            invoiceItem.setQuantity(cartItem.getQuantity());
            invoiceItem.setUnitPrice(cartItem.getProductVariant().getPrice());
            invoiceItemRepository.save(invoiceItem);
        }

        // Xóa giỏ hàng
        cartItemRepository.deleteByCustomerId(customerId);

        return savedInvoice;
    }

    public Invoice getInvoiceByIdAndCustomer(Integer invoiceId, Integer customerId) {
        Invoice invoice = invoiceRepository.findByIdAndCustomerId(invoiceId, customerId)
            .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));
        
        // Load invoice items để tính tổng tiền
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findByInvoiceId(invoiceId);
        invoice.setInvoiceItems(invoiceItems);
        
        return invoice;
    }
}