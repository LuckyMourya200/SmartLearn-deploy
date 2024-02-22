package com.learningplatform.app.smart_learn.adminController;

import com.learningplatform.app.smart_learn.domain.Role;
import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.loginController.Constant;
import com.learningplatform.app.smart_learn.model.UserDTO;
import com.learningplatform.app.smart_learn.repos.RoleRepository;
import com.learningplatform.app.smart_learn.repos.UserRepository;
import com.learningplatform.app.smart_learn.service.RoleService;
import com.learningplatform.app.smart_learn.service.UserService;
import com.learningplatform.app.smart_learn.util.CustomCollectors;
import com.learningplatform.app.smart_learn.util.ReferencedWarning;
import com.learningplatform.app.smart_learn.util.WebUtils;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(final UserService userService, final RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleIdValues", roleRepository.findAll(Sort.by("roleId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Role::getRoleId, Role::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable(name = "userId") final Integer userId, final Model model) {
        model.addAttribute("user", userService.get(userId));
        model.addAttribute("roles", roleService.findAll());
        return "user/edit";
    }

    @PostMapping("/edit/{userId}")
    public String edit(@PathVariable(name = "userId") final Integer userId,
            @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {

        User temp = userRepository.findById(userId).get();
        if (!userDTO.getRoleId().isEmpty()) {
            System.out.println("user id : " + userId);
            System.out.println("roles :" + userDTO.getRoleId());
            System.out.println("working id is here ");
            Set<Role> roleSet = userDTO.getRoleId().stream()
                    .map(roleId -> roleRepository.findById(roleId).get())
                    .collect(Collectors.toSet());
            temp.setRoleId(roleSet);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                    WebUtils.getMessage("Role assigned successfully"));

        } else {
            Role roleUser = roleRepository.findById(Constant.ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("User role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(roleUser);
            temp.setRoleId(roles);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO,
                    WebUtils.getMessage(
                            "Role assigned successfully and user role is given bkz you can't give null role to the user"));

        }
        userRepository.save(temp);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{userId}")
    public String delete(@PathVariable(name = "userId") final Integer userId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = userService.getReferencedWarning(userId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            userService.delete(userId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("user.delete.success"));
        }
        return "redirect:/admin/users";
    }

}
