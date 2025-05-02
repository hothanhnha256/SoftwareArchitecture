package com.softwareA.patient.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.softwareA.patient.dto.Staff;
import com.softwareA.patient.dto.response.MedicalOrderResponse;
import com.softwareA.patient.dto.response.PatientGeneralDTO;
import com.softwareA.patient.model.medical_order.MedicalOrder_OrderItem;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class MedicalOrderPDFPrinter implements MedicalOrderPrinter {
    @Override
    public byte[] print(MedicalOrderResponse order) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            BaseFont baseFont = loadBaseFont();
            Font titleFont = new Font(baseFont, 16, Font.BOLD);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);

            addHeader(doc, order, titleFont, normalFont);
            addPatientInfo(doc, order.getPatient(), boldFont, normalFont);
            addDoctorInfo(doc, order.getDoctor(), boldFont, normalFont);
            addMedicalOrderItems(doc, order.getMedicalOrderItems(), boldFont, normalFont);

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

    private void addHeader(Document doc, MedicalOrderResponse order, Font titleFont, Font normalFont) throws DocumentException {
        Paragraph title = new Paragraph("PHIẾU CHỈ ĐỊNH Y TẾ", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph("Mã phiếu: " + order.getId(), normalFont));
        doc.add(new Paragraph("Ngày tạo: " +
                order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
        doc.add(Chunk.NEWLINE);
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

    private void addDoctorInfo(Document doc, Staff doctor, Font boldFont, Font normalFont) throws DocumentException {
        doc.add(new Paragraph("Thông tin bác sĩ", boldFont));
        doc.add(new Paragraph("Họ tên: " + doctor.getName(), normalFont));
        doc.add(new Paragraph("Chức danh: " + doctor.getJobTitle(), normalFont));
        doc.add(new Paragraph("Mã khoa: " + doctor.getDepartmentId(), normalFont));
        doc.add(new Paragraph("SĐT: " + doctor.getPhoneNumber(), normalFont));
        doc.add(Chunk.NEWLINE);
    }

    private void addMedicalOrderItems(Document doc, List<MedicalOrder_OrderItem> items, Font boldFont, Font normalFont) throws DocumentException {
        doc.add(new Paragraph("Danh sách chỉ định", boldFont));
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 5, 1.5f, 2});

        addTableHeaders(table, boldFont);
        addTableRows(table, items, normalFont);

        doc.add(table);
    }

    private void addTableHeaders(PdfPTable table, Font boldFont) {
        String[] headers = {"STT", "Mã", "Tên chỉ định", "Số lượng", "Giá (VNĐ)"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, boldFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    private void addTableRows(PdfPTable table, List<MedicalOrder_OrderItem> items, Font normalFont) {
        int index = 1;
        for (MedicalOrder_OrderItem item : items) {
            table.addCell(new Phrase(String.valueOf(index++), normalFont));
            table.addCell(new Phrase(item.getMedicalOrderItem().getCode(), normalFont));
            table.addCell(new Phrase(item.getMedicalOrderItem().getName() + "\n" + item.getMedicalOrderItem().getDescription(), normalFont));
            table.addCell(new Phrase(String.valueOf(item.getQuantity()), normalFont));
            table.addCell(new Phrase(String.format("%,.0f", item.getMedicalOrderItem().getPrice()), normalFont));
        }
    }
}
