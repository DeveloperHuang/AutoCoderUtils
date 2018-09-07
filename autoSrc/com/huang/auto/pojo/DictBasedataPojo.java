package com.huang.auto.pojo;

import java.io.Serializable;

/**
 * 数据字典表
 */
public class DictBasedataPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer dataItemId;
	private String dataItemNum;
	private String dataItemName;
	private Integer fatherItemId;
	private String dataItemValue;
	private Integer isLeaf;
	private String remark;
	private Integer leafLevel;
	private Integer sort;
	private String dataType;
	private Integer departmentId;

	public Integer getDataItemId() {
		return dataItemId;
	}

	public void setDataItemId(Integer dataItemId) {
		this.dataItemId = dataItemId;
	}

	public String getDataItemNum() {
		return dataItemNum;
	}

	public void setDataItemNum(String dataItemNum) {
		this.dataItemNum = dataItemNum;
	}

	public String getDataItemName() {
		return dataItemName;
	}

	public void setDataItemName(String dataItemName) {
		this.dataItemName = dataItemName;
	}

	public Integer getFatherItemId() {
		return fatherItemId;
	}

	public void setFatherItemId(Integer fatherItemId) {
		this.fatherItemId = fatherItemId;
	}

	public String getDataItemValue() {
		return dataItemValue;
	}

	public void setDataItemValue(String dataItemValue) {
		this.dataItemValue = dataItemValue;
	}

	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getLeafLevel() {
		return leafLevel;
	}

	public void setLeafLevel(Integer leafLevel) {
		this.leafLevel = leafLevel;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
}