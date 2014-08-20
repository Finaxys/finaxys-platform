/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.util.Arrays;

import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;
import com.finaxys.finaxysplatform.core.domain.Enum.DataClass;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;

// TODO: Auto-generated Javadoc
/**
 * The Class Document.
 */
public class Document implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4579790937340382141L;

	/** The content type. */
	private ContentType contentType;

	/** The data type. */
	private DataType dataType;

	/** The data class. */
	private DataClass dataClass;

	/** The source. */
	private char source;

	/** The content. */
	private byte[] content;

	/**
	 * Instantiates a new document.
	 * 
	 * @param contentType
	 *            the content type
	 * @param dataType
	 *            the data type
	 * @param dataClass
	 *            the data class
	 * @param source
	 *            the source
	 * @param content
	 *            the content
	 */

	public Document(ContentType contentType, DataType dataType, DataClass dataClass, char source, byte[] content) {
		super();
		this.contentType = contentType;
		this.dataType = dataType;
		this.dataClass = dataClass;
		this.source = source;
		setContent(content);
	}

	public Document(byte[] content) {
		super();
		setContent(content);
	}

	public Document(byte[] content, DataType dataType) {
		super();
		this.dataType = dataType;
		setContent(content);
	}

	public Document() {
		super();
	}

	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * 
	 * @param contentType
	 *            the new content type
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the data type.
	 * 
	 * @return the data type
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * Sets the data type.
	 * 
	 * @param dataType
	 *            the new data type
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * Gets the data class.
	 * 
	 * @return the data class
	 */
	public DataClass getDataClass() {
		return dataClass;
	}

	/**
	 * Sets the data class.
	 * 
	 * @param dataClass
	 *            the new data class
	 */
	public void setDataClass(DataClass dataClass) {
		this.dataClass = dataClass;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public char getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(char source) {
		this.source = source;
	}

	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            the new content
	 */
	public void setContent(byte[] content) {
		// defensive copying
		if (content == null) {
			this.content = new byte[0];
		} else {
			this.content = Arrays.copyOf(content, content.length);
		}
	}

}
