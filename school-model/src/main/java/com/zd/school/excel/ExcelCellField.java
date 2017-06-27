package com.zd.school.excel;

public class ExcelCellField {
	

	/**
	 * 在excel中列的顺序，从小到大排
	 *
	 * @return 顺序
	 */
	private int id;

	/**
	 * 在excel文件中某列数据的名称
	 *
	 * @return 名称
	 */
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}




}
