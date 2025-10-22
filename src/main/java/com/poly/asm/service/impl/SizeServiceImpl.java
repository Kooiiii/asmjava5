package com.poly.asm.service.impl;

import com.poly.asm.dao.SizeRepository;
import com.poly.asm.entity.Size;
import com.poly.asm.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }
}