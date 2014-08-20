/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


// TODO: Auto-generated Javadoc
/**
 * The Class ExtractedDatas.
 */
@XmlRootElement(name = "datalist")
public class DataWrapper<T extends ExtractedData> implements Serializable, Splittable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2424471412213673154L;
	private List<T> list;

	public DataWrapper() {
		super();
	}

	public DataWrapper(List<T> list) {
		super();
		this.list = list;
	}

	/**
	 * Gets the currency pairs list.
	 *
	 * @return the currency pairs list
	 */
	@XmlElementWrapper(name = "list")
	@XmlElement(name="data")
	public List<T> getList() {
		if (list == null)
			list = new ArrayList<T>();
		return list;
	}

	/**
	 * Sets the currency pairs list.
	 *
	 * @param list
	 *            the new currency pairs list
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public List<DataWrapper<T>> split(int n) {
		List<DataWrapper<T>> result = new ArrayList<DataWrapper<T>>();
		int x = this.list.size() / n;
		int y = this.list.size() % n;
		for (int i = 0; i < x; i++) {

			result.add(new DataWrapper<T>(this.list.subList((i * n),
					((i + 1) * n))));
		}

		result.add(new DataWrapper<T>(this.list.subList((x * n), (x * n) + y)));
		
		return result;
	}
	
	@Override
	public List<T> split() {
		return list; 
	}
}