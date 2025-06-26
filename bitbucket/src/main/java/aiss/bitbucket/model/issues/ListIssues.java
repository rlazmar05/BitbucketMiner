package aiss.bitbucket.model.issues;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "size",
        "page",
        "pagelen",
        "next",
        "previous",
        "values"
})
public class ListIssues {

    @JsonProperty("size")
    private Integer size;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("pagelen")
    private Integer pagelen;
    @JsonProperty("next")
    private String next;
    @JsonProperty("previous")
    private String previous;

    @JsonProperty("values")
    private List<Issue> values;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<>();

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPagelen() {
        return pagelen;
    }

    public void setPagelen(Integer pagelen) {
        this.pagelen = pagelen;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Issue> getValues() {
        return values;
    }

    public void setValues(List<Issue> values) {
        this.values = values;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "ListIssues{" +
                "size=" + size +
                ", page=" + page +
                ", pagelen=" + pagelen +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", values=" + values +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
