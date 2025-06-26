package aiss.bitbucket.service;

import aiss.bitbucket.model.commits.*;
import aiss.bitbucket.model.commits.Commit;
import aiss.bitbucket.model.commits.ListCommits;
import aiss.bitbucket.model.commits.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    public List<Commit> getAllCommits(String workspace, String repo_slug) {
        List<Commit> commits = new ArrayList<>();
        String uri = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repo_slug + "/commits";
        ListCommits response = restTemplate.getForObject(uri, ListCommits.class);

        if(response != null && response.getValues() != null) {
            for (Value bitbucketCommit : response.getValues()) {

                Commit commit = new Commit();
                commit.setId(bitbucketCommit.getHash());

                String message = bitbucketCommit.getMessage();
                String title = message != null && message.contains("\n") ? message.split("\n")[0] : message;
                commit.setTitle(title);
                commit.setMessage(message);

                Author author = bitbucketCommit.getAuthor();
                if (author != null) {
                    commit.setAuthorName(author.getUser() != null ? author.getUser().getDisplayName() : author.getRaw());
                    commit.setAuthorEmail(null);
                    commit.setAuthoredDate(bitbucketCommit.getDate());
                }

                String webUrl = "https://bitbucket.org/" + workspace + "/" + repo_slug + "/commits/" + bitbucketCommit.getHash();
                commit.setWebUrl(webUrl);

                List<Parent> parents = bitbucketCommit.getParents();
                if (parents != null && !parents.isEmpty()) {
                    commit.setParentIds(parents.stream().map(Parent::getHash).toList());
                }

                commits.add(commit);
            }
        }

        return commits;

    }


}
