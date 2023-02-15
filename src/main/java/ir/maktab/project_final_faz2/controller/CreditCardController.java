package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.InfoCard;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.service.impl.CreditServiceImpl;
import ir.maktab.project_final_faz2.service.impl.OrderCustomerServiceImpl;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/pay")
public class CreditCardController {

    private final CreditServiceImpl creditService;

    private final OrderCustomerServiceImpl orderCustomerService;


    @PostMapping("/paymentOfCard")
    public void paymentOfCard(@Valid @ModelAttribute("infoCard") InfoCard infoCard, HttpServletRequest request) {
        System.out.println(request.getSession().getAttribute("captcha"));
        if (!infoCard.getCaptcha().equalsIgnoreCase((String) request.getSession().getAttribute("captcha")))
            throw new ValidationException("captcha not is  valid");
        LocalDate localDate= UtilDate.getDate(infoCard.getDateExpired());
        Long id = Long.parseUnsignedLong(infoCard.getOrderId());
        OrderCustomer orderCustomer = orderCustomerService.findById(id);
        creditService.checkCredit(localDate, orderCustomer);
    }

}
