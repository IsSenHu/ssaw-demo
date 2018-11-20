package com.ssaw.ssawstreamhello.sink;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import java.io.UnsupportedEncodingException;

@Slf4j
@EnableBinding(Sink.class)
public class SinkServer {

    @StreamListener(Sink.INPUT)
    public void receive(Object payload) throws UnsupportedEncodingException {
        log.info("Received:{}", new String((byte[]) payload, "UTF-8"));
    }
}
