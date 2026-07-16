package com.pranav.departmentemployee.service.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] generateExcel(List<String> headers,
                                List<List<String>> rows) throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Audit");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();

            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.size(); i++) {

                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);

            }

            for (int i = 0; i < rows.size(); i++) {

                Row row = sheet.createRow(i + 1);

                List<String> values = rows.get(i);

                for (int j = 0; j < values.size(); j++) {

                    row.createCell(j)
                            .setCellValue(values.get(j) == null ? "" : values.get(j));

                }
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();
        }
    }
}