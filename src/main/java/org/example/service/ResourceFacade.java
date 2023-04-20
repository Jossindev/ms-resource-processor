package org.example.service;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.example.dto.SongDTO;
import org.example.service.client.ResourceServiceClient;
import org.example.service.client.SongServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceFacade {

    private final ResourceServiceClient resourceClient;
    private final SongServiceClient songClient;
    private final Mp3ProcessingService processingService;

    public Integer process(int resourceId) throws TikaException, IOException, SAXException {
        ResponseEntity<byte[]> resource = resourceClient.getResource(resourceId);
        SongDTO song = processingService.processMp3File(resource.getBody(), resourceId);
        return songClient.saveSong(song).getBody();
    }
}
