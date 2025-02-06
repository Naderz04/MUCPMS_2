package com.MUCPMS.MUCPMS.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ThumbnailService {

    public void generateThumbnail(String filePath, String thumbnailPath) throws IOException {
        String fileExtension = getFileExtension(filePath).toLowerCase();

        if ("pdf".equals(fileExtension)) {
            generatePdfThumbnail(filePath, thumbnailPath);
        } else if ("docx".equals(fileExtension) || "doc".equals(fileExtension)) {
            generateWordThumbnail(filePath, thumbnailPath);
        }
    }

    private void generatePdfThumbnail(String pdfPath, String thumbnailPath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300); // Render first page
            ImageIO.write(bim, "PNG", new File(thumbnailPath));
        }
    }

    private void generateWordThumbnail(String wordPath, String thumbnailPath) throws IOException {
        try (XWPFDocument document = new XWPFDocument(new FileInputStream(wordPath))) {
            // Create a simple placeholder for Word files (you might want to implement a better rendering)
            BufferedImage bim = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bim.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 64, 64);
            g2d.setColor(Color.BLACK);
            g2d.drawString("Word File", 10, 30);
            ImageIO.write(bim, "PNG", new File(thumbnailPath));
        }
    }

    private String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf('.') + 1);
    }
}