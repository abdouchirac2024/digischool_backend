package com.digiSchool.digiSchool.user.serviceimp;

import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.repository.EleveRepository;
import com.digiSchool.digiSchool.user.service.StudentCardService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.net.URI;

@Service
public class StudentCardServiceImpl implements StudentCardService {

    private final EleveRepository eleveRepository;

    public StudentCardServiceImpl(EleveRepository eleveRepository) {
        this.eleveRepository = eleveRepository;
    }

    @Override
    public byte[] generateStudentCard(Long eleveId) throws Exception {
        Eleve eleve = eleveRepository.findById(eleveId)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));

        Document document = new Document(new Rectangle(242, 153)); // Standard ID-1 card size in points (approx 85.6mm x
                                                                   // 54mm)
        document.setMargins(10, 10, 10, 10);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLUE);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, Color.DARK_GRAY);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Color.BLACK);

        // Header Table
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        PdfPCell titleCell = new PdfPCell(new Phrase("CARTE SCOLAIRE", titleFont));
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.addCell(titleCell);
        document.add(header);

        document.add(new Paragraph(" ")); // Spacer

        // Main Content Table (Photo | Info | QR)
        float[] widths = { 30f, 45f, 25f };
        PdfPTable mainTable = new PdfPTable(widths);
        mainTable.setWidthPercentage(100);

        // Photo
        if (eleve.getPhotoUrl() != null && !eleve.getPhotoUrl().isEmpty()) {
            try {
                Image photo = Image.getInstance(URI.create(eleve.getPhotoUrl()).toURL());
                photo.scaleToFit(50, 60);
                PdfPCell photoCell = new PdfPCell(photo);
                photoCell.setBorder(Rectangle.NO_BORDER);
                photoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                mainTable.addCell(photoCell);
            } catch (Exception e) {
                mainTable.addCell(new PdfPCell(new Phrase("PHOTO", valueFont)));
            }
        } else {
            mainTable.addCell(new PdfPCell(new Phrase("PAS DE PHOTO", valueFont)));
        }

        // Student Info
        PdfPTable infoTable = new PdfPTable(1);
        infoTable.addCell(createCell("NOM:", eleve.getNom(), labelFont, valueFont));
        infoTable.addCell(createCell("PRENOM:", eleve.getPrenom(), labelFont, valueFont));
        infoTable.addCell(createCell("MATRICULE:", eleve.getMatricule(), labelFont, valueFont));

        PdfPCell infoContainer = new PdfPCell(infoTable);
        infoContainer.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(infoContainer);

        // QR Code
        String qrContent = "STUDENT:" + eleve.getMatricule();
        byte[] qrBytes = generateQRCodeImage(qrContent, 60, 60);
        Image qrImage = Image.getInstance(qrBytes);
        PdfPCell qrCell = new PdfPCell(qrImage);
        qrCell.setBorder(Rectangle.NO_BORDER);
        qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        mainTable.addCell(qrCell);

        document.add(mainTable);
        document.close();

        return out.toByteArray();
    }

    private PdfPCell createCell(String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        Phrase p = new Phrase();
        p.add(new Chunk(label + " ", labelFont));
        p.add(new Chunk(value != null ? value : "", valueFont));
        cell.addElement(p);
        return cell;
    }

    @Override
    public byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
