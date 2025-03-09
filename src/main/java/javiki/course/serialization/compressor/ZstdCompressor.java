package javiki.course.serialization.compressor;

import com.github.luben.zstd.Zstd;

import java.io.IOException;

public class ZstdCompressor extends Compressor {
    public byte[] compress(byte[] data) throws IOException {
        return Zstd.compress(data);
    }

    public byte[] decompress(byte[] compressedData) throws IOException {
        return Zstd.decompress(compressedData, compressedData.length * 10);
    }
}
