package studio.alpacode.app.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import studio.alpacode.app.domain.Role;
import studio.alpacode.app.domain.User;
import studio.alpacode.app.service.UserService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Dashboard");

        // Route based on user role
        if (user != null && user.getRole() == Role.CUSTOMER) {
            model.addAttribute("currentPage", "home");
            return "dashboard/customer";
        }

        model.addAttribute("currentPage", "dashboard");
        return "dashboard/index";
    }

    // HTMX endpoints for customer dashboard sections
    @GetMapping("/section/{section}")
    public String getSection(@PathVariable String section, Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        model.addAttribute("user", user);

        return switch (section) {
            case "home" -> "dashboard/sections/home";
            case "fatture" -> "dashboard/sections/fatture";
            case "risorse" -> "dashboard/sections/risorse";
            case "progetti" -> "dashboard/sections/progetti";
            case "abbonamenti" -> "dashboard/sections/abbonamenti";
            default -> "dashboard/sections/home";
        };
    }
}
