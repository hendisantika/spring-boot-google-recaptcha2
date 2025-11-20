package com.hendisantika.recaptcha3.controller;

import com.hendisantika.recaptcha3.config.RecaptchaConfig;
import com.hendisantika.recaptcha3.dto.EmployeeDTO;
import com.hendisantika.recaptcha3.entity.Employee;
import com.hendisantika.recaptcha3.service.EmployeeService;
import com.hendisantika.recaptcha3.service.RecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/11/23
 * Time: 05:30
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final RecaptchaService recaptchaService;

    private final RecaptchaConfig recaptchaConfig;

    @GetMapping(path = {"/", "/all"})
    public String showAll(Model model) {

        List<Employee> employeeEntityList = employeeService.findAll();

        model.addAttribute("employees", employeeEntityList);

        return "index";
    }

    @GetMapping("/create/form")
    public String createForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("recaptchaSiteKey", recaptchaConfig.getSiteKey());
        return "form";
    }

    @PostMapping("/create/process")
    public String createProcess(@ModelAttribute(name = "employee") EmployeeDTO employeeDTO,
                                @RequestParam(name = "g-recaptcha-response") String captchaToken,
                                Model model) {

        // Validate reCAPTCHA v3 token with expected action
        boolean captchaValid = recaptchaService.validateRecaptcha(captchaToken, "submit");

        if (captchaValid) {
            Employee employeeEntity = Employee.builder()
                    .name(employeeDTO.getName())
                    .lastName(employeeDTO.getLastName())
                    .dateOfBirth(employeeDTO.getDateOfBirth())
                    .build();

            employeeService.createEmployee(employeeEntity);
            return "redirect:/all";
        } else {
            model.addAttribute("message", "reCAPTCHA validation failed. You appear to be a bot. Please try again.");
            return "error";
        }
    }
}
