package com.finaxys.rd.dataextraction.test.converter.helper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class TestHelper {

	
	public static byte[] getResourceAsBytes(String path) throws IOException{
		InputStream in = TestHelper.class.getResourceAsStream(path);
		return IOUtils.toByteArray(in);
	}
	
}
