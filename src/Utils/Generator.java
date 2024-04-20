package Utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;

public class Generator {

    public static void generateAndSaveEAN13(String ean13Number, String filePath) throws WriterException, IOException {
        if (ean13Number.length() != 13) {
            throw new IllegalArgumentException("EAN-13 number must be 13 digits long");
        }

        EAN13Writer writer = new EAN13Writer();
        BitMatrix bitMatrix = writer.encode(ean13Number, BarcodeFormat.EAN_13, 400, 200);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (bitMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        graphics.dispose();

        // Save as PNG
        ImageIO.write(image, "png", new File(filePath));
    }

    public static void main(String[] args) {
        String ean13Number = "0000000000024";
        String filePath = "barcode.png"; // Replace with your desired path

        try {
            generateAndSaveEAN13(ean13Number, filePath);
            System.out.println("EAN-13 barcode saved successfully!");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

    }
}