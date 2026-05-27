package com.isa.util;

import com.isa.domain.model.Medication;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PrescriptionPdfReport {

    public static byte[] generatePdf(Medication medication) {
        try (PDDocument document = new PDDocument()) {

            final PDType0Font fontRegular = loadFont(document, "DejaVuSans.ttf");
            final PDType0Font fontBold = loadFont(document, "DejaVuSans-Bold.ttf");

            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            final PDPageContentStream content = new PDPageContentStream(document, page);

            final float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            final float leading = 25;

            content.beginText();
            content.setFont(fontBold, 18);
            content.newLineAtOffset(margin, yStart);
            content.showText("Prescription for: " + medication.getAppointment().getPatient().getFirstName() + " " + medication.getAppointment().getPatient().getLastName());
            content.endText();

            yStart -= 2 * leading;

            String formattedDate = "";
            final Instant instant = medication.getAppointment().getDateAndTime();
            if (instant != null) {
                formattedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.of("Europe/Belgrade")).format(instant);
            }

            float currentY = yStart;
            currentY = writeLine(content, fontBold, fontRegular, currentY, "Doctor:", medication.getAppointment().getDoctor().getFirstName() + " " + medication.getAppointment().getDoctor().getLastName());
            currentY = writeLine(content, fontBold, fontRegular, currentY, "Appointment Date:", formattedDate);
            currentY = writeLine(content, fontBold, fontRegular, currentY, "Name:", medication.getMedicament().getName());
            currentY = writeLine(content, fontBold, fontRegular, currentY, "Dosage:", medication.getMedicament().getDosage());
            currentY = writeLine(content, fontBold, fontRegular, currentY, "Frequency:", medication.getFrequency());
            currentY = writeWrappedLabelValue(content, currentY, "Instructions:", medication.getMedicament().getInstructions(), fontBold, fontRegular);
            currentY -= 5;
            writeWrappedLabelValue(content, currentY, "Notes:", medication.getNotes(), fontBold, fontRegular);

            content.close();

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create PDF report: " + e.getMessage(), e);
        }
    }

    private static float writeLine(PDPageContentStream content, PDType0Font fontBold, PDType0Font fontRegular,
                                   float y, String label, String value) throws IOException {
        if (value == null) value = "";
        value = value.replaceAll("[\n\r]", " ");

        content.beginText();
        content.setFont(fontBold, 12);
        content.newLineAtOffset((float) 50.0, y);
        content.showText(label);
        content.endText();

        content.beginText();
        content.setFont(fontRegular, 12);
        content.newLineAtOffset((float) 200.0, y);
        content.showText(value);
        content.endText();

        return y - 20;
    }

    private static float writeWrappedLabelValue(PDPageContentStream content, float y,
                                                String label, String value, PDType0Font fontLabel, PDType0Font fontValue) throws IOException {
        final String[] labelWords = label.split(" ");
        StringBuilder lineLabel = new StringBuilder();
        float currentY = y;

        for (String word : labelWords) {
            final String tempLine = lineLabel.isEmpty() ? word : lineLabel + " " + word;
            final float width = fontLabel.getStringWidth(tempLine) / 1000 * 12;
            if (width > (float) 150) {
                content.beginText();
                content.setFont(fontLabel, 12);
                content.newLineAtOffset((float) 50.0, currentY);
                content.showText(lineLabel.toString());
                content.endText();
                lineLabel = new StringBuilder(word);
                currentY -= (float) 15;
            } else {
                lineLabel = new StringBuilder(tempLine);
            }
        }
        if (!lineLabel.isEmpty()) {
            content.beginText();
            content.setFont(fontLabel, 12);
            content.newLineAtOffset((float) 50.0, currentY);
            content.showText(lineLabel.toString());
            content.endText();
            currentY -= (float) 15;
        }

        final String[] valueWords = (value != null ? value : "").split("[ \n\r]+");
        StringBuilder lineValue = new StringBuilder();
        for (String word : valueWords) {
            final String tempLine = lineValue.isEmpty() ? word : lineValue + " " + word;
            final float width = fontValue.getStringWidth(tempLine) / 1000 * 12;
            if (width > (float) 350) {
                content.beginText();
                content.setFont(fontValue, 12);
                content.newLineAtOffset((float) 200.0, currentY);
                content.showText(lineValue.toString());
                content.endText();
                lineValue = new StringBuilder(word);
                currentY -= (float) 15;
            } else {
                lineValue = new StringBuilder(tempLine);
            }
        }
        if (!lineValue.isEmpty()) {
            content.beginText();
            content.setFont(fontValue, 12);
            content.newLineAtOffset((float) 200.0, currentY);
            content.showText(lineValue.toString());
            content.endText();
            currentY -= (float) 15;
        }

        return currentY;
    }

    private static PDType0Font loadFont(PDDocument document, String fontFileName) throws IOException {
        return PDType0Font.load(document, new java.io.File("/usr/share/fonts/truetype/dejavu/" + fontFileName));
    }
}
