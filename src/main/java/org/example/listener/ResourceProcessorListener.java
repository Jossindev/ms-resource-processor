package org.example.listener;

import org.example.service.ResourceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceProcessorListener {

    private final ResourceService resourceService;

    @RabbitListener(queues = "${queue.processing-queue}")
    public void listenMessage(int resourceId) {
        log.info("Received message: {} ", resourceId);
        try {
            resourceService.process(resourceId);
        } catch (final Exception ex) {
            log.error("Processing message {} with error: {}", resourceId, ex);
        }
    }
}
