package example.code.mvpexample.network.model.apipojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {


    @SerializedName("resultCount")
    @Expose
    private Integer resultCount;
    @SerializedName("results")
    @Expose
    private List<Wifi> results = null;

    /**
     * Gets result count.
     *
     * @return the result count
     */
    public Integer getResultCount() {
        return resultCount;
    }

    /**
     * Sets result count.
     *
     * @param resultCount the result count
     */
    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    /**
     * Gets results.
     *
     * @return the results
     */
    public List<Wifi> getResults() {
        return results;
    }

    /**
     * Sets results.
     *
     * @param results the results
     */
    public void setResults(List<Wifi> results) {
        this.results = results;
    }


}
