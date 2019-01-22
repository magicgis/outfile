package com.asiainfo.httpsws.eaopbase;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Java class for handle complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="handle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlRootElement(name = "handle2")
public class Handle2 {
	protected String arg0;
	protected String sign;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Handle2(){}
	
	public Handle2(String msg,String sign){
		arg0 = msg;
		sign=sign;
	}

	/**
	 * Gets the value of the arg0 property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 */
	public String getArg0() {
		return arg0;
	}

	/**
	 * Sets the value of the arg0 property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 */
	public void setArg0(String value) {
		this.arg0 = value;
	}
}
