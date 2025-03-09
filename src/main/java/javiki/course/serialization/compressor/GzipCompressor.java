package javiki.course.serialization.compressor;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor extends Compressor {
    @Override
    public byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOut = new GZIPOutputStream(byteStream)) {
            gzipOut.write(data);
        }
        return byteStream.toByteArray();
    }

    @Override
    public byte[] decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(compressedData);
        try (GZIPInputStream gzipIn = new GZIPInputStream(byteStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = gzipIn.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
}
