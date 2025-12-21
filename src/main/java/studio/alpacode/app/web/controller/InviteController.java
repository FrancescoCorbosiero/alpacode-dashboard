package studio.alpacode.app.web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import studio.alpacode.app.domain.User;
import studio.alpacode.app.service.UserService;
import studio.alpacode.app.web.dto.SetPasswordForm;

import java.util.Optional;

@Controller
@RequestMapping("/invite")
public class InviteController {

    private final UserService userService;

    public InviteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{token}")
    public String showSetPasswordForm(@PathVariable String token, Model model) {
        Optional<User> userOpt = userService.findByInviteToken(token);

        if (userOpt.isEmpty() || !userOpt.get().isInviteTokenValid()) {
            return "invite/invalid";
        }

        User user = userOpt.get();
        model.addAttribute("user", user);
        model.addAttribute("token", token);
        model.addAttribute("setPasswordForm", new SetPasswordForm());

        return "invite/set-password";
    }

    @PostMapping("/{token}")
    public String setPassword(
            @PathVariable String token,
            @Valid @ModelAttribute("setPasswordForm") SetPasswordForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<User> userOpt = userService.findByInviteToken(token);

        if (userOpt.isEmpty() || !userOpt.get().isInviteTokenValid()) {
            return "invite/invalid";
        }

        User user = userOpt.get();

        // Validate passwords match
        if (!form.passwordsMatch()) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword",
                    "Le password non corrispondono");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("token", token);
            return "invite/set-password";
        }

        boolean activated = userService.activateAccount(token, form.getPassword());

        if (activated) {
            redirectAttributes.addFlashAttribute("message",
                    "Account attivato con successo! Ora puoi effettuare il login.");
            return "redirect:/login";
        } else {
            return "invite/invalid";
        }
    }
}
