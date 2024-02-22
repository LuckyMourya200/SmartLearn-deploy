package com.learningplatform.app.smart_learn.loginController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.repos.CourseRepository;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/")
    public String getHome() {
        return "Home/home";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "Home/about";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "Home/contact";
    }

    @GetMapping("/courses")
    public String getCoCoursesntact(Model model) {
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
        return "Home/Courses";
    }

}
