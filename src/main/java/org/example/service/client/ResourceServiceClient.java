package org.example.service.client;

import org.example.config.RetryConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "ms-resource-service",
    url = "${service.resource.endpoint}",
    configuration = RetryConfiguration.class)
public interface ResourceServiceClient {

    @GetMapping(path = "/resources/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<byte[]> getResource(@PathVariable Integer id);

}
