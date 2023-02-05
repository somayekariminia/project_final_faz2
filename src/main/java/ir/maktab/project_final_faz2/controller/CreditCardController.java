package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.CreditDto;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.mapper.MapStructMapper;
import ir.maktab.project_final_faz2.service.CreditServiceImpl;
import ir.maktab.project_final_faz2.service.CustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditCardController {
    @Autowired
   private CreditServiceImpl creditService;
    @Autowired
    private CustomerServiceImpl customerService;
    private String message;
    @GetMapping("/")
    public String add(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("CreditDto", new CreditDto());
        return "payment";
    }

    @GetMapping("/paymentOfCard")
    public String paymentOfCard(@ModelAttribute("creditDto") CreditDto creditDto, HttpServletRequest request) {
        if (creditDto.getCaptcha().equals(request.getSession().getAttribute("captcha"))) {
            Customer customer = customerService.findByUserName(creditDto.getUserName());
            creditService.checkCredit(MapStructMapper.INSTANCE.creditDtoToCredit(creditDto), customer);
            return "ok";
        } else {
            String message = "Please verify captcha";
            return "redirect:/";
        }
    }
}
