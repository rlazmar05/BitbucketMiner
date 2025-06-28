package aiss.bitbucket.service;

import aiss.bitbucket.model.commits.Commit;
import aiss.bitbucket.model.comments.Comment;
import aiss.bitbucket.model.issues.Issue;
import aiss.bitbucket.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BitbucketService {

    @Autowired
    CommitService commitService;
    @Autowired
    IssueService issueService;
    @Autowired
    CommentService commentService;

    public Project getProjectData(String workspace, String repo_slug, int nCommits, int nIssues, int maxPages) {
        Project project = new Project();

        project.setId(workspace + "/" + repo_slug);
        project.setName(repo_slug);
        project.setWebUrl("https://bitbucket.org/" + workspace + "/" + repo_slug);

        //COMMITS
        List<Commit> commits = commitService.getAllCommits(workspace, repo_slug, nCommits, maxPages);
        if (commits != null) {
            project.setCommits(commits);
        } else {
            project.setCommits(Collections.emptyList());
        }

        //ISSUES
        List<Issue> issues = issueService.getAllIssues(workspace, repo_slug, nIssues, maxPages);
        List<Issue> processedIssues = new ArrayList<>();

        if (issues != null) {
            for (Issue issue : issues) {
                // Añadir comentarios (si no se añadieron ya en el issueService)
                List<Comment> comments = commentService.getAllCommentsFromIssue(workspace, repo_slug, issue.getId());
                issue.setComments(comments != null ? comments : Collections.emptyList());

                processedIssues.add(issue);
            }
            project.setIssues(processedIssues);
        } else {
            project.setIssues(Collections.emptyList());
        }

        return project;
    }
}
