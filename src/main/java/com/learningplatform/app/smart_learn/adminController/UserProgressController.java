package com.learningplatform.app.smart_learn.adminController;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.model.UserProgressDTO;
import com.learningplatform.app.smart_learn.repos.CourseRepository;
import com.learningplatform.app.smart_learn.repos.UserRepository;
import com.learningplatform.app.smart_learn.service.UserProgressService;
import com.learningplatform.app.smart_learn.util.CustomCollectors;
import com.learningplatform.app.smart_learn.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/admin/userProgresses")
public class UserProgressController {

    private final UserProgressService userProgressService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public UserProgressController(final UserProgressService userProgressService,
            final UserRepository userRepository, final CourseRepository courseRepository) {
        this.userProgressService = userProgressService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUsername)));
        model.addAttribute("courseValues", courseRepository.findAll(Sort.by("courseId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Course::getCourseId, Course::getCourseTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("userProgresses", userProgressService.findAll());
        return "userProgress/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("userProgress") final UserProgressDTO userProgressDTO) {
        return "userProgress/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userProgress") @Valid final UserProgressDTO userProgressDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "userProgress/add";
        }
        userProgressService.create(userProgressDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userProgress.create.success"));
        return "redirect:/admin/userProgresses";
    }

    @GetMapping("/edit/{progressId}")
    public String edit(@PathVariable(name = "progressId") final Integer progressId,
            final Model model) {
        model.addAttribute("userProgress", userProgressService.get(progressId));
        return "userProgress/edit";
    }

    @PostMapping("/edit/{progressId}")
    public String edit(@PathVariable(name = "progressId") final Integer progressId,
            @ModelAttribute("userProgress") @Valid final UserProgressDTO userProgressDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "userProgress/edit";
        }
        userProgressService.update(progressId, userProgressDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userProgress.update.success"));
        return "redirect:/admin/userProgresses";
    }

    @PostMapping("/delete/{progressId}")
    public String delete(@PathVariable(name = "progressId") final Integer progressId,
            final RedirectAttributes redirectAttributes) {
        userProgressService.delete(progressId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("userProgress.delete.success"));
        return "redirect:/admin/userProgresses";
    }

}
