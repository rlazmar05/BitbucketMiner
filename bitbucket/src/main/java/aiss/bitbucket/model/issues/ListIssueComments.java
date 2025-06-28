package aiss.bitbucket.model.issues;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ListIssueComments {

    @JsonProperty("values")
    private List<IssueComments> values;

    public List<IssueComments> getValues() {
        return values;
    }

    public void setValues(List<IssueComments> values) {
        this.values = values;
    }
}
