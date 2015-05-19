package com.dad.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.dad.common.entity.Pollutants;

public class PollutantDaoImpl implements PollutantDao {
	
	private JdbcTemplate baseTemplate;

	public void setBaseTemplate(JdbcTemplate baseTemplate) {
		this.baseTemplate = baseTemplate;
	}
	
	private final static String getPollutantByIdSql = "select data_code,data_name,type,data_unit,cou_unit from tb_pollutants where data_code=?";

	@Override
	public Pollutants getPollutantById(String dataCode) {
		return baseTemplate.query(getPollutantByIdSql, new Object[]{dataCode},new ResultSetExtractor<Pollutants>() {

			@Override
			public Pollutants extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Pollutants p = null;
				if (rs.next()) {
					p = new Pollutants();
					p.setDataCode(rs.getString(1));
					p.setDataName(rs.getString(2));
					p.setType(rs.getString(3));
					p.setDataUnit(rs.getString(4));
					p.setCouUnit(rs.getString(5));
				}
				return p;
			}});
	}

	private final static String getPollutantSizeSql = "select count(*) from tb_pollutants";
	@Override
	public int getPollutantSize() {
		return baseTemplate.queryForObject(getPollutantSizeSql, Integer.class);
	}

	private final static String addPollutantSql = "insert into tb_pollutants(data_code,data_name,type,data_unit,cou_unit) values(?,?,?,?)";
	@Override
	public void addPollutant(Pollutants p) {
		baseTemplate.update(addPollutantSql, new Object[]{p.getDataCode(), p.getDataName(), p.getType(), p.getDataUnit(),p.getCouUnit()});
	}

	private final static String updatePollutantSql = "update tb_pollutants set data_name=?, type=?, data_unit=?,cou_unit=? where data_code=?";
	@Override
	public void updatePollutant(Pollutants p) {
		baseTemplate.update(updatePollutantSql, new Object[]{ p.getDataName(), p.getType(), p.getDataUnit(), p.getCouUnit(),p.getDataCode()});
	}
	
	private final static String deletePollutantSql = "delete from tb_pollutants where data_code=?";
	@Override
	public void deletePollutant(String dataCode) {
		baseTemplate.update(deletePollutantSql, new Object[]{dataCode});
	}
	
	private final static String getPollutantsByPageSql = "select data_code,data_name,type,data_unit,cou_unit from tb_pollutants limit ?,?";
	@Override
	public List<Pollutants> getPollutantsByPage(int first, int pages) {
		return baseTemplate.query(getPollutantsByPageSql, new Object[]{first, pages}, new RowMapper<Pollutants>(){

			@Override
			public Pollutants mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Pollutants p = new Pollutants();
				p.setDataCode(rs.getString(1));
				p.setDataName(rs.getString(2));
				p.setType(rs.getString(3));
				p.setDataUnit(rs.getString(4));
				p.setCouUnit(rs.getString(5));
				return p;
			}});
	}

}
