package primefaces.common.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public abstract class PageDataModel<T> extends LazyDataModel<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6534446786252735936L;

	@Override
	public List<T> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Sort> sorts = null;
		if (StringUtils.isNotBlank(sortField) && sortOrder != null
				&& !SortOrder.UNSORTED.equals(sortOrder)) {
			sorts = new ArrayList<Sort>();
			Order o = null;
			if (SortOrder.DESCENDING.equals(sortOrder)) {
				o = Order.DESC;
			} else {
				o = Order.ASC;
			}
			sorts.add(new Sort(sortField, o));
		}

		PageBean<T> beans = getPageResult(first, pageSize, sorts, filters);
		this.setRowCount(beans.getTotalSize());
		return beans.getDatas();
	}

	@Override
	public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters) {
		List<Sort> sorts = null;
		if (CollectionUtils.isNotEmpty(multiSortMeta)) {
			sorts = new ArrayList<Sort>();
			for (SortMeta sm : multiSortMeta) {
				SortOrder sortOrder = sm.getSortOrder();
				String sortField = sm.getSortField();
				if (StringUtils.isNotBlank(sortField) && sortOrder != null
						&& !SortOrder.UNSORTED.equals(sortOrder)) {
					if (sorts == null) {
						sorts = new ArrayList<Sort>();
					}

					Order o = null;
					if (SortOrder.DESCENDING.equals(sortOrder)) {
						o = Order.DESC;
					} else {
						o = Order.ASC;
					}
					sorts.add(new Sort(sortField, o));
				}
			}
		}
		PageBean<T> beans = getPageResult(first, pageSize, sorts, filters);
		this.setRowCount(beans.getTotalSize());
		return beans.getDatas();
	}

	@Override
	public T getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<T> dataList = (List<T>) getWrappedData();
		for (T entity : dataList) {
			if (getRowKey(entity).toString().equals(rowKey)) {
				return entity;
			}
		}
		return null;
	}

	@Override
	public abstract Object getRowKey(T object);

	public abstract PageBean<T> getPageResult(int first, int pageSize,
			List<Sort> multiSortMeta, Map<String, Object> filters);
}
