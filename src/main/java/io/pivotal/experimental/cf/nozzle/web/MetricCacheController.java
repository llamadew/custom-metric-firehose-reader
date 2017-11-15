package io.pivotal.experimental.cf.nozzle.web;

import io.pivotal.experimental.cf.nozzle.collector.EnvelopeCollector;
import io.pivotal.experimental.cf.nozzle.domain.AppMetricCacheSetting;
import io.pivotal.experimental.cf.nozzle.repository.AppMetricCacheSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Created by slhommedieu on 11/10/17.
 */

@RestController
public class MetricCacheController {

    @Autowired
    AppMetricCacheSettingRepository repository;

    @Autowired
    EnvelopeCollector envelopeCollector;

    @RequestMapping(value = "/apps/{appGuid}/metric_cache", method = RequestMethod.POST)
    public AppMetricCacheSetting createAppMetricSettings(@PathVariable(value = "appGuid") String appGuid) {

        AppMetricCacheSetting setting = new AppMetricCacheSetting(appGuid);
        repository.save(setting);

        return setting;
    }

    @RequestMapping(value = "/apps/{appGuid}/metric_cache", method = RequestMethod.DELETE)
    public AppMetricCacheSetting deleteAppMetricCacheSettings(@PathVariable(value = "appGuid") String appGuid) {

        AppMetricCacheSetting setting = new AppMetricCacheSetting(appGuid);
        repository.delete(setting);

        envelopeCollector.endAppMetricNames(appGuid);

        return setting;
    }

    @RequestMapping(value = "/apps/{appGuid}/metric_cache/names", method = RequestMethod.GET)
    public Set<String> getAppMetricCache(@PathVariable(value = "appGuid", required = true) String appGuid) {

        Set<String> names = envelopeCollector.getAppMetricNames(appGuid);
        return names;
    }

    @RequestMapping(value = "/apps/{appGuid}/metric_cache", method = RequestMethod.GET)
    public AppMetricCacheSetting getAppMetricCacheSettings(@PathVariable(value = "appGuid", required = true) String appGuid) {
        AppMetricCacheSetting settings = repository.findFirstByAppGuid(appGuid);

        return settings;
    }
}
