package aiss.bitbucket.model.issues;

import aiss.bitbucket.model.User;
import aiss.bitbucket.model.comments.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @Transient // Este campo lo usamos solo al parsear JSON
    @JsonProperty("content")
    private Content content;

    @JsonProperty("state")
    private String state;

    @JsonProperty("created_on")
    private String createdAt;

    @JsonProperty("updated_on")
    private String updatedAt;

    @JsonProperty("closed_on")
    private String closedAt;

    @JsonProperty("kind")
    private String kind;

    @Transient
    private String description;

    @ElementCollection
    private List<String> labels;

    @JsonProperty("author")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @JsonProperty("assignee")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    private User assignee;

    @JsonProperty("votes")
    private Integer votes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "issueId")
    private List<Comment> comments;

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDescription() {
        return content != null ? content.getRaw() : null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", state='" + state + '\'' +
                ", createdOn='" + createdAt + '\'' +
                ", updatedOn='" + updatedAt + '\'' +
                ", closedOn='" + closedAt + '\'' +
                ", kind='" + kind + '\'' +
                ", description='" + getDescription() + '\'' +
                ", labels=" + labels +
                ", author=" + author +
                ", assignee=" + assignee +
                ", votes=" + votes +
                ", comments=" + comments +
                '}';
    }
}
