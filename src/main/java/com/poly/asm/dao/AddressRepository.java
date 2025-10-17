package com.poly.asm.dao;

import com.poly.asm.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    // Tìm tất cả địa chỉ của một người dùng theo id của họ
    List<Address> findByUserId(Integer userId);
}