package module1_4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class FileCopyDemo {

    private static final String SRC = "test_100mb.bin";
    private static final String DST_NO_BUFFER = "out_no_buffer.bin";
    private static final String DST_BUFFER = "out_buffer.bin";

    public static void main(String[] args) throws IOException {
        generateTestFile();

        System.out.println("=== Копирование БЕЗ буфера ===");
        long t1 = System.nanoTime();
        copyWithoutBuffer();
        long noBufferMs = (System.nanoTime() - t1) / 1_000_000;
        System.out.println("Время: " + noBufferMs + " ms");

        System.out.println("\n=== Копирование С буфером ===");
        long t2 = System.nanoTime();
        copyWithBuffer();
        long bufferMs = (System.nanoTime() - t2) / 1_000_000;
        System.out.println("Время: " + bufferMs + " ms");

        if (bufferMs > 0) {
            System.out.printf("Буферизация быстрее в %.1f раз%n",
                    (double) noBufferMs / bufferMs);
        }
    }

    // Генерируем файл заданного размера (МБ)
    private static void generateTestFile() throws IOException {
        System.out.println("Генерирую " + SRC );
        byte[] chunk = new byte[1024 * 1024];
        Random random = new Random();
        try (FileOutputStream out = new FileOutputStream(SRC)) {
            for (int i = 0; i < 100; i++) {
                random.nextBytes(chunk);
                out.write(chunk);
            }
        }
        System.out.println("Готово");
    }

    // Побайтовое копирование БЕЗ буфера.
    private static void copyWithoutBuffer() throws IOException {
        try (InputStream in = new FileInputStream(SRC);
             OutputStream out = new FileOutputStream(DST_NO_BUFFER)) {
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        }
    }

    // Копирование через BufferedInputStream/BufferedOutputStream.
    private static void copyWithBuffer() throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(SRC));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(DST_BUFFER))) {
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        }
    }
}

/*
   Вывод результатов в консоли:
   Генерирую test_100mb.bin
   Готово
   === Копирование БЕЗ буфера ===
   Время: 684646 ms

   === Копирование С буфером ===
   Время: 3756 ms
   Буферизация быстрее в 182,3 раз


   Объяснение:
   При вызове in.read() на обычном FileInputStream, Java не читает байт сама — она просит операционную систему.
   Каждый read() — это полный цикл, который выглядит примерно так:

   код → JVM → переход в режим ядра → системный вызов read() → ОС читает 1 байт → возврат в режим пользователя
   → байт в программе

   Один такой цикл стоит ~0.5–1 микросекунду на чтение. И столько же на запись -> десятки секунд.

   В свою очередь BufferedInputStream при первом read() делает один системный вызов, который читает сразу 8192 байта в
   свой внутренний массив (буфер). Дальше каждый read() отдаёт байт из этого массива в памяти — без системного вызова,
   без перехода в ядро.
   Меньше вызовов методов JVM, меньше переключений режимов процессора - дают существенный выигрыш во времени.
 */