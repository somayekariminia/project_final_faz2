package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.InfoCard;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.mapper.MapStructMapper;
import ir.maktab.project_final_faz2.service.CreditServiceImpl;
import ir.maktab.project_final_faz2.service.CustomerServiceImpl;
import ir.maktab.project_final_faz2.service.OrderCustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
public class CreditCardController {
    @Autowired
   private CreditServiceImpl creditService;
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;


    @PostMapping("/paymentOfCard")
    public String paymentOfCard(@Valid @ModelAttribute("infoCard") InfoCard infoCard, HttpServletRequest request) {
        System.out.println(request.getSession().getAttribute("captcha"));
        if (!infoCard.getCaptcha().equalsIgnoreCase((String) request.getSession().getAttribute("captcha")))
            return "redirect:/";
        Long id=Long.parseUnsignedLong(infoCard.getOrderId());
        OrderCustomer orderCustomer = orderCustomerService.findById(id);
        LocalDate localDate=LocalDate.parse(infoCard.getDateExpired());
        creditService.checkCredit( localDate,orderCustomer);
        return "ok";
    }
}
