package com.redhat.camel.route.coverage.process;

import com.redhat.camel.route.coverage.model.EipStatistic;
import com.redhat.camel.route.coverage.model.RouteStatistic;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelWriter {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelWriter.class);

    private final Collection<RouteStatistic> routeStatistics;

    private final XSSFWorkbook workbook;

    private final String outputPath;

    private final CellStyle boldStyle;

    private final CellStyle plainStyle;

    public ExcelWriter(Collection<RouteStatistic> routeStatistics, String outputPath) {

        this.routeStatistics = routeStatistics;
        this.outputPath = outputPath;

        workbook = new XSSFWorkbook();

        boldStyle = workbook.createCellStyle();
        XSSFFont boldFont = workbook.createFont();

        boldFont.setBold(true);
        boldFont.setFontHeight(16);
        boldStyle.setFont(boldFont);

        plainStyle = workbook.createCellStyle();
        XSSFFont plainFont = workbook.createFont();

        plainFont.setFontHeight(12);
        plainStyle.setFont(plainFont);
    }

    public void write() throws IOException {

        writeIndex();

        for (RouteStatistic routeStatistic : routeStatistics) {
            writeDetail(routeStatistic);
        }

        FileOutputStream outputStream = new FileOutputStream(outputPath + "/index.xlsx");
        workbook.write(outputStream);
    }

    protected void writeIndex() {

        String safeName = WorkbookUtil.createSafeSheetName("index");
        XSSFSheet sheet = workbook.createSheet(safeName);

        writeIndexHeader(sheet);
        writeIndexData(sheet);
    }

    protected void writeIndexHeader(XSSFSheet sheet) {

        Row row = sheet.createRow(0);
        int colIndex = 0;

        createCell(sheet, row, colIndex++, "Route", boldStyle);
        createCell(sheet, row, colIndex++, "Total EIPs", boldStyle);
        createCell(sheet, row, colIndex++, "Tested", boldStyle);
        createCell(sheet, row, colIndex++, "Coverage %", boldStyle);
        createCell(sheet, row, colIndex, "Time (ms)", boldStyle);
    }

    protected void writeIndexData(XSSFSheet sheet) {

        int rowIndex = 1;

        for (RouteStatistic routeStatistic : routeStatistics) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;

            createCell(sheet, row, colIndex++, routeStatistic.getId(), plainStyle);
            createCell(sheet, row, colIndex++, routeStatistic.getTotalEips(), plainStyle);
            createCell(sheet, row, colIndex++, routeStatistic.getTotalEipsTested(), plainStyle);
            createCell(sheet, row, colIndex++, routeStatistic.getCoverage(), plainStyle);
            createCell(sheet, row, colIndex, routeStatistic.getTotalProcessingTime(), plainStyle);
        }
    }

    protected void writeDetail(RouteStatistic routeStatistic) {

        // sheet names cannot exceed 31 characters, but many routes have similar names
        // try to create a unique sheet name for the route if a duplicate is detected
        String safeName = WorkbookUtil.createSafeSheetName(routeStatistic.getId());
        XSSFSheet sheet;
        try {
            sheet = workbook.createSheet(safeName);
        } catch (IllegalArgumentException e) {
            String abbreviation = "-" + RandomStringUtils.random(3, true, true) + "-";
            safeName = StringUtils.abbreviateMiddle(safeName, abbreviation, safeName.length() - 1);
            sheet = workbook.createSheet(safeName);
        }

        writeDetailHeader(sheet, routeStatistic);
        writeDetailData(sheet, routeStatistic);
    }

    protected void writeDetailHeader(XSSFSheet sheet, RouteStatistic routeStatistic) {

        Row row = sheet.createRow(0);
        createCell(sheet, row, 4, routeStatistic.getId(), boldStyle);

        row = sheet.createRow(1);
        int colIndex = 0;

        createCell(sheet, row, colIndex++, "Index", boldStyle);
        createCell(sheet, row, colIndex++, "EIP", boldStyle);
        createCell(sheet, row, colIndex++, "Tested", boldStyle);
        createCell(sheet, row, colIndex++, "Time (ms)", boldStyle);
        createCell(sheet, row, colIndex, "Properties", boldStyle);
    }

    protected void writeDetailData(XSSFSheet sheet, RouteStatistic routeStatistic) {

        Set<Map.Entry<Integer, List<EipStatistic>>> eips = routeStatistic.getEipStatisticMap().entrySet();

        int startRow = sheet.getLastRowNum() + 1;

        for (Map.Entry<Integer, List<EipStatistic>> entry : eips) {

            int index = entry.getKey();

            for (EipStatistic eip : entry.getValue()) {

                int lastRow = sheet.getLastRowNum();

                // insert the first row
                if (lastRow < startRow) {
                    writeDetailRow(sheet, startRow, index, eip);
                } else {
                    // look for the insertion point
                    int rowNumber;
                    for (rowNumber = startRow; rowNumber <= lastRow; rowNumber++) {
                        Row row = sheet.getRow(rowNumber);

                        double dblIndex = row.getCell(0).getNumericCellValue();
                        int currIndex = (int) dblIndex;
                        if (currIndex >= index) {
                            sheet.shiftRows(rowNumber, lastRow, 1);
                            break;
                        }
                    }
                    writeDetailRow(sheet, rowNumber, index, eip);
                }
            }
        }
    }

    protected void writeDetailRow(XSSFSheet sheet, int rowNumber, Integer key, EipStatistic eip) {

        int colIndex = 0;
        Row row = sheet.createRow(rowNumber);

        createCell(sheet, row, colIndex++, key, plainStyle);
        createCell(sheet, row, colIndex++, eip.getId(), plainStyle);
        createCell(sheet, row, colIndex++, eip.isTested(), plainStyle);
        createCell(sheet, row, colIndex++, eip.getTotalProcessingTime(), plainStyle);
        createCell(sheet, row, colIndex, eip.getProperties(), plainStyle);
    }

    protected void createCell(XSSFSheet sheet, Row row, int columnCount, Object value, CellStyle style) {

        sheet.autoSizeColumn(columnCount);

        Cell cell = row.createCell(columnCount);
        cell.setCellStyle(style);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
