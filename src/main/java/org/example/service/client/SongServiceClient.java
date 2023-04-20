package org.example.service.client;

import org.example.config.RetryConfiguration;
import org.example.dto.SongDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "ms-song-service",
    url = "${service.song.endpoint}",
    configuration = RetryConfiguration.class)
public interface SongServiceClient {

    @PostMapping(path = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Integer> saveSong(SongDTO songDTO);

}
