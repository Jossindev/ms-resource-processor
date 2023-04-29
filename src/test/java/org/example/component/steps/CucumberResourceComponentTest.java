package org.example.component.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.xml.sax.SAXException;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class CucumberResourceComponentTest {

    @Mock
    private ResourceServiceClient resourceClient;

    @Mock
    private SongServiceClient songClient;

    @Mock
    private Mp3ProcessingService processingService;

    @InjectMocks
    private ResourceFacade resourceFacade;

    private ResourceProcessorListener resourceProcessorListener;

    @Given("a resource with ID {string}")
    public void resourceWithId(String resourceId) {
        MockitoAnnotations.openMocks(this);
        resourceProcessorListener = new ResourceProcessorListener(resourceFacade);
    }

    @When("the message with resource ID {string} is received")
    public void messageWithResourceIdIsReceived(String resourceId) throws Exception {
        int id = Integer.parseInt(resourceId);
        byte[] mp3Data = new byte[] {};
        SongDTO song = SongDTO.builder().build();
        ResponseEntity<byte[]> resourceResponse = new ResponseEntity<>(mp3Data, HttpStatus.OK);
        ResponseEntity<Integer> songResponse = new ResponseEntity<>(100, HttpStatus.CREATED);

        when(resourceClient.getResource(id)).thenReturn(resourceResponse);
        when(processingService.processMp3File(any(), eq(id))).thenReturn(song);
        when(songClient.saveSong(any())).thenReturn(songResponse);

        resourceProcessorListener.listenMessage(id);
    }

    @Then("the resource should be processed successfully")
    public void resourceShouldBeProcessedSuccessfully() throws TikaException, IOException, SAXException {
        verify(resourceClient, times(1)).getResource(anyInt());
        verify(processingService, times(1)).processMp3File(any(), anyInt());
        verify(songClient, times(1)).saveSong(any());
    }
}
