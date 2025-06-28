package aiss.bitbucket.controllers;

import aiss.bitbucket.model.commits.Commit;
import aiss.bitbucket.model.issues.Issue;
import aiss.bitbucket.service.*;
import aiss.bitbucket.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bitbucket")
public class BitbucketMinerController {

    @Autowired
    private BitbucketService bitbucketService;

    @Autowired
    private CommitService commitService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private GitMinerService gitMinerService;

    @Autowired
    private CommitService commitService;

    @Autowired
    private IssueService issueService;

    @PostMapping("/{workspace}/{repo_slug}")
    public String fetchAndSendData(
            @PathVariable String workspace,
            @PathVariable String repo_slug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages) {

        List<Commit> commits = commitService.getAllCommits(workspace, repo_slug, nCommits, maxPages);
        List<Issue> issues = issueService.getAllIssues(workspace, repo_slug, nIssues, maxPages);

        gitMinerService.sendDataToGitMiner(commits, issues);

        return "Datos enviados correctamente a GitMiner desde Bitbucket";
    }

    @GetMapping("/{workspace}/{repo_slug}")
    public Map<String, Object> fetchPreview(
            @PathVariable String workspace,
            @PathVariable String repo_slug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages) {

        Map<String, Object> preview = new HashMap<>();
        preview.put("commits", commitService.getAllCommits(workspace, repo_slug, nCommits, maxPages));
        preview.put("issues", issueService.getAllIssues(workspace, repo_slug, nIssues, maxPages));
        return preview;
    }

}