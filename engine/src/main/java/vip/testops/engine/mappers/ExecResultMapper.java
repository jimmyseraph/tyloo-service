package vip.testops.engine.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import vip.testops.engine.entities.vto.ExecResultVTO;

@Mapper
public interface ExecResultMapper {
    @Insert("insert into t_exec_result values(" +
            "null, " +
            "#{projectId}, " +
            "#{caseId}, " +
            "#{caseName}, " +
            "#{requestHeaders}, " +
            "#{url}, " +
            "#{method}, " +
            "#{requestBody}, " +
            "#{responseHeaders}, " +
            "#{responseBody}, " +
            "#{status}, " +
            "#{message}, " +
            "#{duration}, " +
            "#{startTime})")
    int addExecResult(ExecResultVTO execResultVTO);
}
