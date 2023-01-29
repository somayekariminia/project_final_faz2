package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.ValidationException;
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
public class OrderCustomerServiceImpl {
    private final OrderCustomerRepository orderCustomerRepository;

    public OrderCustomer saveOrder(OrderCustomer orderCustomer) {
        if (orderCustomerRepository.findByCodeOrder(orderCustomer.getCodeOrder()).isPresent())
            throw new RepeatException("the order is exist already to code " + orderCustomer.getCodeOrder());
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (orderCustomer.getOfferPrice().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException("priceOffer lower of basic price");
        if (UtilDate.compareTwoDate(orderCustomer.getOfferStartDateCustomer(), today) < 0)
            throw new ValidationException("You can't order before today ");
        orderCustomer.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        return orderCustomerRepository.save(orderCustomer);
    }
    public OrderCustomer findByCode(String codeOrder){
        return orderCustomerRepository.findByCodeOrder(codeOrder).orElseThrow(()->new NotFoundException("there arent any orderCustomer to code "+codeOrder));
    }

    public OrderCustomer findById(Long id) {
       return orderCustomerRepository.findById(id).orElseThrow(()->new NotFoundException("not found Order "+ id));
    }
    public List<OrderCustomer> findAllOrdersBySubJob(SubJob subJob){
        return orderCustomerRepository.findAllBySubJobForAExpert(subJob);
    }
    public void updateOrder(OrderCustomer orderCustomer){
        orderCustomerRepository.save(orderCustomer);
    }
}