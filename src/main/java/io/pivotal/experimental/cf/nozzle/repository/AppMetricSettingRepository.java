package io.pivotal.experimental.cf.nozzle.repository;

import io.pivotal.experimental.cf.nozzle.domain.AppMetricSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by slhommedieu on 11/9/17.
 */
@Repository
public interface AppMetricSettingRepository extends CrudRepository<AppMetricSetting, Long> {

    List<AppMetricSetting> findByMetricName(String metricName);

    List<AppMetricSetting> findByAppGuid(String appGuid);

    List<AppMetricSetting> findByAppGuidAndMetricName(String appGuid, String metricName);

}
