package com.learningplatform.app.smart_learn.adminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.repos.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/profile")
    public String getUserprofile(Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).get();
        m.addAttribute("obj", user);
        return "admin/adminProfile";
    }

}
