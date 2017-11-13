package io.pivotal.experimental.cf.nozzle.web;

import io.pivotal.experimental.cf.nozzle.collector.EnvelopeCollector;
import io.pivotal.experimental.cf.nozzle.domain.AppMetricSetting;
import io.pivotal.experimental.cf.nozzle.repository.AppMetricSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by slhommedieu on 11/10/17.
 */

@RestController
public class MetricCollectionController {

    @Autowired
    AppMetricSettingRepository repository;

    @Autowired
    EnvelopeCollector envelopeCollector;

    @RequestMapping(value = "/apps/{appGuid}/metric_collection/{metricName:.+}", method = RequestMethod.POST)
    public AppMetricSetting createAppMetricSettings(@PathVariable (value="appGuid") String appGuid, @PathVariable(value="metricName") String metricName) {

        AppMetricSetting setting = new AppMetricSetting(appGuid, metricName);
        repository.save(setting);

        return setting;
    }

    @RequestMapping(value = "/apps/{appGuid}/metric_collection/{metricName:.+}", method = RequestMethod.DELETE)
    public AppMetricSetting deleteAppMetricSettings(@PathVariable (value="appGuid") String appGuid, @PathVariable(value="metricName") String metricName) {

        AppMetricSetting setting = new AppMetricSetting(appGuid, metricName);
        repository.delete(setting);

        envelopeCollector.endAppMetrics(appGuid,metricName);

        return setting;
    }

    @RequestMapping(value = "/apps/{appGuid}/metric_collection", method = RequestMethod.GET)
    public List<AppMetricSetting> getAppMetricSettings(@PathVariable (value="appGuid", required = true) String appGuid, @RequestParam(value="metricName", required = false) String metricName) {
        List<AppMetricSetting> settings = new ArrayList<AppMetricSetting>();


        if (appGuid != null && !appGuid.isEmpty()) {
            if (metricName != null && !metricName.isEmpty()) {
                settings = repository.findByAppGuidAndMetricName(appGuid, metricName);
            }else{
                settings = repository.findByAppGuid(appGuid);
            }
        }else {
            if (metricName != null && !metricName.isEmpty()) {
                settings = repository.findByMetricName(metricName);
            }else{
                settings =   StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(repository.findAll().iterator(),
                                Spliterator.ORDERED), false).collect(
                        Collectors.<AppMetricSetting> toList());
            }
        }
        return settings;
    }
}
