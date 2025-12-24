package studio.alpacode.app.web.controller.customer;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import studio.alpacode.app.domain.User;
import studio.alpacode.app.service.UserService;

@Controller
@RequestMapping("/resources")
public class ResourcesController {

    private final UserService userService;

    public ResourcesController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String resources(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Risorse");
        model.addAttribute("currentPage", "resources");

        return "customer/resources";
    }
}
