package org.example.service;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResourceProcessorNotifier {
    private final RabbitMessagingTemplate rabbitTemplate;
    private final String successQueue;

    @Autowired
    public ResourceProcessorNotifier(@Value("${queue.success-queue}") String processingQueue,
                                               RabbitMessagingTemplate rabbitTemplate) {
        this.successQueue = processingQueue;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Retryable(value = MessagingException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void notifyResourceProcessedSuccessfully(int resourceId) {
        try {
            log.info("Try to send notification to success queue...");
            rabbitTemplate.convertAndSend(successQueue, resourceId);
        } catch (Exception e) {
            log.error("Exception occurred while sending resource with id:{} to the {}", resourceId, successQueue, e);
            throw e;
        }
    }
}