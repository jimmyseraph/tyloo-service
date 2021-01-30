package vip.testops.manager.mappers;

import org.apache.ibatis.annotations.*;
import vip.testops.manager.entities.dto.ProjectDTO;

import java.util.List;

@Mapper
public interface ProjectMapper {
    @Select("select * from t_project")
    List<ProjectDTO> getList();

    @Insert("insert into t_project values(" +
            "null, #{projectName}, #{description}, #{status}, now(), now())")
    int addProject(ProjectDTO projectDTO);

    @Update("update t_project set " +
            "projectName=#{projectName}, " +
            "description=#{description}, " +
            "updateTime=now() " +
            "where projectId=#{projectId}")
    int modifyProject(ProjectDTO projectDTO);

    @Delete("delete from t_project where projectId=#{projectId}")
    int removeProject(Long projectId);

    @Select("select * from t_project where projectId=#{projectId}")
    ProjectDTO getProjectById(Long projectId);

    @Update("update t_project set status=#{status} where projectId=#{projectId}")
    int updateProjectStatusById(Long projectId, Integer status);
}
