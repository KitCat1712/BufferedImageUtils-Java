package io.kweakkweak.bufferedimageutils;

import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    ImageWriter abstractionWriter = new PNGImageWriter(new PNGImageWriterSpi());
    long nano1 = System.nanoTime();
    abstractionWriter.setOutput(new FileImageOutputStream(new File("examples\\waves\\sinusy.jpg")));
    abstractionWriter.write(BufferedImageUtils.toWaves(ImageIO.read(new File("examples\\waves\\NormAS.jpg")), 50));
    long nano2 = System.nanoTime();
    System.out.println(nano2 - nano1);

    // 361066300 - 1 method
  }
}
