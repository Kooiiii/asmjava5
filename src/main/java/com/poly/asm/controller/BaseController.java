package com.poly.asm.controller;

import com.poly.asm.config.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
public class BaseController {

    protected final UserSession userSession;

    @ModelAttribute
    public void addUserToModel(Model model) {
        model.addAttribute("user", userSession.getCurrentUser());
    }
}