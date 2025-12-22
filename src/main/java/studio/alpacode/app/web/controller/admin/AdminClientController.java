package studio.alpacode.app.web.controller.admin;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import studio.alpacode.app.domain.User;
import studio.alpacode.app.service.UserService;
import studio.alpacode.app.web.dto.InviteForm;

import java.util.List;

@Controller
@RequestMapping("/admin/clients")
public class AdminClientController {

    private final UserService userService;

    public AdminClientController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listClients(Authentication authentication, Model model) {
        List<User> clients = userService.findAllClients();
        User currentUser = userService.findByEmail(authentication.getName()).orElse(null);

        model.addAttribute("clients", clients);
        model.addAttribute("user", currentUser);
        model.addAttribute("inviteForm", new InviteForm());
        model.addAttribute("pageTitle", "Gestione Clienti");
        model.addAttribute("currentPage", "clients");

        return "admin/clients/index";
    }

    @PostMapping("/invite")
    public String inviteClient(
            @Valid @ModelAttribute("inviteForm") InviteForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Check if email already exists
        if (userService.emailExists(form.getEmail())) {
            bindingResult.rejectValue("email", "error.email",
                    "Un utente con questa email esiste già");
        }

        if (bindingResult.hasErrors()) {
            List<User> clients = userService.findAllClients();
            User currentUser = userService.findByEmail(authentication.getName()).orElse(null);

            model.addAttribute("clients", clients);
            model.addAttribute("user", currentUser);
            model.addAttribute("pageTitle", "Gestione Clienti");
            model.addAttribute("currentPage", "clients");
            model.addAttribute("showInviteModal", true);

            return "admin/clients/index";
        }

        try {
            userService.inviteClient(form.getEmail(), form.getName(), form.getCompanyName());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Invito inviato con successo a " + form.getEmail());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/clients";
    }

    @PostMapping("/{id}/disable")
    public String disableClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.disableClient(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cliente disabilitato con successo");
        return "redirect:/admin/clients";
    }

    @PostMapping("/{id}/enable")
    public String enableClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.enableClient(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cliente riabilitato con successo");
        return "redirect:/admin/clients";
    }

    @PostMapping("/{id}/resend-invite")
    public String resendInvite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.resendInvite(id);
        redirectAttributes.addFlashAttribute("successMessage", "Invito rinviato con successo");
        return "redirect:/admin/clients";
    }
}
