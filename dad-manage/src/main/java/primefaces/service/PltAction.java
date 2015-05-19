package primefaces.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import primefaces.common.page.PageBean;

import com.dad.common.entity.Pollutants;
import com.dad.common.service.DadService;
import com.dad.common.util.PollutantType;

@Service
public class PltAction {
	
	@Autowired
	private DadService dadService;
	
	public PageBean<Pollutants> getPltPages(int first, int pageSize,
			String dataCode) throws Exception {
		PageBean<Pollutants> pages = new PageBean<Pollutants>();
		int size = 0;
		List<Pollutants> plts = new ArrayList<>();
		if(StringUtils.isBlank(dataCode)) {
			plts = dadService.getPollutantsByPage(first, pageSize);
			if(CollectionUtils.isNotEmpty(plts)) {
				for(Pollutants p:plts){
					String typeCode = p.getType();
					for(PollutantType pt : PollutantType.values()) {
						if(pt.getCode().equals(typeCode)) {
							p.setType(pt.getName());
						}
					}
					
				}
				size = dadService.getPollutantSize();
			}
		} else {
			Pollutants d = dadService.getPollutantById(dataCode);
			if(d != null) {
				plts.add(d);
				size = 1;
			}
		}
		pages.setDatas(plts);
		pages.setTotalSize(size);
		return pages;
	}

	public void addDevice(Pollutants addPlt) throws Exception {
		dadService.addPollutant(addPlt);
	}

	public void delete(String dataCode) throws Exception {
		dadService.deletePollutant(dataCode);
	}

	public void updatePlt(Pollutants p) throws Exception {
		for(PollutantType pt : PollutantType.values()) {
			if(pt.getName().equals(p.getType())) {
				p.setType(pt.getCode());
			}
		}
		dadService.updatePollutant(p);
	}
}
