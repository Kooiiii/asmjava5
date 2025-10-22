package com.poly.asm.service;

import com.poly.asm.entity.Color;
import java.util.List;

public interface ColorService {
    List<Color> findAll();

    Color findById(Integer id);
}
