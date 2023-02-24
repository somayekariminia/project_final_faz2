package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.InfoCardDto;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.service.serviceImpl.CreditServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.OrderCustomerServiceImpl;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/pay")
@Slf4j
@Validated
public class CreditCardController {

    private final CreditServiceImpl creditService;

    private final OrderCustomerServiceImpl orderCustomerService;


    @PostMapping("/paymentOfCard")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public void paymentOfCard(@Valid @ModelAttribute("infoCardDto") InfoCardDto infoCardDto, HttpServletRequest request) {
        if (!infoCardDto.getCaptcha().equalsIgnoreCase((String) request.getSession().getAttribute("captcha")))
            throw new ValidationException("captcha not is  valid");
        log.info("start method payment");
        LocalDate localDate = UtilDate.getDate(infoCardDto.getDateExpired());
        Long id = Long.parseUnsignedLong(infoCardDto.getOrderId());
        OrderCustomer orderCustomer = orderCustomerService.findById(id);
        creditService.checkCredit(localDate, orderCustomer);
        log.info("end methode payment");
    }

}
