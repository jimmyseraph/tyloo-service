package vip.testops.manager.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.manager.entities.dto.HeaderDTO;

import java.util.List;

@Mapper
public interface HeaderMapper {

    @Select("select * from t_header where caseId=#{caseId}")
    List<HeaderDTO> getHeadersByCaseId(Long caseId);

    @Insert("insert into t_header values(null, #{name}, #{value}, #{caseId})")
    int addHeader(HeaderDTO headerDTO);

    @Delete("delete from t_header where caseId=#{caseId}")
    int removeHeadersByCaseId(Long caseId);

}
