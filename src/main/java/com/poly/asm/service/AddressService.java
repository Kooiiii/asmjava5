package com.poly.asm.service;

import com.poly.asm.entity.Address;
import java.util.List;

public interface AddressService {
    List<Address> findByUserId(Integer userId);
    Address save(Address address, Integer userId);
    void deleteById(Integer id);
    Address findById(Integer id);
}