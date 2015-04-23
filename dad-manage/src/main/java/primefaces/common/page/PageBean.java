package primefaces.common.page;

import java.util.List;

public class PageBean<E> {
	
	private int totalSize;
	
	private List<E> datas;

	public List<E> getDatas() {
		return datas;
	}

	public void setDatas(List<E> datas) {
		this.datas = datas;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

}
