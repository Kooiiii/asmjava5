package com.poly.asm.service;

import com.poly.asm.entity.Brand;
import java.util.List;

public interface BrandService {
    List<Brand> findAll();
    Brand findById(Integer id);
    Brand save(Brand brand);
    void deleteById(Integer id);
}