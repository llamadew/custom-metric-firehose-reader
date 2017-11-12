package io.pivotal.experimental.cf.nozzle.repository;

import io.pivotal.experimental.cf.nozzle.domain.Metric;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by slhommedieu on 11/9/17.
 */
@Repository
public interface MetricRepository  extends CrudRepository<Metric, Long> {

    List<Metric> findByAppGuidAndMetricName(String appGuid, String metricName);

    List<Metric> findByAppGuidAndMetricNameAndAggregation(String appGuid, String metricName, String aggregation);


    @Query(value = "select metric " +
            "from Metric metric " +
            "where metric.metricName = :metricName " +
            "and metric.appGuid = :appGuid " +
            "and metric.timestamp = (select max(metric1.timestamp) from Metric metric1 where metric1.metricName = :metricName and metric.appGuid = :appGuid)")
    List<Metric> findLatestAppGuidAndMetricName(@Param("appGuid") String appGuid, @Param("metricName") String metricName);

    @Query(value = "select metric " +
            "from Metric metric " +
            "where metric.metricName = :metricName " +
            "and metric.appGuid = :appGuid " +
            "and metric.aggregation = :aggregation " +
            "and metric.timestamp = (select max(metric1.timestamp) from Metric metric1 where metric1.metricName = :metricName and metric.appGuid = :appGuid)")
    List<Metric> findLatestAppGuidAndMetricNameandAndAggregation(@Param("appGuid") String appGuid, @Param("metricName") String metricName, @Param("aggregation") String aggregation);


    int deleteByTimestampBefore(@Param("timestamp") Long timestamp);

    int deleteByTimestampBeforeAndAppGuid(@Param("timestamp") Long timestamp, @Param("appGuid") String appGuid);

}
