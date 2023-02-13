package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.respons.CustomerDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.PersonDto;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface MapperUsers {
    MapperUsers INSTANCE = Mappers.getMapper(MapperUsers.class);
    ExpertDto expertToExpertDto(Expert expert);

    Expert expertDtoToExpert(ExpertDto expertDto);
    CustomerDto customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDto customerDto);
    List<ExpertDto> listExpertToExpertDto(List<Expert> listExpert);
    List<PersonDto> listPersonToPersonDto(List<Person> personList);
}
