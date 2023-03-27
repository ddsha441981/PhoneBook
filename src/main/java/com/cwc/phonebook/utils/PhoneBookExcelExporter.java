package com.cwc.phonebook.utils;

import com.cwc.phonebook.model.PhoneBook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PhoneBookExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<PhoneBook> phoneBookList;

    public PhoneBookExcelExporter(List<PhoneBook> phoneBookList) {
        this.phoneBookList = phoneBookList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("PhoneBook");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();

        //set color of heading background
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        //Set Color of heading
        font.setColor(IndexedColors.BLACK1.getIndex());
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Phone ID", style);
        createCell(row, 1, "First Name", style);
        createCell(row, 2, "Last Name", style);
        createCell(row, 3, "Mobile", style);
        createCell(row, 4, "Enabled", style);
    }
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (PhoneBook phoneBook : phoneBookList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, phoneBook.getPhoneBookId(), style);
            createCell(row, columnCount++, phoneBook.getFirstname(), style);
            createCell(row, columnCount++, phoneBook.getLastname(), style);
            createCell(row, columnCount++, phoneBook.getMobile(), style);
            createCell(row, columnCount++, phoneBook.isEnabled(), style);
        }
    }
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
