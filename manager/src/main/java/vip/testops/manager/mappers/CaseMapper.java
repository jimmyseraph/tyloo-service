package vip.testops.manager.mappers;

import org.apache.ibatis.annotations.*;
import vip.testops.manager.entities.dto.CaseDTO;

import java.util.List;

@Mapper
public interface CaseMapper {

    @Select("select count(*) from t_case")
    int queryTotal();

    @Select("select * from t_case")
    List<CaseDTO> getList();

    @Insert("insert into t_case values(" +
            "null, " +
            "#{caseName}, " +
            "#{description}, " +
            "#{url}, " +
            "#{method}, " +
            "#{body}, " +
            "now(), " +
            "now())")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "caseId", before = false, resultType = Long.class)
    int addCase(CaseDTO caseDTO);

    @Update("update t_case set " +
            "caseName=#{caseName}, " +
            "description=#{description}, " +
            "url=#{url}, " +
            "method=#{method}, " +
            "body=#{body}," +
            "updateTime=now() " +
            "where caseId=#{caseId}")
    int modifyCase(CaseDTO caseDTO);

    @Delete("delete from t_case where caseId=#{caseId}")
    int removeCase(Long caseId);

    @Select("select * from t_case where caseId=#{caseId}")
    CaseDTO getCaseById(Long caseId);
}
