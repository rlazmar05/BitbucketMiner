package aiss.bitbucket.service;

import aiss.bitbucket.model.commits.Commit;
import aiss.bitbucket.model.issues.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GitMinerService {

    @Autowired
    RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/gitminer";

    public void sendDataToGitMiner(List<Commit> commits, List<Issue> issues) {
        for (Commit commit : commits) {
            restTemplate.postForObject(BASE_URL + "/commits", commit, Void.class);
        }
        for (Issue issue : issues) {
            restTemplate.postForObject(BASE_URL + "/issues", issue, Void.class);
        }
    }
}