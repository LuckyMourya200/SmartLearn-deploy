package com.learningplatform.app.smart_learn.adminController;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.model.LearningContentDTO;
import com.learningplatform.app.smart_learn.repos.CourseRepository;
import com.learningplatform.app.smart_learn.service.LearningContentService;
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
@RequestMapping("/admin/learningContents")
public class LearningContentController {

    private final LearningContentService learningContentService;
    private final CourseRepository courseRepository;

    public LearningContentController(final LearningContentService learningContentService,
            final CourseRepository courseRepository) {
        this.learningContentService = learningContentService;
        this.courseRepository = courseRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("courseValues", courseRepository.findAll(Sort.by("courseId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Course::getCourseId, Course::getCourseTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("learningContents", learningContentService.findAll());
        return "learningContent/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("learningContent") final LearningContentDTO learningContentDTO) {
        return "learningContent/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("learningContent") @Valid final LearningContentDTO learningContentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "learningContent/add";
        }
        learningContentService.create(learningContentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("learningContent.create.success"));
        return "redirect:/admin/learningContents";
    }

    @GetMapping("/edit/{contentId}")
    public String edit(@PathVariable(name = "contentId") final Integer contentId,
            final Model model) {
        model.addAttribute("learningContent", learningContentService.get(contentId));
        return "learningContent/edit";
    }

    @PostMapping("/edit/{contentId}")
    public String edit(@PathVariable(name = "contentId") final Integer contentId,
            @ModelAttribute("learningContent") @Valid final LearningContentDTO learningContentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "learningContent/edit";
        }
        learningContentService.update(contentId, learningContentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("learningContent.update.success"));
        return "redirect:/admin/learningContents";
    }

    @PostMapping("/delete/{contentId}")
    public String delete(@PathVariable(name = "contentId") final Integer contentId,
            final RedirectAttributes redirectAttributes) {
        learningContentService.delete(contentId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("learningContent.delete.success"));
        return "redirect:/admin/learningContents";
    }

}
