package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.dto.request.OfferRegistryDto;
import ir.maktab.project_final_faz2.data.model.entity.*;

import java.util.List;

public interface OfferService {
    List<OrderCustomer> findAllOrdersForAnSubJobOfExpert(Expert expert, SubJob subJob);

    Offers save(OfferRegistryDto offerRegistryDto);

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

    List<OrderCustomer> findAllOrderDoneExpert(Expert expert);

    List<Offers> findAllOffersIsAcceptedAExpert(Expert expert);

    List<Offers> findAllOffersIsAcceptedACustomer(Customer customer);
}
