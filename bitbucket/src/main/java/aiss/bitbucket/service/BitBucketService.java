package aiss.bitbucket.service;

import aiss.bitbucket.model.commits.Commit;
import aiss.bitbucket.model.issues.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BitBucketService {

    @Autowired
    RestTemplate restTemplate;

    private static final String BASE_URL = "https://api.bitbucket.org/2.0/repositories/";

    public List<Commit> getAllCommits(String workspace, String repo_slug) {
        String uri = BASE_URL + workspace + "/" + repo_slug + "/commits";
        Commit[] commits = restTemplate.getForObject(uri, Commit[].class);
        if (commits != null) {
            return Arrays.stream(commits).toList();
        }
        return Collections.emptyList();
    }

    public List<Issue> getAllIssues(String workspace, String repo_slug) {
        String uri = BASE_URL + workspace + "/" + repo_slug + "/issues";
        Issue[] issues = restTemplate.getForObject(uri, Issue[].class);
        if (issues != null) {
            return Arrays.stream(issues).toList();
        }
        return Collections.emptyList();
    }
}
