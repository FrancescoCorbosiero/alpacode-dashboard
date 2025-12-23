package studio.alpacode.app.web.controller.customer;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import studio.alpacode.app.domain.Invoice;
import studio.alpacode.app.domain.InvoiceStatus;
import studio.alpacode.app.domain.User;
import studio.alpacode.app.service.InvoiceService;
import studio.alpacode.app.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoicesController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    public InvoicesController(UserService userService, InvoiceService invoiceService) {
        this.userService = userService;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public String invoices(Authentication authentication, Model model,
                          @RequestParam(required = false) Integer year,
                          @RequestParam(required = false) String status) {
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        List<Invoice> invoices = invoiceService.findByUserIdFiltered(user.getId(), year, status);
        List<Integer> availableYears = invoiceService.findDistinctYearsByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("invoices", invoices);
        model.addAttribute("availableYears", availableYears);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", InvoiceStatus.values());
        model.addAttribute("pageTitle", "Fatture");
        model.addAttribute("currentPage", "invoices");

        return "customer/invoices";
    }
}
