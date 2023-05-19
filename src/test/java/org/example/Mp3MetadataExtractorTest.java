package org.example;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.example.service.Mp3MetadataExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

@ExtendWith(MockitoExtension.class)
public class Mp3MetadataExtractorTest {


    private Mp3MetadataExtractor mp3MetadataExtractor;

    @BeforeEach
    public void setUp() {
        mp3MetadataExtractor = new Mp3MetadataExtractor();
    }

    @Test
    public void testExtractMetadata() throws IOException, TikaException, SAXException {
        // given
        InputStream data = getClass().getClassLoader()
            .getResourceAsStream("data/test.mp3");
        byte[] mp3Data = IOUtils.toByteArray(ofNullable(data)
            .orElse(InputStream.nullInputStream()));

        String expectedArtist = "In Flames";
        String expectedAlbum = "Foregone";
        String expectedGenre = "alternativemetal";
        int expectedMetadataSize = 19;

        // when
        Metadata actualMetadata = mp3MetadataExtractor.extractMetadata(mp3Data);

        // then
        assertThat(expectedMetadataSize).isEqualTo(actualMetadata.size());
        assertThat(expectedArtist).isEqualTo(actualMetadata.get("xmpDM:artist"));
        assertThat(expectedAlbum).isEqualTo(actualMetadata.get("xmpDM:album"));
        assertThat(expectedGenre).isEqualTo(actualMetadata.get("xmpDM:genre"));
    }
}




