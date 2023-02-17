package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.request.SubJobUpdateDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.BasicJobDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.SubJobDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.SubJobDtoRes;
import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MapperServices {
    MapperServices INSTANCE = Mappers.getMapper(MapperServices.class);

    List<SubJobDto> subJobListToSubJobDto(List<SubJob> subJobList);

    List<BasicJobDto> ListBasicJobToBasicJobDto(List<BasicJob> list);

    BasicJobDto basicJobToBasicJobDto(BasicJob basicJob);

    BasicJob basicJobDtoToBasicJob(BasicJobDto basicJobDto);

    SubJobDto subJubToSubJobDto(SubJob subJob);

    SubJob subJobDtoToSubJob(SubJobDto subJobDto);

    SubJobDtoRes subJobToSubJobDtoRes(SubJob subJob);

    SubJob subJobUpdateDtoToSubJob(SubJobUpdateDto subJobUpdateDto);

    List<SubJobDtoRes> listSubJobToSubJobDtoRes(List<SubJob> subJobList);

}
