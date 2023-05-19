package org.example.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
@EqualsAndHashCode
public class SongDTO {

    private String name;
    private String artist;
    private String album;
    private String length;
    private Integer resourceId;
    private Integer year;

}

