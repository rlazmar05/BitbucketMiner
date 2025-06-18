package aiss.bitbucket.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import aiss.bitbucket.model.Project;
import aiss.bitbucket.model.commits.Author;
import aiss.bitbucket.model.commits.Commit;
import aiss.bitbucket.model.issues.Issue;

@Service
@RequiredArgsConstructor
public class BitBucketService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Project fetchData(String workspace, String repoSlug, int nCommits, int nIssues, int maxPages) {
        String repoUrl = "https://bitbucket.org/" + workspace + "/" + repoSlug;
        List<Commit> commits = fetchCommits(workspace, repoSlug, nCommits, maxPages);
        List<Issue> issues = fetchIssues(workspace, repoSlug, nIssues, maxPages);

        Project project = new Project();
        project.setId(UUID.nameUUIDFromBytes(repoUrl.getBytes()).toString());
        project.setName(repoSlug);
        project.setWebUrl(repoUrl);
        project.setCommits(commits);
        project.setIssues(issues);
        return project;
    }

    private List<Commit> fetchCommits(String workspace, String repoSlug, int limit, int maxPages) {
        List<Commit> result = new ArrayList<>();
        String baseUrl = String.format("https://api.bitbucket.org/2.0/repositories/%s/%s/commits", workspace, repoSlug);
        String nextUrl = baseUrl;

        for (int page = 0; page < maxPages && nextUrl != null; page++) {
            ResponseEntity<JsonNode> response = restTemplate.exchange(nextUrl, HttpMethod.GET, null, JsonNode.class);
            JsonNode values = response.getBody().get("values");

            for (JsonNode node : values) {
                Commit commit = new Commit();
                commit.setId(node.get("hash").asText());
                commit.setTitle(node.get("message").asText().split("\n")[0]);
                commit.setMessage(node.get("message").asText());
                commit.setAuthoredDate(node.get("date").asText());
                commit.setAuthorName(node.get("author").get("user").get("display_name").asText());
                commit.setAuthorEmail("bitbucket@example.com"); // placeholder
                commit.setWebUrl(node.get("links").get("html").get("href").asText());
                result.add(commit);
                if (result.size() >= limit) return result;
            }

            JsonNode next = response.getBody().get("next");
            nextUrl = (next != null) ? next.asText() : null;
        }

        return result;
    }

    private List<Issue> fetchIssues(String workspace, String repoSlug, int limit, int maxPages) {
        List<Issue> result = new ArrayList<>();
        String baseUrl = String.format("https://api.bitbucket.org/2.0/repositories/%s/%s/issues", workspace, repoSlug);
        String nextUrl = baseUrl;

        for (int page = 0; page < maxPages && nextUrl != null; page++) {
            ResponseEntity<JsonNode> response = restTemplate.exchange(nextUrl, HttpMethod.GET, null, JsonNode.class);
            JsonNode values = response.getBody().get("values");

            for (JsonNode node : values) {
                Issue issue = new Issue();
                issue.setId(node.get("id").asText());
                issue.setTitle(node.get("title").asText());
                issue.setDescription(node.get("content").get("raw").asText());
                issue.setState(node.get("state").asText().toLowerCase());
                issue.setCreatedAt(node.get("created_on").asText());
                issue.setUpdatedAt(node.get("updated_on").asText());
                issue.setAuthor(new Author(
                        node.get("reporter").get("uuid").asText(),
                        node.get("reporter").get("username").asText(),
                        node.get("reporter").get("display_name").asText(),
                        node.get("reporter").get("links").get("html").get("href").asText()
                ));
                issue.setComments(Collections.emptyList()); // Bitbucket comments opcional
                result.add(issue);
                if (result.size() >= limit) return result;
            }

            JsonNode next = response.getBody().get("next");
            nextUrl = (next != null) ? next.asText() : null;
        }

        return result;
    }

    public void sendToGitMiner(Project project) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Project> request = new HttpEntity<>(project, headers);
        restTemplate.postForEntity("http://localhost:8080/gitminer/projects", request, Void.class);
    }
}
