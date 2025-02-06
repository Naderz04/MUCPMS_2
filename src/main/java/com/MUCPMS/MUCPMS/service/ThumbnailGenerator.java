package com.MUCPMS.MUCPMS.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ThumbnailGenerator {

    public static void generateThumbnail(String inputFilePath, String outputFilePath, int width) throws IOException {
        try (PDDocument document = PDDocument.load(new File(inputFilePath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 150, ImageType.RGB); // Render the first page
            BufferedImage resizedImage = resizeImage(image, width); // Resize the image
            ImageIO.write(resizedImage, "png", new File(outputFilePath)); // Save the thumbnail
        }
    }

    static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) {
        int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth()));
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.createGraphics().drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        return resizedImage;
    }
}