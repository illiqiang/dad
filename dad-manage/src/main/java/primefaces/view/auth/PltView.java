package primefaces.view.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import primefaces.common.page.PageBean;
import primefaces.common.page.PageDataModel;
import primefaces.common.page.Sort;
import primefaces.service.PltAction;

import com.dad.common.entity.Pollutants;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.util.PollutantType;

@ManagedBean
@ViewScoped
public class PltView {

private final static Logger log = LoggerFactory.getLogger(PltView.class);
	
	@ManagedProperty(value = "#{pltAction}")
	private PltAction pltAction;
	
	private LazyDataModel<Pollutants> pltPages;
	
	private Pollutants selectPlt;
	
	private Pollutants addPlt = new Pollutants();
	
	private List<PollutantType> pltTypes;
	
	@PostConstruct
	public void init() {
		
		pltTypes = new ArrayList<>();
		pltTypes.addAll(Arrays.asList(PollutantType.values()));
		
		pltPages = new PageDataModel<Pollutants>() {

			private static final long serialVersionUID = 4497840920037869584L;

			@Override
			public Object getRowKey(Pollutants object) {
				return object.getDataCode();
			}

			@Override
			public PageBean<Pollutants> getPageResult(int first, int pageSize,
					List<Sort> multiSortMeta, Map<String, Object> filters) {
				String dataCode = (String) filters.get("dataCode");
				PageBean<Pollutants> plts = new PageBean<Pollutants>();
				try {
					plts = pltAction.getPltPages(first, pageSize, dataCode);
					
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return plts;
			}
		};
	}
	
	public void initAddPlt() {
		addPlt = new Pollutants();
	}
	
	public void addPlt() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean isadd = false;
		if (StringUtils.isNotBlank(addPlt.getDataCode())&&StringUtils.isNotBlank(addPlt.getDataName())&&StringUtils.isNotBlank(addPlt.getType())) {
			try {
				pltAction.addDevice(addPlt);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "新增成功",
						String.format("污染物[%s] 已经成功添加", addPlt.getDataName()));
				isadd = true;
			} catch (BusinessServiceException e) {
				isadd = false;
				message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"新增失败", String.format("污染物[%s]已经存在。", addPlt.getDataCode()));
			} catch (Exception e) {
				isadd = false;
				log.error(e.getMessage(), e);
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"新增失败", "服务器出现了异常，本次操作失败了。");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		} else {
			isadd = false;
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "新增失败",
					"代码、名称和类型不能为空");
		}

		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("isadd", isadd);
	}

	public void deletePlt() {
		FacesMessage message = null;
		try {
				pltAction.delete(selectPlt.getDataCode());	
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "删除成功",
						String.format("污染物[%s] 已被成功删除",
								selectPlt.getDataName()));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败",
					"服务器出现了异常，本次操作失败了。");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = null;
		Pollutants p = (Pollutants) event.getObject();
		if(StringUtils.isBlank(p.getDataCode()) && StringUtils.isNotBlank(p.getDataName())&& StringUtils.isNotBlank(p.getType())) {
			msg = new FacesMessage("修改失败", "代码、名称和类型不可以为空");
		} else {
			try {
				pltAction.updatePlt(p);
				msg = new FacesMessage("修改成功", String.format("成功修改了污染物[%s] 的信息", p.getDataName()));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"修改失败", "服务器出现了异常，本次操作失败了。");
			} 
		}
       
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

	public Pollutants getSelectPlt() {
		return selectPlt;
	}

	public void setSelectPlt(Pollutants selectPlt) {
		this.selectPlt = selectPlt;
	}

	public Pollutants getAddPlt() {
		return addPlt;
	}

	public void setAddPlt(Pollutants addPlt) {
		this.addPlt = addPlt;
	}

	public LazyDataModel<Pollutants> getPltPages() {
		return pltPages;
	}

	public void setPltAction(PltAction pltAction) {
		this.pltAction = pltAction;
	}

	public List<PollutantType> getPltTypes() {
		return pltTypes;
	}
	
}
