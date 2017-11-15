package io.pivotal.experimental.cf.nozzle.web;

import io.pivotal.experimental.cf.nozzle.domain.Metric;
import io.pivotal.experimental.cf.nozzle.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by slhommedieu on 11/10/17.
 */

@RestController
public class MetricsController {

    @Autowired
    MetricRepository repository;

    @RequestMapping(value = "/apps/{appGuid}/metrics/{metricName:.+}", method = RequestMethod.GET)
    public List<Metric> getAppMetrics(@PathVariable(value = "appGuid") String appGuid,
                                      @PathVariable(value = "metricName") String metricName,
                                      @RequestParam(value = "aggregation", required = false) String aggregation) {

        List<Metric> metricAggregations;
        if (aggregation != null && !aggregation.isEmpty()) {
            metricAggregations = repository.findByAppGuidAndMetricNameAndAggregation(appGuid, metricName, aggregation);
        } else {
            metricAggregations = repository.findByAppGuidAndMetricName(appGuid, metricName);
        }

        return metricAggregations;
    }

    @RequestMapping(value = "/apps/{appGuid}/last_metric/{metricName:.+}", method = RequestMethod.GET)
    public List<Metric> getLastAppMetrics(@PathVariable(value = "appGuid") String appGuid,
                                          @PathVariable(value = "metricName") String metricName,
                                          @RequestParam(value = "aggregation", required = false) String aggregation) {

        List<Metric> metricAggregations;
        if (aggregation != null && !aggregation.isEmpty()) {
            metricAggregations = repository.findLatestAppGuidAndMetricNameandAndAggregation(appGuid, metricName, aggregation);
        } else {
            metricAggregations = repository.findLatestAppGuidAndMetricName(appGuid, metricName);
        }

        return metricAggregations;
    }

    @RequestMapping(value = "/apps/{appGuid}/{metricName:.+}/{aggregation}", method = RequestMethod.GET)
    public Metric getAppMetric(@PathVariable(value = "appGuid") String appGuid,
                               @PathVariable(value = "metricName") String metricName,
                               @PathVariable(value = "aggregation") String aggregation) {

        List<Metric> metricAggregations = repository.findLatestAppGuidAndMetricNameandAndAggregation(appGuid, metricName, aggregation);
        Metric aggregateMetric = metricAggregations.get(0);
        if (metricAggregations == null || metricAggregations.size() != 1) {
            aggregateMetric = new Metric(System.currentTimeMillis(), appGuid, metricName, null, aggregation);
        }
        return aggregateMetric;
    }
}