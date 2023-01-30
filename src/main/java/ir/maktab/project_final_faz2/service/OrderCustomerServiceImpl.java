package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.TimeOutException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.interfaces.OrderCustomerService;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCustomerServiceImpl implements OrderCustomerService {
    private final OrderCustomerRepository orderCustomerRepository;

    @Override
    public OrderCustomer saveOrder(OrderCustomer orderCustomer) {
        if (orderCustomerRepository.findByCodeOrder(orderCustomer.getCodeOrder()).isPresent())
            throw new RepeatException(String.format("the order is exist already to code: %s " + orderCustomer.getCodeOrder()));
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (orderCustomer.getOfferPrice().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException(String.format("The offer price by Customer for this sub-service %s is lower than the original price", orderCustomer.getSubJob().getSubJobName()));
        if (UtilDate.compareTwoDate(orderCustomer.getStartDateDoWork(), today) < 0)
            throw new TimeOutException("The current date is less than the proposed date");
        orderCustomer.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        return orderCustomerRepository.save(orderCustomer);
    }

    @Override
    public OrderCustomer findByCode(String codeOrder) {
        return orderCustomerRepository.findByCodeOrder(codeOrder).orElseThrow(() -> new NotFoundException(String.format("there arent any orderCustomer to code " + codeOrder)));
    }

    @Override
    public OrderCustomer findById(Long id) {
        return orderCustomerRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Not Found Order %d " + id)));
    }

    @Override
    public List<OrderCustomer> findAllOrdersBySubJob(SubJob subJob) {
        List<OrderCustomer> allOrderBySubJobForAExpert = orderCustomerRepository.findAllBySubJobForAExpert(subJob);
        if (allOrderBySubJobForAExpert.isEmpty())
            throw new NotFoundException("No Order for This SubJob");
        return allOrderBySubJobForAExpert;
    }

    @Override
    public void updateOrder(OrderCustomer orderCustomer) {
        orderCustomerRepository.save(orderCustomer);
    }
}
