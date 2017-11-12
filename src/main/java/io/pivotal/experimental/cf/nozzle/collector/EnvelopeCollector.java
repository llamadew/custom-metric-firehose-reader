package io.pivotal.experimental.cf.nozzle.collector;

import org.cloudfoundry.doppler.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
/**
 * Created by slhommedieu on 11/10/17.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EnvelopeCollector {

    HashMap<String, HashMap<String, EnvelopeBucket>> appMetrics = new HashMap<String, HashMap<String, EnvelopeBucket>>();

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvelopeCollector.class);


    public Envelope collect(Envelope envelope){

        HashMap<String, EnvelopeBucket> buckets = appMetrics.get(envelope.getTags().get("source_id"));
        if (buckets != null)
        {
            EnvelopeBucket bucket = buckets.get(envelope.getValueMetric().getName());
            if (bucket != null) {
                bucket.acceptEnvelope(envelope);
            }
        }
        return envelope;
    }


    public void collectAppMetrics(String appGuid, String metricName){

        HashMap<String, EnvelopeBucket> buckets = appMetrics.get(appGuid);
        if (buckets == null)
        {
            appMetrics.put(appGuid, new HashMap<String, EnvelopeBucket>());
            buckets = appMetrics.get(appGuid);
        }

        EnvelopeBucket bucket = buckets.get(metricName);
        if (bucket == null) {
            LOGGER.debug(String.format("Collecting %s Metrics for APP: %s",metricName, appGuid));
            buckets.put(metricName, new EnvelopeBucket());
        }


    }

    public EnvelopeBucket getAppMetrics(String appGuid, String metricName){
        EnvelopeBucket bucket = null;
        HashMap<String, EnvelopeBucket> buckets = appMetrics.get(appGuid);
        if (buckets != null) {
            bucket = buckets.get(metricName);
        }
        return bucket;
    }
}
