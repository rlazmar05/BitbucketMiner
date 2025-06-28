package aiss.bitbucket.service;

import aiss.bitbucket.model.Project;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    @Autowired
    CommitService commitService;

    @Autowired
    IssueService issueService;

    public List<Project> getAllProjects(String workspace, String repo_slug){
        List<Project> projects = new ArrayList<>();
        String uri = "https://bitbucket.org/" + workspace + "/" + repo_slug;

        Project project = new Project();
        project.setId(workspace + "/" + repo_slug);
        project.setName(repo_slug);
        project.setWebUrl(uri);
        project.setCommits(commitService.getAllCommits(workspace, repo_slug, 5, 2));
        project.setIssues(issueService.getAllIssues(workspace, repo_slug, 5, 2));

        projects.add(project);
        return projects;
    }

    public Project createProject(Project input){
        Project project = new Project();
        project.setId(input.getId());
        project.setName(input.getName());
        project.setWebUrl(input.getWebUrl());

        if (input.getCommits() != null)
            project.setCommits(new ArrayList<>(input.getCommits()));
        if (input.getIssues() != null)
            project.setIssues(new ArrayList<>(input.getIssues()));

        return project;
    }

    public Project getProjectById(String id, String workspace, String repo_slug){
        List<Project> projects = getAllProjects(workspace, repo_slug);
        return projects.stream()
                .filter(p->p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
