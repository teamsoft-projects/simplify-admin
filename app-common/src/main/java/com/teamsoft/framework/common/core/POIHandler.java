package com.teamsoft.framework.common.core;

import com.teamsoft.framework.common.annotation.ExcelColumn;
import com.teamsoft.framework.common.model.ExcelField;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * EXCEL读写实体类
 * @author zhangcc
 * @version 2017/9/16
 */
@Data
public class POIHandler {
	// 默认读取sheet索引
	private Integer defaultSheet = 0;
	// 开启读取行
	private Integer startRow = 1;
	// Workbook对象
	private Workbook wb;
	// 写入路径
	private String writePath;

	/**
	 * 根据文件构建Excel处理对象
	 * @param sourceFile 源文件
	 */
	public static POIHandler build(File sourceFile) throws Exception {
		POIHandler handler = new POIHandler();
		handler.wb = WorkbookFactory.create(Files.newInputStream(sourceFile.toPath()));
		return handler;
	}

	/**
	 * 根据流构建Excel处理对象
	 * @param is 输入流
	 */
	public static POIHandler build(InputStream is) throws Exception {
		POIHandler handler = new POIHandler();
		handler.wb = WorkbookFactory.create(is);
		return handler;
	}

	/**
	 * 构建一个写入型的处理对象
	 * @param writePath 写入路径
	 */
	public static POIHandler build(String writePath) {
		POIHandler handler = new POIHandler();
		handler.wb = new XSSFWorkbook();
		handler.writePath = writePath;
		return handler;
	}

	/**
	 * 构建一个写入型的处理对象
	 */
	public static POIHandler build() {
		POIHandler handler = new POIHandler();
		handler.wb = new XSSFWorkbook();
		return handler;
	}

	/**
	 * 获取单元格数据
	 * @param cell 数据单元格
	 * @return 结果值
	 */
	public Object getCellValue(Cell cell) {
		Object retVal;
		if (cell == null) {
			return null;
		}
		//判断数据的类型
		switch (cell.getCellType()) {
			case NUMERIC: //数字
				retVal = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
				break;
			case STRING: //字符串
				retVal = cell.getStringCellValue();
				break;
			case BOOLEAN: //Boolean
				retVal = cell.getBooleanCellValue();
				break;
			case FORMULA: //公式
				retVal = cell.getCellFormula();
				break;
			case BLANK: //空值
				retVal = "";
				break;
			case ERROR: //故障
				retVal = "非法字符";
				break;
			default:
				retVal = "未知类型";
				break;
		}
		return retVal;
	}

	/**
	 * 根据模型类定义, 获取field列表
	 */
	private List<ExcelField> generateExcelField(Class<?> modelClass) {
		List<ExcelField> result = new ArrayList<>();
		Field[] fs = modelClass.getDeclaredFields();
		for (Field f : fs) {
			ExcelField field = new ExcelField();
			ExcelColumn col = f.getAnnotation(ExcelColumn.class);
			if (col == null) {
				continue;
			}
			field.setField(f);
			field.setIndex(col.value());
			field.setType(f.getGenericType());
			result.add(field);
		}
		return result;
	}

	/**
	 * 读取Excel文档
	 * @param modelClass 模型类
	 */
	public <T> List<T> read(Class<T> modelClass) throws Exception {
		Sheet sheet = wb.getSheetAt(defaultSheet);
		int lastRowNum = sheet.getLastRowNum();
		if (startRow > lastRowNum) {
			return null;
		}
		List<ExcelField> fields = generateExcelField(modelClass);
		if (fields.isEmpty()) {
			return null;
		}
		List<T> retList = new ArrayList<>();
		for (int i = startRow; i <= lastRowNum; i++) {
			Row row = sheet.getRow(i);
			short lastCellNum = row.getLastCellNum();
			T obj = modelClass.newInstance();
			retList.add(obj);
			for (ExcelField field : fields) {
				Field f = field.getField();
				int index = field.getIndex();
				if (index < 0 || index > lastCellNum) {
					continue;
				}
				f.setAccessible(true);
				Type type = field.getType();
				Cell cell = row.getCell(index);
				Object cellVal = getCellValue(cell);
				switch (type.getTypeName()) {
					case "java.lang.Integer": {
						if (cellVal == null) {
							f.set(obj, null);
							break;
						} else if (cellVal instanceof Integer) {
							f.set(obj, cellVal);
						} else if (cellVal instanceof Double) {
							Double cellValDbl = (Double) cellVal;
							f.set(obj, cellValDbl.intValue());
						} else if (cellVal instanceof String) {
							if ("".equals(cellVal)) {
								f.set(obj, null);
								break;
							}
							try {
								double cellValDbl = Double.parseDouble(cellVal.toString());
								f.set(obj, (int) cellValDbl);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						break;
					}
					case "java.lang.String": {
						if (cellVal == null) {
							f.set(obj, null);
							break;
						}
						String cellValStr = cellVal.toString();
						if (!StringUtils.hasLength(cellValStr)) {
							f.set(obj, null);
						} else {
							f.set(obj, cellValStr);
						}
						break;
					}
				}
			}
		}
		wb.close();
		return retList;
	}

	/**
	 * 将List对象写入Excel文件
	 */
	public byte[] writeToBytes(String[] header, List<?> data, Class<?> modelClass) throws Exception {
		return this.write(header, data, modelClass, true);
	}

	/**
	 * 将List对象写入Excel文件
	 */
	public void write(String[] header, List<?> data, Class<?> modelClass) throws Exception {
		this.write(header, data, modelClass, false);
	}

	/**
	 * 将List对象写入Excel文件
	 */
	private byte[] write(String[] header, List<?> data, Class<?> modelClass, boolean isBytes) throws Exception {
		Sheet sheet = wb.createSheet();
		if (header != null && header.length > 0) {
			// 写入头部分
			Row headRow = sheet.createRow(0);
			for (int i = 0; i < header.length; i++) {
				headRow.createCell(i).setCellValue(header[i]);
			}
		}
		List<ExcelField> fields = generateExcelField(modelClass);
		if (!fields.isEmpty()) {
			// 写入内容部分
			for (int i = startRow; i < data.size() + startRow; i++) {
				Object dataObj = data.get(i - startRow);
				Row row = sheet.createRow(i);
				for (ExcelField field : fields) {
					Cell cell = row.createCell(field.getIndex());
					Field f = field.getField();
					f.setAccessible(true);
					Object cellVal = f.get(dataObj);
					cell.setCellValue(String.valueOf(cellVal));
				}
			}
		}
		if (!isBytes) {
			wb.write(Files.newOutputStream(Paths.get(writePath)));
			wb.close();
			return null;
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
		wb.write(bos);
		wb.close();
		return bos.toByteArray();
	}
}