package io.pivotal.experimental.cf.nozzle.worker;

import io.pivotal.experimental.cf.nozzle.collector.EnvelopeBucket;
import io.pivotal.experimental.cf.nozzle.collector.EnvelopeCollector;
import io.pivotal.experimental.cf.nozzle.domain.AppMetricCacheSetting;
import io.pivotal.experimental.cf.nozzle.domain.AppMetricSetting;
import io.pivotal.experimental.cf.nozzle.domain.Metric;
import io.pivotal.experimental.cf.nozzle.repository.AppMetricCacheSettingRepository;
import io.pivotal.experimental.cf.nozzle.repository.AppMetricSettingRepository;
import io.pivotal.experimental.cf.nozzle.repository.MetricRepository;
import org.cloudfoundry.doppler.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.sql.Time;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;

/**
 * Created by slhommedieu on 11/10/17.
 */
@Controller
@Configuration
@EnableScheduling
public class MetricMonitorWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricMonitorWorker.class);


    @Autowired
    AppMetricSettingRepository appMetricSettingRepository;

    @Autowired
    AppMetricCacheSettingRepository appMetricCacheSettingRepository;

    @Autowired
    EnvelopeCollector envelopeCollector;


    @Autowired
    MetricRepository metricsRepository;

    @Scheduled(fixedDelay = 5000)
    public void monitorAppMetric() {
        LOGGER.debug(
                "Checking for new apps/metrics to monitor - " + System.currentTimeMillis() / 1000);
        Iterator<AppMetricSetting> appMetricSettingIterator = appMetricSettingRepository.findAll().iterator();

        appMetricSettingIterator.forEachRemaining(o -> envelopeCollector.collectAppMetrics(o.getAppGuid(), o.getMetricName()));
    }

    @Scheduled(fixedDelay = 5000)
    public void cacheAppMetricNames() {
        LOGGER.debug(
                "Caching metric names for new apps- " + System.currentTimeMillis() / 1000);
        Iterator<AppMetricCacheSetting> appMetricCacheSettingIterator = appMetricCacheSettingRepository.findAll().iterator();

        appMetricCacheSettingIterator.forEachRemaining(o -> envelopeCollector.collectAppMetricNames(o.getAppGuid()));
    }


    @Scheduled(fixedDelay = 30000)
    public void aggregateAppMetric() {
        LOGGER.debug(
                "Running Aggregations @  " + System.currentTimeMillis() / 1000);
        Iterator<AppMetricSetting> appMetricSettingIterator = appMetricSettingRepository.findAll().iterator();

        appMetricSettingIterator.forEachRemaining(o -> {
            EnvelopeBucket b = envelopeCollector.getAppMetrics(o.getAppGuid(), o.getMetricName());
            if (b != null && b.getEnvelopes()!=null) {
                    ArrayDeque<Envelope> envelopes = b.getEnvelopes().clone();
                    //should probably be a rolling window.
                    b.reset();
                    DoubleSummaryStatistics stats = envelopes.stream().filter(e -> e!=null).mapToDouble((x) -> x.getValueMetric().value())
                            .summaryStatistics();

                LOGGER.debug(String.format("App: %s --- Metric: %s --- Average: %s", o.getAppGuid(), o.getMetricName(), stats.getAverage()));

                ArrayList<Metric> metrics = new ArrayList<Metric>();
                Long timestamp = System.currentTimeMillis();

                metrics.add(new Metric(timestamp, o.getAppGuid(), o.getMetricName(), envelopes.getFirst().getValueMetric().value(), "last"));
                metrics.add(new Metric(timestamp, o.getAppGuid(), o.getMetricName(), stats.getAverage(), "average"));
                metrics.add(new Metric(timestamp, o.getAppGuid(), o.getMetricName(), stats.getMax(), "max"));
                metrics.add(new Metric(timestamp, o.getAppGuid(), o.getMetricName(), stats.getMin(), "min"));
                metrics.add(new Metric(timestamp, o.getAppGuid(), o.getMetricName(), stats.getSum(), "sum"));

                metricsRepository.save(metrics);
            }
        });
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void pruneAppMetric() {
        LOGGER.debug(
                "Running Pruning @  " + System.currentTimeMillis() / 1000);

        metricsRepository.deleteByTimestampBefore(System.currentTimeMillis()- 60*1000 );
    }


}