package org.example.component;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.example.dto.SongDTO;
import org.example.listener.ResourceProcessorListener;
import org.example.service.Mp3ProcessingService;
import org.example.service.ResourceFacade;
import org.example.service.client.ResourceServiceClient;
import org.example.service.client.SongServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.xml.sax.SAXException;

@ExtendWith(MockitoExtension.class)
public class ResourceProcessorComponentTest {

    @Mock
    private ResourceServiceClient resourceClient;

    @Mock
    private SongServiceClient songClient;

    @Mock
    private Mp3ProcessingService processingService;

    private ResourceProcessorListener resourceProcessorListener;

    @BeforeEach
    void setUp() {
        ResourceFacade resourceFacade = new ResourceFacade(resourceClient, songClient, processingService);
        resourceProcessorListener = new ResourceProcessorListener(resourceFacade);
    }

    @Test
    void shouldSuccessfullyProcessMessage() throws TikaException, IOException, SAXException {
        int resourceId = 1;
        byte[] mp3Data = new byte[] {};
        SongDTO song = SongDTO.builder().build();
        ResponseEntity<byte[]> resourceResponse = new ResponseEntity<>(mp3Data, HttpStatus.OK);
        ResponseEntity<Integer> songResponse = new ResponseEntity<>(100, HttpStatus.CREATED);

        when(resourceClient.getResource(resourceId)).thenReturn(resourceResponse);
        when(processingService.processMp3File(mp3Data, resourceId)).thenReturn(song);
        when(songClient.saveSong(song)).thenReturn(songResponse);

        resourceProcessorListener.listenMessage(resourceId);

        verify(resourceClient, times(1)).getResource(resourceId);
        verify(processingService, times(1)).processMp3File(mp3Data, resourceId);
        verify(songClient, times(1)).saveSong(song);
    }

    @Test
    void shouldThrowAmqpRejectAndDontRequeueException() throws TikaException, IOException, SAXException {
        int resourceId = 1;
        when(resourceClient.getResource(resourceId)).thenThrow(RuntimeException.class);

        try {
            resourceProcessorListener.listenMessage(resourceId);
        } catch (AmqpRejectAndDontRequeueException ex) {
            // Expected exception
        }

        verify(resourceClient, times(1)).getResource(resourceId);
        verify(processingService, never()).processMp3File(any(), anyInt());
        verify(songClient, never()).saveSong(any());
    }
}


