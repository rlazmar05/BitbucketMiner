package aiss.bitbucket.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aiss.bitbucket.model.Project;
import aiss.bitbucket.service.BitbucketService;
import aiss.bitbucket.service.RequiredArgsConstructor;

@RestController
@RequestMapping("/bitbucket")
@RequiredArgsConstructor
public class BitbucketController {

    private final BitbucketService bitbucketService = new BitbucketService();

    @PostMapping("/{workspace}/{repo_slug}")
    public ResponseEntity<String> importToGitMiner(
            @PathVariable String workspace,
            @PathVariable String repo_slug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages) {

        Project project = bitbucketService.fetchData(workspace, repo_slug, nCommits, nIssues, maxPages);
        bitbucketService.sendToGitMiner(project);
        return ResponseEntity.ok("Project imported successfully.");
    }

    @GetMapping("/{workspace}/{repo_slug}")
    public ResponseEntity<Project> previewData(
            @PathVariable String workspace,
            @PathVariable String repo_slug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages) {

        Project project = bitbucketService.fetchData(workspace, repo_slug, nCommits, nIssues, maxPages);
        return ResponseEntity.ok(project);
    }
}
