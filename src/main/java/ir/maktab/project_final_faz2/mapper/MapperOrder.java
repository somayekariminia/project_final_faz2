package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.request.OrderCustomerDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.OrderCustomerResponseDto;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MapperOrder {
    MapperOrder INSTANCE = Mappers.getMapper(MapperOrder.class);

    @Mapping(source = "startDateDoWork", target = "startDateDoWork", dateFormat = "yyyy-MM-dd HH:mm:ss")
    OrderCustomerDto orderCustomerToOrderCustomerDto(OrderCustomer orderCustomer);

    @Mapping(source = "startDateDoWork", target = "startDateDoWork", dateFormat = "yyyy-MM-dd HH:mm:ss")
    OrderCustomer orderCustomerDtoToOrderCustomer(OrderCustomerDto orderCustomerDto);

    List<OrderCustomerDto> listOrderCustomerTOrderCustomerDto(List<OrderCustomer> list);

    OrderCustomerResponseDto orderCustomerToOrderCustomerResponseDto(OrderCustomer orderCustomer);

    List<OrderCustomerResponseDto> listOrderCustomerToOrderCustomerResponseDto(List<OrderCustomer> list);

}
