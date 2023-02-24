package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.dto.request.OrderRegistryDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.OrderCustomerResponseDto;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import java.util.List;

public interface OrderCustomerService {
    OrderCustomer saveOrder(OrderRegistryDto orderRegistryDto);

    OrderCustomer findById(Long id);

    List<OrderCustomer> findAllOrdersBySubJob(SubJob subJob);

    OrderCustomer updateOrder(OrderCustomer orderCustomer);

    List<OrderCustomer> findOrdersCustomer(Customer customer);

    List<OrderCustomer> viewAllOrder(Expert expert);



}
