package primefaces.common.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.dad.common.util.PollutantType;

/**
 * 点播排行页面：显示类型装换器
 * 
 * @author 907720
 * 
 */
@FacesConverter("pollutantTypeConverter")
public class PollutantTypeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		for(PollutantType p : PollutantType.values()) {
			if(p.getCode().equals(value)){
				return p;
			}
		}
		return null;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value instanceof PollutantType) {
			return ((PollutantType) value).getCode();
		}
		return value.toString();
	}

}
