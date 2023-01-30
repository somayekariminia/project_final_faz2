package ir.maktab.project_final_faz2.service.interfaces;

import ir.maktab.project_final_faz2.data.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface OfferService {
    List<OrderCustomer> findAllOrdersForAnSubJobOfExpert(Expert expert, SubJob subJob);

    Offers save(Offers offers, String codeOrder);

    List<Offers> viewAllOffersOrderByPriceAsc(String codeOrder);

    Offers selectAnOfferByCustomer(Offers offers, OrderCustomer orderCustomer);

    void changeOrderToStartByCustomer(Offers offers, OrderCustomer orderCustomer);

    void endDoWork(OrderCustomer orderCustomer, LocalDateTime endDoWork);

    Offers findById(Long id);

    Offers findOffersIsAccept(OrderCustomer orderCustomer);

    List<Offers> viewAllOrdersOrderByScoreExpertAsc(String orderCode);

    List<SubJob> findAllSubJubExpert(Expert expert);
}
