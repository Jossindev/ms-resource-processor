package org.example.service;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.example.dto.SongDTO;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Mp3ProcessingService {

    private final Mp3MetadataExtractor mp3MetadataExtractor;

    public SongDTO processMp3File(byte[] mp3Data, int resourceId) throws IOException, TikaException, SAXException {
        Metadata metadata = mp3MetadataExtractor.extractMetadata(mp3Data);

        String title = metadata.get("title");
        String artist = metadata.get("xmpDM:artist");
        String album = metadata.get("xmpDM:album");
        String duration = metadata.get("xmpDM:duration");
        String year = metadata.get("xmpDM:releaseDate");

        return SongDTO.builder()
            .name(title)
            .artist(artist)
            .album(album)
            .length(duration)
            .resourceId(resourceId)
            .year(Integer.parseInt(year))
            .build();
    }
}
