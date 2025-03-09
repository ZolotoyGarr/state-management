package javiki.course.serialization.compressor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class Compressor implements CompressionAlgorithm {
    /**
     * Сжимает файл с использованием алгоритма компрессии.
     *
     * @param sourceFile    Путь к исходному файлу.
     * @param compressedFile Путь для сохранения сжатого файла.
     * @throws IOException Если происходит ошибка ввода/вывода.
     */
    public void compressFile(Path sourceFile, Path compressedFile) throws IOException {
        byte[] inputData = Files.readAllBytes(sourceFile);
        byte[] compressedData = compress(inputData);
        Files.write(compressedFile, compressedData);
        System.out.println("✅ Файл сжат: " + compressedFile);
    }

    /**
     * Разжимает файл с использованием алгоритма компрессии.
     *
     * @param compressedFile Путь к сжатому файлу.
     * @param outputFile     Путь для сохранения разжатого файла.
     * @throws IOException Если происходит ошибка ввода/вывода.
     */
    public void decompressFile(Path compressedFile, Path outputFile) throws IOException {
        byte[] compressedData = Files.readAllBytes(compressedFile);
        byte[] decompressedData = decompress(compressedData);
        Files.write(outputFile, decompressedData);
        System.out.println("✅ Файл разжат: " + outputFile);
    }
}
