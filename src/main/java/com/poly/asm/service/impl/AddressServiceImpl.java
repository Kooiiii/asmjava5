package com.poly.asm.service.impl;

import com.poly.asm.dao.AddressRepository;
import com.poly.asm.dao.UserRepository;
import com.poly.asm.entity.Address;
import com.poly.asm.entity.User;
import com.poly.asm.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Address> findByUserId(Integer userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address save(Address address, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public void deleteById(Integer id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Address findById(Integer id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
    }
}