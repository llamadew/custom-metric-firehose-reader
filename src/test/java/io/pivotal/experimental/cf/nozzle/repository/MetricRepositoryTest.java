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

import javax.persistence.Entity;
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
        Metric e = repository.save(new Metric(System.currentTimeMillis(),"appGuid1", "test.metric.unit",  1d, "average"));
        assert (e!=null);
    }
}