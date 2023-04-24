package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.example.dto.SongDTO;
import org.example.service.Mp3MetadataExtractor;
import org.example.service.Mp3ProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

public class Mp3ProcessingServiceTest {

    private Mp3MetadataExtractor mp3MetadataExtractor;
    private Mp3ProcessingService mp3ProcessingService;

    @BeforeEach
    public void setUp() {
        mp3MetadataExtractor = Mockito.mock(Mp3MetadataExtractor.class);
        mp3ProcessingService = new Mp3ProcessingService(mp3MetadataExtractor);
    }

    @Test
    public void testProcessMp3File() throws IOException, TikaException, SAXException {
        // given
        byte[] testMp3Data = new byte[] { 1, 2, 3, 4 };
        int testResourceId = 1;
        String testTitle = "Test Title";
        String testArtist = "Test Artist";
        String testAlbum = "Test Album";
        String testDuration = "12345";
        int testYear = 2022;
        SongDTO expectedResult = SongDTO.builder()
            .album(testAlbum)
            .name(testTitle)
            .artist(testArtist)
            .length(testDuration)
            .resourceId(testResourceId)
            .year(testYear)
            .build();

        Metadata expectedMetadata = new Metadata();
        expectedMetadata.set("title", testTitle);
        expectedMetadata.set("xmpDM:artist", testArtist);
        expectedMetadata.set("xmpDM:album", testAlbum);
        expectedMetadata.set("xmpDM:duration", testDuration);
        expectedMetadata.set("xmpDM:releaseDate", String.valueOf(testYear));

        // when
        when(mp3MetadataExtractor.extractMetadata(testMp3Data))
            .thenReturn(expectedMetadata);
        SongDTO actualResult = mp3ProcessingService
            .processMp3File(testMp3Data, testResourceId);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
