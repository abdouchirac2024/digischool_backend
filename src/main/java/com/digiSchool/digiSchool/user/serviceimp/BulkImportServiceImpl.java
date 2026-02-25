package com.digiSchool.digiSchool.user.serviceimp;

import com.digiSchool.digiSchool.user.dto.EleveDto;
import com.digiSchool.digiSchool.user.service.BulkImportService;
import com.digiSchool.digiSchool.user.service.EleveService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class BulkImportServiceImpl implements BulkImportService {

    private final EleveService eleveService;

    public BulkImportServiceImpl(EleveService eleveService) {
        this.eleveService = eleveService;
    }

    @Override
    public List<Map<String, String>> importStudents(MultipartFile file) throws Exception {
        List<Map<String, String>> results = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // Header mapping
        Row headerRow = sheet.getRow(0);
        Map<Integer, String> columnMapping = new HashMap<>();
        for (Cell cell : headerRow) {
            columnMapping.put(cell.getColumnIndex(), cell.getStringCellValue().toLowerCase().trim());
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                continue;

            EleveDto dto = new EleveDto();
            Map<String, String> result = new HashMap<>();

            try {
                for (Map.Entry<Integer, String> entry : columnMapping.entrySet()) {
                    Cell cell = row.getCell(entry.getKey());
                    if (cell == null)
                        continue;

                    String value = getCellValueAsString(cell);
                    String field = entry.getValue();

                    switch (field) {
                        case "nom" -> dto.setNom(value);
                        case "prenom" -> dto.setPrenom(value);
                        case "sexe" -> dto.setSexe(value.toUpperCase());
                        case "date de naissance" -> dto.setDateNaissance(getCellValueAsLocalDate(cell));
                        case "lieu de naissance" -> dto.setLieuNaissance(value);
                        case "nationalite" -> dto.setNationalite(value);
                        default -> {
                        }
                    }
                }

                if (dto.getNom() != null && dto.getPrenom() != null) {
                    EleveDto created = eleveService.create(dto);
                    result.put("status", "SUCCESS");
                    result.put("matricule", created.getMatricule());
                    result.put("message", "Élève " + created.getPrenom() + " " + created.getNom() + " importé");
                } else {
                    result.put("status", "ERROR");
                    result.put("message", "Ligne " + (i + 1) + ": Nom ou prénom manquant");
                }
            } catch (Exception e) {
                result.put("status", "ERROR");
                result.put("message", "Ligne " + (i + 1) + ": " + e.getMessage());
            }
            results.add(result);
        }

        workbook.close();
        return results;
    }

    private String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                }
                yield String.valueOf((long) cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private LocalDate getCellValueAsLocalDate(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
