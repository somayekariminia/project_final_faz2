package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.InfoCard;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.service.serviceImpl.CreditServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.OrderCustomerServiceImpl;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/pay")
@Slf4j
public class CreditCardController {

    private final CreditServiceImpl creditService;

    private final OrderCustomerServiceImpl orderCustomerService;


    @PostMapping("/paymentOfCard")
    public void paymentOfCard(@Valid @ModelAttribute("infoCard") InfoCard infoCard, HttpServletRequest request) {
        if (!infoCard.getCaptcha().equalsIgnoreCase((String) request.getSession().getAttribute("captcha")))
            throw new ValidationException("captcha not is  valid");
        log.info("start method payment");
        LocalDate localDate = UtilDate.getDate(infoCard.getDateExpired());
        Long id = Long.parseUnsignedLong(infoCard.getOrderId());
        OrderCustomer orderCustomer = orderCustomerService.findById(id);
        creditService.checkCredit(localDate, orderCustomer);
        log.info("end methode payment");
    }

}
