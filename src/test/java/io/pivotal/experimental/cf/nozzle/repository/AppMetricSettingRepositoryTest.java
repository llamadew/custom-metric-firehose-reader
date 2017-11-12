package io.pivotal.experimental.cf.nozzle.repository;

import io.pivotal.experimental.cf.nozzle.domain.AppMetricSetting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AppMetricSettingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    AppMetricSettingRepository repository;

    @Test
    public void save() throws Exception {
        repository.save(new AppMetricSetting("123-123-123", "test.metric.name"));
        assert(repository.findByMetricName("test.metric.name").size()==1);
    }

    @Test
    public void findByName() throws Exception {
        repository.save(new AppMetricSetting("123-123-123", "test.metric.name"));
        assert(repository.findByMetricName("test.metric.name").size()==1);
        assert(((Iterable <AppMetricSetting>) repository.findAll()).iterator().hasNext());
    }

    @Test
    public void findByAppGuid() throws Exception {
        repository.save(new AppMetricSetting("123-123-123", "test.metric.name"));
        repository.save(new AppMetricSetting("123-123-1234", "test.metric.name"));

        assert(repository.findByAppGuid("123-123-123").size()==1);

    }

    @Test
    public void findAll() throws Exception {
        repository.save(new AppMetricSetting("123-123-123", "test.metric.name"));
        repository.save(new AppMetricSetting("123-123-123", "test.metric.name2"));
        Iterator<AppMetricSetting> metricsettings = repository.findAll().iterator();

        assert(metricsettings.hasNext());
        metricsettings.next();
        assert(metricsettings.hasNext());
    }

    @Test
    public void findByAppGuidAndMetricName() throws Exception {
        repository.save(new AppMetricSetting("123-123-123", "test.metric.name"));
        repository.save(new AppMetricSetting("123-123-123", "test.metric.name2"));
        repository.save(new AppMetricSetting("123-123-1234", "test.metric.name2"));

        Iterator<AppMetricSetting> metricsettings = repository.findByAppGuidAndMetricName("123-123-123", "test.metric.name2").iterator();

        assert(metricsettings.hasNext());
        metricsettings.next();
        assert(!metricsettings.hasNext());
    }




}