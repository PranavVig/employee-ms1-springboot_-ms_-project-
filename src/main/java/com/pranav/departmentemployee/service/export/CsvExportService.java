package com.pranav.departmentemployee.service.export;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvExportService {

    public byte[] generateCsv(List<String> headers,
                              List<List<String>> rows) {

        StringBuilder csv = new StringBuilder();

        csv.append(String.join(",", headers));
        csv.append("\n");

        for (List<String> row : rows) {

            List<String> escapedRow = row.stream()
                    .map(this::escape)
                    .toList();

            csv.append(String.join(",", escapedRow));
            csv.append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");

        return "\"" + escaped + "\"";
    }
}