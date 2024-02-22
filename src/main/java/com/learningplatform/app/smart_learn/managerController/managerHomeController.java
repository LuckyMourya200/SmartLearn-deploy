package com.learningplatform.app.smart_learn.managerController;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.learningplatform.app.smart_learn.domain.LearningContent;
import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.model.CourseDTO;
import com.learningplatform.app.smart_learn.model.LearningContentDTO;
import com.learningplatform.app.smart_learn.repos.CourseRepository;
import com.learningplatform.app.smart_learn.repos.LearningContentRepository;
import com.learningplatform.app.smart_learn.repos.UserRepository;
import com.learningplatform.app.smart_learn.service.CourseService;
import com.learningplatform.app.smart_learn.service.LearningContentService;
import com.learningplatform.app.smart_learn.util.WebUtils;

import jakarta.validation.Valid;

@SuppressWarnings("null")
@Controller
@RequestMapping("/manager")
public class managerHomeController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getManager() {
        return "managerHome/manager";
    }

    @GetMapping("/profile")
    public String getUserprofile(Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).get();
        m.addAttribute("obj", user);
        return "managerHome/managerProfile";
    }

    @GetMapping("/courses")
    public String list(final Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "managerHome/list";
    }

    @GetMapping("/courses/add")
    public String add(@ModelAttribute("course") final CourseDTO courseDTO) {
        return "managerHome/add";
    }

    @PostMapping("/courses/add")
    public String add(@ModelAttribute("course") @Valid final CourseDTO courseDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "managerHome/add";
        }
        courseService.create(courseDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("course.create.success"));
        return "redirect:/manager/courses";
    }

    @GetMapping("/courses/edit/{courseId}")
    public String edit(@PathVariable(name = "courseId") final Integer courseId, final Model model) {
        model.addAttribute("course", courseService.get(courseId));
        return "managerHome/edit";
    }

    @PostMapping("/courses/edit/{courseId}")
    public String edit(@PathVariable(name = "courseId") final Integer courseId,
            @ModelAttribute("course") @Valid final CourseDTO courseDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "managerHome/edit";
        }
        courseService.update(courseId, courseDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("course.update.success"));
        return "redirect:/manager/courses";
    }

    // @PostMapping("/courses/delete/{courseId}")
    // public String delete(@PathVariable(name = "courseId") final Integer courseId,
    // final RedirectAttributes redirectAttributes) {
    // final ReferencedWarning referencedWarning =
    // courseService.getReferencedWarning(courseId);
    // if (referencedWarning != null) {
    // redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
    // WebUtils.getMessage(referencedWarning.getKey(),
    // referencedWarning.getParams().toArray()));
    // } else {
    // courseService.delete(courseId);
    // redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO,
    // WebUtils.getMessage("course.delete.success"));
    // }
    // return "redirect:/manager/courses";
    // }

    @Autowired
    LearningContentRepository learningContentRepository;

    @Autowired
    LearningContentService learningContentService;

    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/LearningContent/{courseId}")
    public String getmanageCourseData(@PathVariable(name = "courseId") final Integer courseId, final Model model) {
        System.out.println("Course number: " + courseId);
        model.addAttribute("courseObj", courseRepository.findById(courseId).get());
        model.addAttribute("learningContentList",
                courseRepository.findById(courseId).get().getCourseLearningContents().stream()
                        .map((obj) -> learningContentService.mapToDTO(obj, new LearningContentDTO()))
                        .collect(Collectors.toList()));
        LearningContentDTO contentDTO = new LearningContentDTO();
        contentDTO.setCourse(courseId);
        model.addAttribute("obj", contentDTO);
        return "managerHome/learningContent";
    }

    @PostMapping("/LearningContent")
    public String addFolderData(@ModelAttribute("obj") LearningContentDTO learningContentDTO,
            final RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) throws IOException {
        try {
            LearningContent learningContent = new LearningContent();

            learningContent = learningContentService.mapToEntity(learningContentDTO, learningContent);
            learningContent.setUnit(learningContentDTO.getUnit());
            if (!file.isEmpty()) {
                learningContent.setPostImage(file.getBytes());
            }
            learningContentRepository.save(learningContent);
            System.out.println("i an here");
        } catch (Exception e) {
        }
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                WebUtils.getMessage("Course Added successfully"));
        return "redirect:/manager/LearningContent/" + learningContentDTO.getCourse();
    }

    // @GetMapping("/LearningContent/delete/{imageId}")
    // public String deleteFolderImage(@PathVariable Integer imageId, final
    // RedirectAttributes redirectAttributes) {
    // imageRepository.deleteById(imageId);
    // redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
    // WebUtils.getMessage("Delete data successfully"));
    // return "redirect:/user/folders";
    // }

    @GetMapping("/LearningContent/image/{id}")
    public ResponseEntity<byte[]> getPost(@PathVariable("id") int id, Model model) {
        LearningContent post = learningContentRepository.findById(id).get();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(post.getPostImage());
    }
}
