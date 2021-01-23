package vip.testops.manager.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.manager.entities.dto.AssertionDTO;

import java.util.List;

@Mapper
public interface AssertionsMapper {
    @Select("select * from t_assertion where caseId=#{caseId}")
    List<AssertionDTO> getAssertionsByCaseId(Long caseId);

    @Insert("insert into t_assertion values(null, #{actual}, #{op}, #{expected}, #{caseId})")
    int addAssertion(AssertionDTO assertionDTO);

    @Delete("delete from t_assertion where caseId=#{caseId}")
    int removeAssertionsByCaseId(Long caseId);
}
