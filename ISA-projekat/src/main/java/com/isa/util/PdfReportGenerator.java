package com.isa.util;

import com.isa.domain.model.AppointmentReport;
import com.isa.domain.model.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PdfReportGenerator {

    private static final ZoneId ZONE = ZoneId.of("Europe/Belgrade");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static byte[] generatePdf(AppointmentReport report) {
        try (PDDocument document = new PDDocument()) {

            final PDType0Font fontRegular = loadFont(document, "DejaVuSans.ttf");
            final PDType0Font fontBold = loadFont(document, "DejaVuSans-Bold.ttf");

            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            final PDPageContentStream content = new PDPageContentStream(document, page);

            final float margin = 50;
            final float pageWidth = page.getMediaBox().getWidth();
            final float contentWidth = pageWidth - 2 * margin;
            float y = page.getMediaBox().getHeight() - margin;

            final User doctor = report.getAppointment().getDoctor();
            final User patient = report.getAppointment().getPatient();
            final ZonedDateTime zdt = report.getAppointment().getDateAndTime().atZone(ZONE);

            // ── One-row header ──────────────────────────────────────────────
            final String hospitalName = doctor.getHospital() != null ? doctor.getHospital().getName() : "";
            final String dateStr = FORMATTER.format(zdt);
            final String doctorName = doctor.getFirstName() + " " + doctor.getLastName()
                    + (doctor.getDepartment() != null ? " - " + doctor.getDepartment().getName() : "");

            final float sectionWidth = contentWidth / 3;
            writeHeaderCell(content, fontBold, fontRegular, margin,                    y, "Hospital:", hospitalName);
            writeHeaderCell(content, fontBold, fontRegular, margin + sectionWidth,     y, "Date:", dateStr);
            writeHeaderCell(content, fontBold, fontRegular, margin + 2 * sectionWidth, y, "Doctor:", doctorName);

            y -= 55;

            // ── Centered title ──────────────────────────────────────────────
            final String title = "Appointment Report";
            final float titleWidth = fontBold.getStringWidth(title) / 1000 * 16;
            content.beginText();
            content.setFont(fontBold, 16);
            content.newLineAtOffset((pageWidth - titleWidth) / 2, y);
            content.showText(title);
            content.endText();

            y -= 30;

            // ── Patient table ───────────────────────────────────────────────
            final String[] headers = {"Name", "Surname", "Address", "Personal ID", "Phone"};
            final String[] values = {
                    nvl(patient.getFirstName()),
                    nvl(patient.getLastName()),
                    nvl(patient.getAddress()),
                    nvl(patient.getPersonalId()),
                    nvl(patient.getPhone())
            };

            y = drawTable(content, fontBold, fontRegular, y, contentWidth, headers, values);

            y -= 35;

            // ── Report fields (only non-blank) ──────────────────────────────
            if (isNotBlank(report.getAnamnesis())) {
                y = writeLine(content, fontBold, fontRegular, y, "Anamnesis:", report.getAnamnesis());
            }
            if (isNotBlank(report.getAllergies())) {
                y = writeLine(content, fontBold, fontRegular, y, "Allergies:", report.getAllergies());
            }
            if (isNotBlank(report.getChronicDiseases())) {
                y = writeLine(content, fontBold, fontRegular, y, "Chronic Diseases:", report.getChronicDiseases());
            }
            if (isNotBlank(report.getBloodPressure())) {
                y = writeLine(content, fontBold, fontRegular, y, "Blood Pressure:", report.getBloodPressure());
            }
            if (isNotBlank(report.getHearthRate())) {
                y = writeLine(content, fontBold, fontRegular, y, "Heart Rate:", report.getHearthRate());
            }
            if (isNotBlank(report.getDiagnosis())) {
                y = writeLine(content, fontBold, fontRegular, y, "Diagnosis:", report.getDiagnosis());
            }
            if (isNotBlank(report.getTherapy())) {
                y = writeLine(content, fontBold, fontRegular, y, "Long-term therapy:", report.getTherapy());
            }
            if (isNotBlank(report.getNextControl())) {
                y = writeLine(content, fontBold, fontRegular, y, "Next Control:", report.getNextControl());
            }
            if (isNotBlank(report.getDoctorsComment())) {
                writeLine(content, fontBold, fontRegular, y, "Doctor's Comment:", report.getDoctorsComment());
            }

            content.close();

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create PDF report: " + e.getMessage(), e);
        }
    }

    private static void writeHeaderCell(PDPageContentStream content, PDType0Font fontBold, PDType0Font fontRegular,
                                        float x, float y, String label, String value) throws IOException {
        content.beginText();
        content.setFont(fontBold, 9);
        content.newLineAtOffset(x, y);
        content.showText(label);
        content.endText();

        content.beginText();
        content.setFont(fontRegular, 9);
        content.newLineAtOffset(x, y - 12);
        content.showText(value);
        content.endText();
    }

    private static float drawTable(PDPageContentStream content, PDType0Font fontBold, PDType0Font fontRegular,
                                   float y, float tableWidth,
                                   String[] headers, String[] values) throws IOException {
        final int cols = headers.length;
        final float colWidth = tableWidth / cols;
        final float rowHeight = 22;
        final float textPadding = 4;

        // Header row
        for (int i = 0; i < cols; i++) {
            final float cellX = (float) 50.0 + i * colWidth;
            drawCell(content, cellX, y - rowHeight, colWidth);
            content.beginText();
            content.setFont(fontBold, 9);
            content.newLineAtOffset(cellX + textPadding, y - rowHeight + textPadding + 4);
            content.showText(headers[i]);
            content.endText();
        }
        y -= rowHeight;

        // Value row
        for (int i = 0; i < cols; i++) {
            final float cellX = (float) 50.0 + i * colWidth;
            drawCell(content, cellX, y - rowHeight, colWidth);
            content.beginText();
            content.setFont(fontRegular, 9);
            content.newLineAtOffset(cellX + textPadding, y - rowHeight + textPadding + 4);
            content.showText(values[i]);
            content.endText();
        }
        y -= rowHeight;

        return y;
    }

    private static void drawCell(PDPageContentStream content, float x, float y, float width) throws IOException {
        content.setLineWidth(0.5f);
        content.addRect(x, y, width, (float) 22.0);
        content.stroke();
    }

    private static float writeLine(PDPageContentStream content, PDType0Font fontBold, PDType0Font fontRegular,
                                   float y, String label, String value) throws IOException {
        content.beginText();
        content.setFont(fontBold, 11);
        content.newLineAtOffset((float) 50.0, y);
        content.showText(label);
        content.endText();

        content.beginText();
        content.setFont(fontRegular, 11);
        content.newLineAtOffset((float) 200, y);
        content.showText(value != null ? value.replaceAll("[\n\r]", " ") : "");
        content.endText();

        return y - 30;
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private static String nvl(String value) {
        return value != null ? value : "";
    }

    private static PDType0Font loadFont(PDDocument document, String fontFileName) throws IOException {
        return PDType0Font.load(document, new java.io.File("/usr/share/fonts/truetype/dejavu/" + fontFileName));
    }
}
