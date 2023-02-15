package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.exception.*;
import ir.maktab.project_final_faz2.service.serviceInterface.OrderCustomerService;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCustomerServiceImpl implements OrderCustomerService {
    private final OrderCustomerRepository orderCustomerRepository;
    private final CustomerServiceImpl customerService;
    private final SubJobServiceImpl subJobService;

    @Override
    public OrderCustomer saveOrder(OrderCustomer orderCustomer) {
        if (Objects.isNull(orderCustomer))
            throw new NullObjects("orderCustomer is null");
        if (orderCustomerRepository.findById(orderCustomer.getId()).isPresent())
            throw new DuplicateException(String.format("the order is exist already to code: %s", orderCustomer.getId()));
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (orderCustomer.getOfferPrice().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException(String.format("The offer price by Customer for this sub-service %s is lower than the original price", orderCustomer.getSubJob().getSubJobName()));
        if (UtilDate.compareTwoDate(orderCustomer.getStartDateDoWork(), today) < 0)
            throw new TimeOutException("The current date is less than the proposed date");
        orderCustomer.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        return orderCustomerRepository.save(orderCustomer);
    }

    @Override
    public OrderCustomer findById(Long id) {
        return orderCustomerRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Not Found Order %d ", id)));
    }

    @Override
    public List<OrderCustomer> findAllOrdersBySubJob(SubJob subJob) {
        SubJob subJobDb = subJobService.findSubJobByName(subJob.getSubJobName());
        List<OrderCustomer> allOrderBySubJobForAExpert = orderCustomerRepository.findAllBySubJobForAExpert(subJobDb);
        if (allOrderBySubJobForAExpert.isEmpty())
            throw new NotFoundException(String.format("!!!No Order for This SubJob %s", subJobDb.getSubJobName()));
        return allOrderBySubJobForAExpert;
    }

    @Override
    public List<OrderCustomer> findOrdersCustomer(Customer customer) {
        Customer customerDb = customerService.findByUserName(customer.getEmail());
        List<OrderCustomer> allOrdersCustomer = orderCustomerRepository.findAllByCustomer(customerDb);
        if (allOrdersCustomer.isEmpty())
            throw new NotFoundException(String.format("there aren't order for this Customer %s", customerDb.getEmail()));
        return allOrdersCustomer;

    }

    @Override
    public OrderCustomer updateOrder(OrderCustomer orderCustomer) {
        if (Objects.isNull(orderCustomer))
            throw new NotFoundException("not Fount orderCustomer");
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
