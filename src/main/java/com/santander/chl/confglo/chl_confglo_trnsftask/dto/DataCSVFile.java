package com.santander.chl.confglo.chl_confglo_trnsftask.dto;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",", skipFirstLine = false, generateHeaderColumns = true)
public class DataCSVFile {

	@DataField(pos = 1, columnName = "name")
	private String name;
	@DataField(pos = 2, columnName = "money")
	private String money;
	@DataField(pos = 3, columnName = "theme")
	private String theme;

	public DataCSVFile() {

	}
	
	public String getName() {
		return this.name;
	}

	public String getMoney() {
		return this.money;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
}
