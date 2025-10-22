package com.poly.asm.service.impl;

import com.poly.asm.dao.ColorRepository;
import com.poly.asm.entity.Color;
import com.poly.asm.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public Color findById(Integer id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found with id: " + id));
    }
}
