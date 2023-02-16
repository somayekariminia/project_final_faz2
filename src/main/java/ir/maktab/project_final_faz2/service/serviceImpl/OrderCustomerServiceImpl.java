package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.exception.*;
import ir.maktab.project_final_faz2.service.serviceInterface.OrderCustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCustomerServiceImpl implements OrderCustomerService {
    private final OrderCustomerRepository orderCustomerRepository;
    private final CustomerServiceImpl customerService;
    private final SubJobServiceImpl subJobService;
    @Autowired
    MessageSourceConfiguration messageSource;
    @Override
    public OrderCustomer saveOrder(OrderCustomer orderCustomer) {
        if (Objects.isNull(orderCustomer))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        if (orderCustomer.getOfferPrice().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException(messageSource.getMessage("errors.message.low_price"));
        if (orderCustomer.getStartDateDoWork().isBefore(LocalDateTime.now()))
            throw new TimeOutException(messageSource.getMessage("errors.message.isBefore_date_now"));
        orderCustomer.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        return orderCustomerRepository.save(orderCustomer);
    }

    @Override
    public OrderCustomer findById(Long id) {
        return orderCustomerRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.null-object")));
    }

    @Override
    public List<OrderCustomer> findAllOrdersBySubJob(SubJob subJob) {
        SubJob subJobDb = subJobService.findSubJobByName(subJob.getSubJobName());
        List<OrderCustomer> allOrderBySubJobForAExpert = orderCustomerRepository.findAllBySubJobForAExpert(subJobDb);
        if (allOrderBySubJobForAExpert.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return allOrderBySubJobForAExpert;
    }

    @Override
    public List<OrderCustomer> findOrdersCustomer(Customer customer) {
        Customer customerDb = customerService.findByUserName(customer.getEmail());
        List<OrderCustomer> allOrdersCustomer = orderCustomerRepository.findAllByCustomer(customerDb);
        if (allOrdersCustomer.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return allOrdersCustomer;

    }

    @Override
    public OrderCustomer updateOrder(OrderCustomer orderCustomer) {
        if (Objects.isNull(orderCustomer))
            throw new NotFoundException(messageSource.getMessage("errors.message.null-object"));
        return orderCustomerRepository.save(orderCustomer);
    }

    @Override
    public List<OrderCustomer> viewAllOrder(Expert expert) {
        List<OrderCustomer> customerList = new ArrayList<>();
        for (SubJob subJob : expert.getServicesList()) {
            customerList.addAll(findAllOrdersBySubJob(subJob));
        }
        return customerList;
    }
}
