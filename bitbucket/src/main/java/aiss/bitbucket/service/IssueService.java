package aiss.bitbucket.service;

import aiss.bitbucket.model.issues.Issue;
import aiss.bitbucket.model.issues.Content;
import aiss.bitbucket.model.issues.ListIssues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    public List<Issue> getAllIssues(String workspace, String repo_slug, int nIssues, int maxPages) {
        List<Issue> issues = new ArrayList<>();

        String uri = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repo_slug + "/issues";

        ListIssues response = restTemplate.getForObject(uri, ListIssues.class);

        if (response != null && response.getValues() != null) {
            for (aiss.bitbucket.model.issues.Issue bi : response.getValues()) {
                Issue issue = new Issue();
                issue.setId(String.valueOf(bi.getId()));
                issue.setTitle(bi.getTitle());
                issue.setState(bi.getState());
                issue.setCreatedAt(bi.getCreatedAt());
                issue.setUpdatedAt(bi.getUpdatedAt());
                issue.setClosedAt(bi.getClosedAt());
                issue.setVotes(bi.getVotes());

                Content content = bi.getContent();
                issue.setDescription(content != null ? content.getRaw() : null);

                issue.setLabels(bi.getKind() != null ? List.of(bi.getKind()) : Collections.emptyList());

                issues.add(issue);

                if (issues.size() >= nIssues) {
                    break;
                }
            }
        }

        return issues;
    }

    public Issue getIssueById(String id, String workspace, String repo_slug, int nIssues, int maxPages) {
        List<Issue> allIssues = getAllIssues(workspace, repo_slug, nIssues, maxPages);
        if (allIssues != null) {
            Optional<Issue> found = allIssues.stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst();
            return found.orElse(null);
        }
        return null;
    }

    public List<Issue> getIssuesByState(String state, String workspace, String repo_slug, int nIssues, int maxPages) {
        List<Issue> allIssues = getAllIssues(workspace, repo_slug, nIssues, maxPages);
        if (allIssues == null || allIssues.isEmpty()) {
            return Collections.emptyList();
        }

        return allIssues.stream()
                .filter(issue -> issue.getState() != null &&
                        issue.getState().equalsIgnoreCase(state))
                .toList();
    }
}
