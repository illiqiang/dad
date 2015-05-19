package com.dad.service.dao;

import java.util.List;

import com.dad.common.entity.Pollutants;

public interface PollutantDao {
	public List<Pollutants> getPollutantsByPage(int first, int pages);
	public Pollutants getPollutantById(String dataCode);
	public int getPollutantSize();
	public void addPollutant(Pollutants d);
	public void updatePollutant(Pollutants d);
	public void deletePollutant(String dataCode);
}
