package aiss.bitbucket.model.commits;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "hash",
        "message",
        "date",
        "author",
        "parents"
})
public class Value {

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("message")
    private String message;

    @JsonProperty("date")
    private String date;

    @JsonProperty("author")
    private Author author;

    @JsonProperty("parents")
    private List<Parent> parents;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "Value{" +
                "hash='" + hash + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", author=" + author +
                ", parents=" + parents +
                '}';
    }
}
