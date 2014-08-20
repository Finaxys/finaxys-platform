package com.finaxys.finaxysplatform.dataprovidersimulator.splitter;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.Splittable;

public class GenericSplitter<T extends Splittable> {



	public  List<?> split(T t){
		
			return t.split();
	}
}
