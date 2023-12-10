package com.hendisantika.recaptcha3.controller;

import com.hendisantika.recaptcha3.entity.Employee;
import com.hendisantika.recaptcha3.service.EmployeeService;
import com.hendisantika.recaptcha3.service.RecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping(path = {"/", "/all"})
    public String showAll(Model model) {

        List<Employee> employeeEntityList = employeeService.findAll();

        model.addAttribute("employees", employeeEntityList);

        return "index";
    }

}
