package aiss.bitbucket.service;

import aiss.bitbucket.model.commits.Author;
import aiss.bitbucket.model.commits.Commit;
import aiss.bitbucket.model.commits.ListCommits;
import aiss.bitbucket.model.commits.Parent;
import aiss.bitbucket.model.commits.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    public List<Commit> getAllCommits(String workspace, String repo_slug, int nCommits, int maxPages) {
        List<Commit> commits = new ArrayList<>();
        String uri = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repo_slug + "/commits";

        ListCommits response = restTemplate.getForObject(uri, ListCommits.class);

        if (response != null && response.getValues() != null) {
            for (Value bitbucketCommit : response.getValues()) {
                Commit commit = new Commit();
                commit.setId(bitbucketCommit.getHash());

                String message = bitbucketCommit.getMessage();
                String title = (message != null && message.contains("\n")) ? message.split("\n")[0] : message;
                commit.setTitle(title);
                commit.setMessage(message);

                Author author = bitbucketCommit.getAuthor();
                if (author != null) {
                    if (author.getUser() != null) {
                        commit.setAuthorName(author.getUser().getDisplayName());
                    } else {
                        commit.setAuthorName(author.getRaw());
                    }

                    commit.setAuthorEmail(null); // Bitbucket no ofrece email por defecto
                    commit.setAuthoredDate(bitbucketCommit.getDate());
                }

                // Web URL
                String webUrl = "https://bitbucket.org/" + workspace + "/" + repo_slug + "/commits/" + bitbucketCommit.getHash();
                commit.setWebUrl(webUrl);

                // Parents
                List<Parent> parents = bitbucketCommit.getParents();
                if (parents != null && !parents.isEmpty()) {
                    commit.setParentIds(parents.stream().map(Parent::getHash).toList());
                }

                commits.add(commit);

                if (commits.size() >= nCommits) {
                    break;
                }
            }
        }

        return commits.isEmpty() ? Collections.emptyList() : commits;
    }

}
