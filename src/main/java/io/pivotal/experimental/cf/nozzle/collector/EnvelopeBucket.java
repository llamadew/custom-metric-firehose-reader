package io.pivotal.experimental.cf.nozzle.collector;

import org.cloudfoundry.doppler.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;

/**
 * Created by slhommedieu on 11/10/17.
 */
public class EnvelopeBucket {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvelopeBucket.class);


    ArrayDeque<Envelope> envelopes ;

    public EnvelopeBucket() {

    }

    public void acceptEnvelope(Envelope envelope){
        if(envelopes == null){
            envelopes = new ArrayDeque<Envelope>();
        }
        envelopes.addFirst(envelope);
        LOGGER.debug(envelope.toString());
    }

    public ArrayDeque<Envelope> getEnvelopes() {
        return envelopes;
    }

    public void reset() {
        envelopes.clear();
    }
}
