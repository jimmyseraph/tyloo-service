package vip.testops.manager.mappers;

import org.apache.ibatis.annotations.*;
import vip.testops.manager.entities.dto.DetailDTO;
import vip.testops.manager.entities.dto.SuiteDTO;
import vip.testops.manager.entities.vto.SuiteVTO;

import java.util.List;

@Mapper
public interface SuiteMapper {

    @Select("select count(*) from t_suite where status=#{status}")
    int countByStatus(Integer status);

    @Select("select count(*) from t_suite")
    int total();

    @Select("select * from t_suite where projectId=#{projectId}")
    List<SuiteDTO> getSuiteByProjectId(Long projectId);

    @Select("select caseId, caseName, description, status from t_suite inner join t_case using(caseId) where projectId=#{projectId}")
    List<DetailDTO> getSuiteVTOByProjectId(Long projectId);

    @Delete("delete from t_suite where projectId=#{projectId}")
    int removeSuiteByProjectId(Long projectId);

    @Insert("insert into t_suite values(" +
            "null, " +
            "#{projectId}, " +
            "#{caseId}, " +
            "#{status}, " +
            "#{duration}, " +
            "null)")
    int addSuite(SuiteDTO suiteDTO);

    @Update("update t_suite set status=#{status} where projectId=#{projectId}")
    int updateStatusByProjectId(Long projectId, Integer status);

    @Update("update t_suite set duration=#{duration}, status=#{status}, lastRun=NOW() where projectId=#{projectId} and caseId=#{caseId}")
    int updateStatusByProjectIdAndCaseId(Long projectId, Long caseId, Long duration, Integer status);
}
