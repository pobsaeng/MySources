package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.app.bean.ProductCategoryBean;
import com.app.util.DBUtil;

public class ProductCategoryDao {

	public List<ProductCategoryBean> getProductCategory(int offset,
			int noOfRecords) {
		System.out.println(offset+","+noOfRecords);
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "select   * from inv_product_category limit " + offset
				+ ", " + noOfRecords;
		List<ProductCategoryBean> productCategories = new ArrayList<ProductCategoryBean>();
		try {
			connection = DBUtil.getConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					ProductCategoryBean productCategory = new ProductCategoryBean();
					productCategory.setId(resultSet.getLong("id"));
					productCategory.setProductCategoryName(resultSet
							.getString("product_category_name"));
					productCategory.setProductCategoryDesc(resultSet
							.getString("product_category_desc"));
					productCategories.add(productCategory);
				}
			}
		} catch (Exception e) {
			productCategories = null;
		} finally {
			DBUtil.closeResource(connection, null, resultSet);
		}
		return productCategories;
	}

	public int getTotalRows() {

		int totalRows = 0;
		String query = "select count(*) as cnt from inv_product_category";
		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DBUtil.getConnection();
			if (!"".equals(query)) {
				if (connection != null) {
					preparedStatement = connection.prepareStatement(query);
					resultSet = preparedStatement.executeQuery();
					if (resultSet.next()) {
						totalRows = resultSet.getInt("cnt");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			totalRows = 0;
		} finally {
			DBUtil.closeResource(connection, preparedStatement, resultSet);
		}
		return totalRows;

	}

}