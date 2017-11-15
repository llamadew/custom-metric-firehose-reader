package io.pivotal.experimental.cf.nozzle.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by slhommedieu on 11/9/17.
 */
@Entity
public class Metric {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private Long timestamp;
    private String appGuid;
    private String metricName;
    private Double value;
    private String aggregation;

    protected Metric() {
    }

    public Metric(Long timestamp, String appGuid, String metricName, Double value, String aggregation) {
        this.timestamp = timestamp;
        this.appGuid = appGuid;
        this.metricName = metricName;
        this.value = value;
        this.aggregation = aggregation;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppGuid() {
        return appGuid;
    }

    public void setAppGuid(String appGuid) {
        this.appGuid = appGuid;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }
}
