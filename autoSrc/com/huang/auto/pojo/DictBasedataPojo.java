package com.huang.auto.pojo;

import java.io.Serializable;


public class DictBasedataPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;
	private String name;
	private Integer parentId;
	private String parentIds;
	private String value;
	private Integer isLeaf;
	private String remark;
	private Integer leafLevel;
	private String dataType;
	private Integer departmentId;

	public void setId (Integer id){
		this.id = id;
	}
	public Integer getId (){
		return this.id;
	}
	public void setCode (String code){
		this.code = code;
	}
	public String getCode (){
		return this.code;
	}
	public void setName (String name){
		this.name = name;
	}
	public String getName (){
		return this.name;
	}
	public void setParentId (Integer parentId){
		this.parentId = parentId;
	}
	public Integer getParentId (){
		return this.parentId;
	}
	public void setParentIds (String parentIds){
		this.parentIds = parentIds;
	}
	public String getParentIds (){
		return this.parentIds;
	}
	public void setValue (String value){
		this.value = value;
	}
	public String getValue (){
		return this.value;
	}
	public void setIsLeaf (Integer isLeaf){
		this.isLeaf = isLeaf;
	}
	public Integer getIsLeaf (){
		return this.isLeaf;
	}
	public void setRemark (String remark){
		this.remark = remark;
	}
	public String getRemark (){
		return this.remark;
	}
	public void setLeafLevel (Integer leafLevel){
		this.leafLevel = leafLevel;
	}
	public Integer getLeafLevel (){
		return this.leafLevel;
	}
	public void setDataType (String dataType){
		this.dataType = dataType;
	}
	public String getDataType (){
		return this.dataType;
	}
	public void setDepartmentId (Integer departmentId){
		this.departmentId = departmentId;
	}
	public Integer getDepartmentId (){
		return this.departmentId;
	}

}