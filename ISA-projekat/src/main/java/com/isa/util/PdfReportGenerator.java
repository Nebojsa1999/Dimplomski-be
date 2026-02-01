package com.isa.util;

import com.isa.domain.model.AppointmentReport;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PdfReportGenerator {

    public static byte[] generatePdf(AppointmentReport report) {
        try (PDDocument document = new PDDocument()) {

            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            final PDPageContentStream content = new PDPageContentStream(document, page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float leading = 20;
            float xValue = 200;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 18);
            content.newLineAtOffset(margin, yStart);
            content.showText("Appointment Report for: " + report.getAppointment().getPatient().getFirstName() + " " + report.getAppointment().getPatient().getLastName());
            content.endText();

            yStart -= 2 * leading;

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            float currentY = yStart;
            final Instant instant = report.getAppointment().getDateAndTime();
            final ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Belgrade"));
            currentY = writeLine(content, margin, xValue, currentY, "Doctor:", report.getAppointment().getDoctor().getFirstName() + " " + report.getAppointment().getDoctor().getLastName());
            currentY = writeLine(content, margin, xValue, currentY, "Appointment Date:",
                    formatter.format(zdt));
            currentY = writeLine(content, margin, xValue, currentY, "Blood Type:", report.getBloodType().name());
            currentY = writeLine(content, margin, xValue, currentY, "Past Medical History:", report.getPastMedicalHistory());
            currentY = writeLine(content, margin, xValue, currentY, "Allergies:", report.getAllergies());
            currentY = writeLine(content, margin, xValue, currentY, "Family History:", report.getFamilyHistory());
            currentY = writeLine(content, margin, xValue, currentY, "Blood Pressure:", report.getBloodPressure());
            currentY = writeLine(content, margin, xValue, currentY, "Heart Rate:", report.getHearthRate());
            writeLine(content, margin, xValue, currentY, "Diagnosis:", report.getDiagnosis());

            content.close();

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create PDF report: " + e.getMessage(), e);
        }
    }

    private static float writeLine(PDPageContentStream content, float xLabel, float xValue, float y, String label, String value) throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.newLineAtOffset(xLabel, y);
        content.showText(label);
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 12);
        content.newLineAtOffset(xValue, y);
        content.showText(value != null ? value : "");
        content.endText();

        return y - 20;
    }
}
