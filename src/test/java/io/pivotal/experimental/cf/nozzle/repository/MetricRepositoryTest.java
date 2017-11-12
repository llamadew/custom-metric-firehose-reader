package io.pivotal.experimental.cf.nozzle.repository;

import io.pivotal.experimental.cf.nozzle.domain.Metric;
import io.pivotal.experimental.cf.nozzle.repository.MetricRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MetricRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    MetricRepository repository;

    @Test
    public void save() throws Exception {
        repository.save(new Metric("test.metric.name", 1d, "test.metric.unit"));
        assert(repository.findByName("test.metric.name").size()==1);
        assert(((Iterable <Metric>) repository.findAll()).iterator().hasNext());
    }

    @Test
    public void findByName() throws Exception {
        repository.save(new Metric("test.metric.name", 1d, "test.metric.unit"));
        assert(repository.findByName("test.metric.name").size()==1);
        assert(((Iterable <Metric>) repository.findAll()).iterator().hasNext());
    }

    @Test
    public void findAll() throws Exception {
        repository.save(new Metric("test.metric.name", 1d, "test.metric.unit"));
        repository.save(new Metric("test.metric.name", 2d, "test.metric.unit"));
        Iterator<Metric> metrics = repository.findAll().iterator();

        assert(metrics.hasNext());
        metrics.next();
        assert(metrics.hasNext());
    }




}