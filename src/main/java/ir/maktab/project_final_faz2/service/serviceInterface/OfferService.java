package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import java.util.List;

public interface OfferService {
    List<OrderCustomer> findAllOrdersForAnSubJobOfExpert(Expert expert, SubJob subJob);

    Offers save(Offers offers, Long id);

    List<Offers> viewAllOffersOrderByPriceAsc(Long id);

    Offers selectAnOfferByCustomer(Long offersId, Long orderCustomerId);

    OrderCustomer changeOrderToStartByCustomer(Long offersId, Long orderCustomerId);

    void endDoWork(Long orderCustomer);

    Offers findById(Long id);

    Offers findOffersIsAccept(OrderCustomer orderCustomer);

    List<Offers> viewAllOrdersOrderByScoreExpertAsc(Long id);

    List<SubJob> findAllSubJubExpert(Expert expert);

    List<Offers> viewAllOrdersOrderByScoreExpertDesc(Long id);

    List<Offers> viewAllOffersOrderByPriceDesc(Long id);

    void subtractOfScore(OrderCustomer orderCustomer);

    void updateOffer(Offers offers);
}
