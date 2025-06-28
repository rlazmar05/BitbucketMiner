package aiss.bitbucket.service;

import aiss.bitbucket.model.comments.Comment;
import aiss.bitbucket.model.User;
import aiss.bitbucket.model.issues.IssueComments;
import aiss.bitbucket.model.issues.ListIssueComments;
import aiss.bitbucket.model.issues.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    public List<Comment> getAllCommentsFromIssue(String workspace, String repo_slug, String issueId) {
        List<Comment> comments = new ArrayList<>();
        String uri = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repo_slug + "/issues/" + issueId + "/comments";

        ListIssueComments response = restTemplate.getForObject(uri, ListIssueComments.class);

        if (response != null && response.getValues() != null) {
            for (IssueComments bc : response.getValues()) {
                Comment comment = new Comment();

                comment.setId(bc.getId() != null ? bc.getId().toString() : null);

                Content content = bc.getContent();
                comment.setBody(content != null ? content.getRaw() : null);

                comment.setCreatedAt(bc.getCreatedOn());
                comment.setUpdatedAt(bc.getUpdatedOn());

                if (bc.getUser() != null) {
                    User user = getUser(bc);

                    comment.setAuthor(user);
                }

                comments.add(comment);
            }
            return comments;
        }

        return Collections.emptyList();
    }

    private static User getUser(IssueComments bc) {
        User bitbucketUser = bc.getUser();

        User user = new User();
        user.setId(bitbucketUser.getId() != null ? bitbucketUser.getId() : bitbucketUser.getUsername());
        user.setUsername(bitbucketUser.getUsername());
        user.setDisplayName(bitbucketUser.getDisplayName());
        user.setName(bitbucketUser.getName());
        user.setAvatarUrl(bitbucketUser.getAvatarUrl());
        user.setWebUrl(bitbucketUser.getWebUrl());
        return user;
    }

    public Comment getCommentFromIssueById(String workspace, String repoSlug, String issueId, String commentId) {
        List<Comment> all = getAllCommentsFromIssue(workspace, repoSlug, issueId);
        if (all == null) return null;

        Optional<Comment> found = all.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst();
        return found.orElse(null);
    }
}
