package vip.testops.manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.manager.common.Response;
import vip.testops.manager.entities.requests.ProjectAddModifyRequest;
import vip.testops.manager.entities.vto.ProjectVTO;
import vip.testops.manager.services.ProjectService;
import vip.testops.manager.utils.StringUtil;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/list")
    @ResponseBody
    public Response<List<ProjectVTO>> getList(){
        Response<List<ProjectVTO>> response = new Response<>();
        projectService.doGetList(response);
        return response;
    }

    @PostMapping("/add")
    @ResponseBody
    public Response<?> addProject(@RequestBody ProjectAddModifyRequest request){
        Response<?> response = new Response<>();
        // 参数检查
        if(StringUtil.isEmptyOrNull(request.getProjectName())){
            response.paramMissError("projectName");
            return response;
        }
        projectService.doAddProject(request.getProjectName(), request.getDescription(), response);
        return response;
    }

    @PostMapping("/modify")
    @ResponseBody
    public Response<?> modifyProject(@RequestBody ProjectAddModifyRequest request){
        Response<?> response = new Response<>();
        // 参数检查
        if(request.getProjectId() == null){
            response.paramMissError("projectId");
            return response;
        }
        if(StringUtil.isEmptyOrNull(request.getProjectName())){
            response.paramMissError("projectName");
            return response;
        }
        projectService.doModifyProject(
                request.getProjectId(),
                request.getProjectName(),
                request.getDescription(),
                response);
        return response;
    }

    @DeleteMapping("/{id}/remove")
    @ResponseBody
    public Response<?> removeProject(@PathVariable("id") Long projectId){
        Response<?> response = new Response<>();
        projectService.doRemoveProject(projectId, response);
        return response;
    }

    @GetMapping("/{id}/detail")
    @ResponseBody
    public Response<ProjectVTO> getDetail(@PathVariable("id") Long projectId){
        Response<ProjectVTO> response = new Response<>();
        projectService.doGetDetail(projectId, response);
        return response;
    }

    @GetMapping("/{id}/execute")
    @ResponseBody
    public Response<?> projectExecute(@PathVariable("id") Long projectId){
        Response<?> response = new Response<>();

        return response;
    }
}
