package com.app.bean;

public class ProductCategoryBean {
	private Long id;
	private String productCategoryName;
	private String productCategoryDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductCategoryName() {
		return productCategoryName;
	}
	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}
	public String getProductCategoryDesc() {
		return productCategoryDesc;
	}
	public void setProductCategoryDesc(String productCategoryDesc) {
		this.productCategoryDesc = productCategoryDesc;
	}

}
