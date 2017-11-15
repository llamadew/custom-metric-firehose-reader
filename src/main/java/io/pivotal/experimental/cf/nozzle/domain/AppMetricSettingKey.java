package io.pivotal.experimental.cf.nozzle.domain;

import java.io.Serializable;

public class AppMetricSettingKey implements Serializable {
    private String appGuid;
    private String metricName;


    public AppMetricSettingKey() {
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
}