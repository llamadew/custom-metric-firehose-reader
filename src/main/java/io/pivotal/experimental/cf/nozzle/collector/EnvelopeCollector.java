package io.pivotal.experimental.cf.nozzle.collector;

import org.cloudfoundry.doppler.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by slhommedieu on 11/10/17.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EnvelopeCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvelopeCollector.class);
    HashMap<String, HashMap<String, EnvelopeBucket>> appMetrics = new HashMap<String, HashMap<String, EnvelopeBucket>>();
    HashMap<String, Set<String>> appMetriNames = new HashMap<String, Set<String>>();

    public Envelope collect(Envelope envelope) {

        String appGuid = envelope.getTags().get("source_id");
        String metricName = envelope.getValueMetric().getName();
        //collect the metric
        HashMap<String, EnvelopeBucket> buckets = appMetrics.get(appGuid);
        if (buckets != null) {
            EnvelopeBucket bucket = buckets.get(metricName);
            if (bucket != null) {
                bucket.acceptEnvelope(envelope);
            }
        }

        //collect the metric name
        Set<String> metricNames = appMetriNames.get(envelope.getTags().get("source_id"));
        if (metricNames != null) {

            metricNames.add(metricName);
        }

        return envelope;
    }

    public void collectAppMetrics(String appGuid, String metricName) {

        HashMap<String, EnvelopeBucket> buckets = appMetrics.get(appGuid);
        if (buckets == null) {
            appMetrics.put(appGuid, new HashMap<String, EnvelopeBucket>());
            buckets = appMetrics.get(appGuid);
        }

        EnvelopeBucket bucket = buckets.get(metricName);
        if (bucket == null) {
            LOGGER.debug(String.format("Collecting %s Metrics for APP: %s", metricName, appGuid));
            buckets.put(metricName, new EnvelopeBucket());
        }
    }

    public void collectAppMetricNames(String appGuid) {

        Set<String> metricNames = appMetriNames.get(appGuid);
        if (metricNames == null) {
            appMetriNames.put(appGuid, new HashSet<String>());
            LOGGER.debug(String.format("Collecting Metric Names for APP: %s", appGuid));
        }
    }

    public void endAppMetricNames(String appGuid) {

        Set<String> metricNames = appMetriNames.get(appGuid);
        if (metricNames != null) {
            appMetriNames.remove(appGuid);
            LOGGER.debug(String.format("Ending Collection of Metric Names for APP: %s", appGuid));
        }
    }

    public void endAppMetrics(String appGuid, String metricName) {

        HashMap<String, EnvelopeBucket> buckets = appMetrics.get(appGuid);
        if (buckets != null) {
            buckets.remove(metricName);
            LOGGER.debug(String.format("Ending Collection of %s Metrics for APP: %s", metricName, appGuid));
        }
    }

    public EnvelopeBucket getAppMetrics(String appGuid, String metricName) {
        EnvelopeBucket bucket = null;
        HashMap<String, EnvelopeBucket> buckets = appMetrics.get(appGuid);
        if (buckets != null) {
            bucket = buckets.get(metricName);
        }
        return bucket;
    }


    public Set<String> getAppMetricNames(String appGuid) {
        Set<String> metricNames = appMetriNames.get(appGuid);
        return metricNames;
    }
}
