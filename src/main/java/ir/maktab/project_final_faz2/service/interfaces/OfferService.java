package ir.maktab.project_final_faz2.service.interfaces;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import java.time.LocalDateTime;
import java.util.List;

public interface OfferService {
    List<OrderCustomer> findAllOrdersForAExpert(Expert expert);

    List<OrderCustomer> findAllOrdersForAnSubJobOfExpert(Expert expert, SubJob subJob);

    Offers save(Offers offers, String codeOrder);

    List<Offers> viewAllOffersOrdersByCustomerOrderByPrice(String orderCode);

    Offers selectAnOfferByCustomer(Offers offers, OrderCustomer orderCustomer);

    void changeOrderToStartByCustomer(Offers offers, OrderCustomer orderCustomer);

    void endDoWork(OrderCustomer orderCustomer, LocalDateTime endDoWork);

    Offers findById(Long id);

    Offers findOffersIsAccept(OrderCustomer orderCustomer);

    List<Offers> viewAllOffersOrdersByCustomerOrderByPerformanceExpert(String orderCode);
}
