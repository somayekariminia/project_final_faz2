package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.InfoCard;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.service.CreditServiceImpl;
import ir.maktab.project_final_faz2.service.OrderCustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@CrossOrigin
public class CreditCardController {
    @Autowired
    private CreditServiceImpl creditService;
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;


    @PostMapping("/paymentOfCard")
    public void paymentOfCard(@Valid @ModelAttribute("infoCard") InfoCard infoCard, HttpServletRequest request) {
        System.out.println(request.getSession().getAttribute("captcha"));
        if (!infoCard.getCaptcha().equalsIgnoreCase((String) request.getSession().getAttribute("captcha")))
          throw new ValidationException("captcha not is  valid");
        Long id = Long.parseUnsignedLong(infoCard.getOrderId());
        OrderCustomer orderCustomer = orderCustomerService.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(infoCard.getDateExpired(), formatter);
        creditService.checkCredit(localDate, orderCustomer);
    }
}
