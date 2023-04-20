package org.example.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Service
public class Mp3MetadataExtractor {

    public Metadata extractMetadata(byte[] mp3Data) throws IOException, TikaException, SAXException {
        InputStream inputStream = new ByteArrayInputStream(mp3Data);

        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseContext = new ParseContext();

        parser.parse(inputStream, handler, metadata, parseContext);

        return metadata;
    }
}
