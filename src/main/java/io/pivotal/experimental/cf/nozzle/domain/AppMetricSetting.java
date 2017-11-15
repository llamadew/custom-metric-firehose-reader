package io.pivotal.experimental.cf.nozzle.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by slhommedieu on 11/9/17.
 */
@Entity
@IdClass(AppMetricSettingKey.class)
public class AppMetricSetting {

    @Id
    private String appGuid;
    @Id
    private String metricName;


    public AppMetricSetting() {
    }

    public AppMetricSetting(String appGuid, String metricName) {
        this.appGuid = appGuid;
        this.metricName = metricName;
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

    @Override
    public String toString() {
        return "AppMetricSetting{" +
                ", appGuid='" + appGuid + '\'' +
                ", metricName='" + metricName + '\'' +
                '}';
    }

}
