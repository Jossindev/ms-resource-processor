package org.example.listener;

import org.example.service.ResourceFacade;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceProcessorListener {

    private final ResourceFacade resourceService;

    @RabbitListener(queues = "${queue.processing-queue}")
    public void listenMessage(int resourceId) {
        log.info("Received message. ResourceId = {}", resourceId);
        try {
            Integer songId = resourceService.process(resourceId);
            log.info("Processing message with songId = {} finished successfully!", songId);
        } catch (final Exception ex) {
            log.error("Processing message {} with error: ", resourceId, ex);
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
