package io.pivotal.experimental.cf.nozzle;

import io.pivotal.experimental.cf.nozzle.collector.EnvelopeCollector;
import io.pivotal.experimental.cf.nozzle.domain.AppMetricSetting;
import io.pivotal.experimental.cf.nozzle.repository.AppMetricSettingRepository;
import org.cloudfoundry.doppler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@SpringBootApplication
public class CfFirehoseClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfFirehoseClientApplication.class, args);
	}

}

@Component
class CfFirehoseClientCommandLineRunner implements CommandLineRunner {

	private final DopplerClient dopplerClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(CfFirehoseClientCommandLineRunner.class);

	@Value("${metric.origin:metrics-forwarder}")
	private String metricOrigin;

	@Autowired
	public CfFirehoseClientCommandLineRunner(DopplerClient dopplerClient, EnvelopeCollector envelopeCollector) {
		this.dopplerClient = dopplerClient;
		this.envelopeCollector = envelopeCollector;
	}

	private final EnvelopeCollector envelopeCollector;

	@Override
	public void run(String... args) throws Exception {

		Flux<Envelope> cfEvents = this.dopplerClient.firehose(
				FirehoseRequest
						.builder()
						.subscriptionId(UUID.randomUUID().toString()).build());

		LOGGER.info("Collecting Envelopes from the firehose");
		cfEvents.filter(e -> e.getOrigin().equals(metricOrigin))
				.filter(e -> e.getEventType().equals(EventType.VALUE_METRIC))
				.subscribe(e -> envelopeCollector.collect(e));
	}
}
