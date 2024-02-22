package com.learningplatform.app.smart_learn.adminController;

import com.learningplatform.app.smart_learn.model.CourseDTO;
import com.learningplatform.app.smart_learn.service.CourseService;
import com.learningplatform.app.smart_learn.util.ReferencedWarning;
import com.learningplatform.app.smart_learn.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/admin/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(final CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "course/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("course") final CourseDTO courseDTO) {
        return "course/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("course") @Valid final CourseDTO courseDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "course/add";
        }
        courseService.create(courseDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("course.create.success"));
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{courseId}")
    public String edit(@PathVariable(name = "courseId") final Integer courseId, final Model model) {
        model.addAttribute("course", courseService.get(courseId));
        return "course/edit";
    }

    @PostMapping("/edit/{courseId}")
    public String edit(@PathVariable(name = "courseId") final Integer courseId,
            @ModelAttribute("course") @Valid final CourseDTO courseDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "course/edit";
        }
        courseService.update(courseId, courseDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("course.update.success"));
        return "redirect:/admin/courses";
    }

    @PostMapping("/delete/{courseId}")
    public String delete(@PathVariable(name = "courseId") final Integer courseId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = courseService.getReferencedWarning(courseId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            courseService.delete(courseId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("course.delete.success"));
        }
        return "redirect:/admin/courses";
    }

}
