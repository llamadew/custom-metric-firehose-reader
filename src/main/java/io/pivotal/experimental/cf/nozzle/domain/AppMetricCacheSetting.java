package io.pivotal.experimental.cf.nozzle.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by slhommedieu on 11/9/17.
 */
@Entity
public class AppMetricCacheSetting {

    @Id
    private String appGuid;

    public AppMetricCacheSetting(){}

    public AppMetricCacheSetting(String appGuid) {
        this.appGuid = appGuid;
    }

    public String getAppGuid() {
        return appGuid;
    }

    public void setAppGuid(String appGuid) {
        this.appGuid = appGuid;
    }

    @Override
    public String toString() {
        return "AppMetricSetting{" +
                ", appGuid='" + appGuid + '\'' +
                '}';
    }

}
