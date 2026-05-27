package com.isa.domain.api;

import com.isa.domain.model.Appointment;
import com.isa.domain.model.LabDocument;
import com.isa.exception.NotFoundException;
import com.isa.service.AppointmentService;
import com.isa.service.LabDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("api/appointments")
@PreAuthorize("isAuthenticated()")
public class LabDocumentApi {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png");

    private final LabDocumentService labDocumentService;
    private final AppointmentService appointmentService;

    @Autowired
    public LabDocumentApi(LabDocumentService labDocumentService, AppointmentService appointmentService) {
        this.labDocumentService = labDocumentService;
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PostMapping("/{appointmentId}/lab-documents")
    public ResponseEntity<LabDocument> upload(@PathVariable long appointmentId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        final String originalName = file.getOriginalFilename();
        final String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase()
                : "";
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file type. Allowed types: PDF, JPG, PNG.");
        }
        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(labDocumentService.upload(appointment, file), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{appointmentId}/lab-documents")
    public ResponseEntity<LabDocument> getByAppointment(@PathVariable long appointmentId) {
        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(labDocumentService.listByAppointment(appointment.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/lab-documents/{id}")
    public ResponseEntity<LabDocument> get(@PathVariable Long id) {
        final LabDocument document = labDocumentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/lab-documents/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        final LabDocument document = labDocumentService.listByAppointment(id);
        final String filename = document.getOriginalFilename() != null ? document.getOriginalFilename() : "document";
        final MediaType mediaType = document.getContentType() != null
                ? MediaType.parseMediaType(document.getContentType())
                : MediaType.APPLICATION_OCTET_STREAM;
        final boolean isImage = mediaType.getType().equals("image");
        final ContentDisposition disposition = isImage
                ? ContentDisposition.inline().filename(filename).build()
                : ContentDisposition.attachment().filename(filename).build();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(document.getContent(), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @DeleteMapping("/lab-documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final LabDocument document = labDocumentService.get(id).orElseThrow(NotFoundException::new);
        labDocumentService.delete(document);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
