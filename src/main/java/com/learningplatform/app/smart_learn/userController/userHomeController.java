package com.learningplatform.app.smart_learn.userController;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.domain.LearningContent;
import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.domain.UserProgress;
import com.learningplatform.app.smart_learn.repos.CourseRepository;
import com.learningplatform.app.smart_learn.repos.LearningContentRepository;
import com.learningplatform.app.smart_learn.repos.UserProgressRepository;
import com.learningplatform.app.smart_learn.repos.UserRepository;
import com.learningplatform.app.smart_learn.util.WebUtils;

@SuppressWarnings("null")
@Controller
@RequestMapping("/user")
public class userHomeController {

    @Autowired
    UserProgressRepository userProgressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    LearningContentRepository learningContentRepository;

    @GetMapping
    public String getUser() {
        return "userHome/user";
    }

    @GetMapping("/explore")
    public String exploreCoursesle(Model model) {
        List<Course> allCourses = courseRepository.findAll();

        List<Course> programmingCourses = allCourses.stream()
                .filter(course -> "Programming Courses".equals(course.getCourseType()))
                .collect(Collectors.toList());

        List<Course> csCourses = allCourses.stream()
                .filter(course -> "Computer Science Courses".equals(course.getCourseType()))
                .collect(Collectors.toList());

        List<Course> webDevelopmentCourses = allCourses.stream()
                .filter(course -> "Web Development Courses".equals(course.getCourseType()))
                .collect(Collectors.toList());

        List<Course> otherCourses = allCourses.stream()
                .filter(course -> "Other Courses".equals(course.getCourseType()))
                .collect(Collectors.toList());

        model.addAttribute("programming", programmingCourses);
        model.addAttribute("cs", csCourses);
        model.addAttribute("web", webDevelopmentCourses);
        model.addAttribute("other", otherCourses);

        return "userHome/exploreCourses";
    }

    @GetMapping("/detail/{courseId}")
    public String getUsercousrsDetail(Model m, @PathVariable(name = "courseId") final Integer courseId) {
        m.addAttribute("obj", courseRepository.findById(courseId).get());
        return "userHome/courseDetails";
    }

    @GetMapping("/profile")
    public String getUserprofile(Model m, final RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            m.addAttribute("user", user);
            m.addAttribute("courses", user.getUserUserProgresses().size());
            m.addAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("Welcome, " + user.getUsername() + "!"));
            return "userHome/userProfile";
        } else {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage("User not found. Please log in. Something went wrong."));
            return "redirect:/user";
        }
    }

    @GetMapping("/enroll/{courseId}")
    public String getUsenrollDetail(Model m, @PathVariable(name = "courseId") final Integer courseId,
            final RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage("User not found. Please log in."));
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO,
                    WebUtils.getMessage("Redirecting to the login page..."));
            return "redirect:/login";
        }

        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage("Course not found. Please try again."));
            return "redirect:/user";
        }

        Optional<UserProgress> optional = userProgressRepository.findByUserAndCourse(user, course);

        if (optional.isPresent()) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage("You have already bought this course."));
        } else {
            UserProgress progress = new UserProgress();
            progress.setCourse(course);
            progress.setUser(user);
            progress.setCompletionStatus(false);
            progress.setProgress(0);
            userProgressRepository.save(progress);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                    WebUtils.getMessage("Successfully enrolled in the course."));
        }
        return "redirect:/user";
    }

    @GetMapping("/myCourses")
    public String getUsercousrsmyCourses(Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        if (user == null) {
            m.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("User not found. Please log in."));
            m.addAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("Redirecting to the login page..."));
            return "redirect:/login";
        }
        List<Course> myCourses = user.getUserUserProgresses()
                .stream()
                .map(obj -> obj.getCourse())
                .collect(Collectors.toList());

        if (myCourses.isEmpty()) {
            m.addAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("You have no enrolled courses."));
        } else {
            m.addAttribute("mycourses", myCourses);
        }
        return "userHome/myCourses";
    }

    @GetMapping("/learningContent/{courseId}")
    public String getLearningContent(Model model, @PathVariable(name = "courseId") final Integer courseId,
            @RequestParam(defaultValue = "0") int page, final RedirectAttributes redirectAttributes) {
        int pageSize = 1;

        Course course = courseRepository.findById(courseId).orElse(null);

        if (course == null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("Error, something is wrong."));
            return "redirect:/user"; // You can replace "errorPage" with the actual error page name
        }

        model.addAttribute("courseObject", course);

        List<LearningContent> x = learningContentRepository.findByCourse(course);
        Page<LearningContent> learningContentPage = new PageImpl<>(x, PageRequest.of(page, pageSize), x.size());

        model.addAttribute("learningContentPage", learningContentPage.getContent().get(page));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", learningContentPage.getTotalPages());

        int nextPage = page + 1;
        int previousPage = page - 1;
        int firstPage = 0;
        int lastPage = learningContentPage.getTotalPages() - 1;

        model.addAttribute("firstPageUrl", "/user/learningContent/" + courseId + "?page=" + firstPage);
        model.addAttribute("previousPageUrl", "/user/learningContent/" + courseId + "?page=" + previousPage);
        model.addAttribute("nextPageUrl", "/user/learningContent/" + courseId + "?page=" + nextPage);
        model.addAttribute("lastPageUrl", "/user/learningContent/" + courseId + "?page=" + lastPage);

        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(lastPage, page + 4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "userHome/userLearningContent";
    }

}
