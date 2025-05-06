package com.softwareA.patient.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.softwareA.patient.dto.Staff;
import com.softwareA.patient.dto.response.PatientGeneralDTO;
import com.softwareA.patient.dto.response.PrescriptionResponse;
import com.softwareA.patient.model.medical_order.MedicalOrder_OrderItem;
import com.softwareA.patient.model.prescription.Prescription_Medication;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PrescriptionPDFPrinter implements PrescriptionPrinter {
    @Override
    public byte[] print(PrescriptionResponse prescriptionResponse) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            BaseFont baseFont = loadBaseFont();
            Font titleFont = new Font(baseFont, 16, Font.BOLD);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);

            addHeader(doc, prescriptionResponse, titleFont, normalFont);
            addPatientInfo(doc, prescriptionResponse.getPatient(), boldFont, normalFont);
            addDoctorInfo(doc, prescriptionResponse.getDoctor(), boldFont, normalFont);
            addMedicationDetails(doc, prescriptionResponse.getMedications(), boldFont, normalFont);
            addNotes(doc, boldFont, normalFont);
            addSignatureBlock(doc, boldFont, normalFont);
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private BaseFont loadBaseFont() throws Exception {
        File fontFile = ResourceUtils.getFile("classpath:fonts/times.ttf");
        return BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    private void addHeader(Document doc, PrescriptionResponse prescription, Font titleFont, Font normalFont) throws DocumentException {
        Paragraph title = new Paragraph("ĐƠN THUỐC", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph("Mã đơn thuốc: " + prescription.getId(), normalFont));
        doc.add(new Paragraph("Ngày tạo: " +
                prescription.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
        doc.add(Chunk.NEWLINE);
    }

    private void addDoctorInfo(Document doc, Staff doctor, Font boldFont, Font normalFont) throws DocumentException {
        doc.add(new Paragraph("Thông tin bác sĩ", boldFont));
        doc.add(new Paragraph("Họ tên: " + doctor.getName(), normalFont));
        doc.add(new Paragraph("Chức danh: " + doctor.getJobTitle(), normalFont));
        doc.add(new Paragraph("Mã khoa: " + doctor.getDepartmentId(), normalFont));
        doc.add(new Paragraph("SĐT: " + doctor.getPhoneNumber(), normalFont));
        doc.add(Chunk.NEWLINE);
    }

    private void addMedicationDetails(Document doc, List<Prescription_Medication> items, Font boldFont, Font normalFont) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.addCell(new Phrase("Tên thuốc", boldFont));
        table.addCell(new Phrase("Dạng bào chế", boldFont));
        table.addCell(new Phrase("Hàm lượng", boldFont));
        table.addCell(new Phrase("Số lượng", boldFont));
        table.addCell(new Phrase("Hãng sản xuất", boldFont));
        table.addCell(new Phrase("Ghi chú", boldFont));

        for (Prescription_Medication m : items) {
            table.addCell(m.getName());
            table.addCell(m.getDosageForm());
            table.addCell(m.getStrength());
            table.addCell(String.valueOf(m.getQuantity()));
            table.addCell(m.getManufacturer());
            table.addCell(m.getDescription());
        }

        doc.add(table);
    }

    private void addPatientInfo(Document doc, PatientGeneralDTO patient, Font boldFont, Font normalFont) throws DocumentException {
        doc.add(new Paragraph("Thông tin bệnh nhân", boldFont));
        doc.add(new Paragraph("Họ tên: " + patient.getFirstName() + " " + patient.getLastName(), normalFont));
        doc.add(new Paragraph("Ngày sinh: " + patient.getDob(), normalFont));
        doc.add(new Paragraph("Giới tính: " + patient.getSex(), normalFont));
        doc.add(new Paragraph("SĐT: " + patient.getPhoneNumber(), normalFont));
        doc.add(new Paragraph("CMND/CCCD: " + patient.getCitizenId(), normalFont));
        doc.add(new Paragraph("Mã BHYT: " + patient.getHealthInsuranceNumber(), normalFont));
        doc.add(Chunk.NEWLINE);
    }

    private void addNotes(Document doc, Font boldFont, Font normalFont) throws DocumentException {
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Ghi chú:", boldFont));
        doc.add(new Paragraph("• Uống thuốc đúng giờ, theo chỉ định của bác sĩ.", normalFont));
        doc.add(new Paragraph("• Báo ngay với bác sĩ nếu có tác dụng phụ hoặc dị ứng.", normalFont));
        doc.add(new Paragraph("• Không tự ý ngừng thuốc khi chưa có chỉ định.", normalFont));
    }

    private void addSignatureBlock(Document doc, Font boldFont, Font normalFont) throws DocumentException {
        // Line separator for signature area
        LineSeparator line = new LineSeparator();
        line.setLineWidth(0.5f);
        line.setPercentage(30);
        line.setAlignment(Element.ALIGN_RIGHT);
        doc.add(new Paragraph(" "));
        doc.add(line);

        Paragraph signature = new Paragraph("Bác sĩ kê đơn", boldFont);
        Paragraph signatureNote = new Paragraph("(Ký, ghi rõ họ tên)", normalFont);
        signatureNote.setAlignment(Element.ALIGN_RIGHT);
        signature.setAlignment(Element.ALIGN_RIGHT);
        doc.add(signature);
        doc.add(signatureNote);
    }
}
