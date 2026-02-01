package com.isa.util;

import com.isa.domain.model.Medication;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PrescriptionPdfReport {

    public static byte[] generatePdf(Medication medication) {
        try (PDDocument document = new PDDocument()) {

            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            final PDPageContentStream content = new PDPageContentStream(document, page);

            final float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            final float leading = 25;
            final float xLabel = margin;
            final float xValue = 200;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 18);
            content.newLineAtOffset(xLabel, yStart);
            content.showText("Prescription Report for: " + medication.getAppointment().getPatient().getFirstName() + " " + medication.getAppointment().getPatient().getLastName());
            content.endText();

            yStart -= 2 * leading;

            String formattedDate = "";
            final Instant instant = medication.getAppointment().getDateAndTime();
            if (instant != null) {
                formattedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.of("Europe/Belgrade")).format(instant);
            }

            float currentY = yStart;
            currentY = writeLine(content, xLabel, xValue, currentY, "Doctor:", medication.getAppointment().getDoctor().getFirstName() + " " + medication.getAppointment().getDoctor().getLastName());
            currentY = writeLine(content, xLabel, xValue, currentY, "Appointment Date:", formattedDate);
            currentY = writeLine(content, xLabel, xValue, currentY, "Name:", medication.getName());
            currentY = writeLine(content, xLabel, xValue, currentY, "Dosage:", medication.getDosage());
            currentY = writeLine(content, xLabel, xValue, currentY, "Frequency:", medication.getFrequency());
            currentY = writeLine(content, xLabel, xValue, currentY, "Instructions:", medication.getInstructions());
            currentY = writeWrappedLabelValue(content, xLabel, xValue, currentY,
                    "Notes:", medication.getNotes(),
                    PDType1Font.HELVETICA_BOLD, PDType1Font.HELVETICA, 12,
                    150, 350, 15);

            content.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create PDF report: " + e.getMessage(), e);
        }
    }

    private static float writeLine(PDPageContentStream content, float xLabel, float xValue, float y, String label, String value) throws IOException {
        if (value == null) value = "";

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.newLineAtOffset(xLabel, y);
        content.showText(label);
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 12);
        content.newLineAtOffset(xValue, y);
        content.showText(value);
        content.endText();

        return y - 20;
    }

    private static float writeWrappedLabelValue(PDPageContentStream content, float xLabel, float xValue, float y,
                                                String label, String value, PDType1Font fontLabel, PDType1Font fontValue,
                                                int fontSize, float maxWidthLabel, float maxWidthValue, float leading) throws IOException {

        String[] labelWords = label.split(" ");
        StringBuilder lineLabel = new StringBuilder();
        float currentY = y;

        for (String word : labelWords) {
            String tempLine = lineLabel.isEmpty() ? word : lineLabel + " " + word;
            float width = fontLabel.getStringWidth(tempLine) / 1000 * fontSize;
            if (width > maxWidthLabel) {
                content.beginText();
                content.setFont(fontLabel, fontSize);
                content.newLineAtOffset(xLabel, currentY);
                content.showText(lineLabel.toString());
                content.endText();

                lineLabel = new StringBuilder(word);
                currentY -= leading;
            } else {
                lineLabel = new StringBuilder(tempLine);
            }
        }

        if (!lineLabel.isEmpty()) {
            content.beginText();
            content.setFont(fontLabel, fontSize);
            content.newLineAtOffset(xLabel, currentY);
            content.showText(lineLabel.toString());
            content.endText();
            currentY -= leading;
        }

        String[] valueWords = (value != null ? value : "").split(" ");
        StringBuilder lineValue = new StringBuilder();
        for (String word : valueWords) {
            String tempLine = lineValue.isEmpty() ? word : lineValue + " " + word;
            float width = fontValue.getStringWidth(tempLine) / 1000 * fontSize;
            if (width > maxWidthValue) {
                content.beginText();
                content.setFont(fontValue, fontSize);
                content.newLineAtOffset(xValue, currentY);
                content.showText(lineValue.toString());
                content.endText();

                lineValue = new StringBuilder(word);
                currentY -= leading;
            } else {
                lineValue = new StringBuilder(tempLine);
            }
        }

        if (!lineValue.isEmpty()) {
            content.beginText();
            content.setFont(fontValue, fontSize);
            content.newLineAtOffset(xValue, currentY);
            content.showText(lineValue.toString());
            content.endText();
            currentY -= leading;
        }

        return currentY;
    }
}
