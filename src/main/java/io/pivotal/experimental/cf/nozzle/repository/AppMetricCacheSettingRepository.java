package io.pivotal.experimental.cf.nozzle.repository;

import io.pivotal.experimental.cf.nozzle.domain.AppMetricCacheSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by slhommedieu on 11/9/17.
 */
@Repository
public interface AppMetricCacheSettingRepository extends CrudRepository<AppMetricCacheSetting, Long> {

    AppMetricCacheSetting findFirstByAppGuid(String appGuid);

}
