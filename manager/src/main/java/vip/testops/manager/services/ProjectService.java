package vip.testops.manager.services;

import vip.testops.manager.common.Response;
import vip.testops.manager.entities.vto.ProjectVTO;

import java.util.List;

public interface ProjectService {
    void doGetList(Response<List<ProjectVTO>> response);

    void doAddProject(String projectName, String description, Response<?> response);

    void doModifyProject(Long projectId, String projectName, String description, Response<?> response);

    void doRemoveProject(Long projectId, Response<?> response);

    void doGetDetail(Long projectId, Response<ProjectVTO> response);
}
