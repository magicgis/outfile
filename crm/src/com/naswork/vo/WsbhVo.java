/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;

/**
 * 生成的文书号Vo
 * @since 2016年5月5日 上午9:56:48
 * @author xiaohu
 * @version v1.0
 */
public class WsbhVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4135694018726647315L;
	
	private String wsbhUuid;
	//局轨
	private String jg;
	//字轨
	private String zg;
	//年度
	private String nd;
	//前缀
	private String qz;
	//文书号
	private String wsh;
	
	public String toString(){
		return getQz() == null ? getNd()+getWsh() : getQz() +getNd()+getWsh();
	}
	
	public String getJg() {
		return jg;
	}
	public void setJg(String jg) {
		this.jg = jg;
	}
	public String getZg() {
		return zg;
	}
	public void setZg(String zg) {
		this.zg = zg;
	}
	public String getNd() {
		return nd;
	}
	public void setNd(String nd) {
		this.nd = nd;
	}
	public String getWsh() {
		return wsh;
	}
	public void setWsh(String wsh) {
		this.wsh = wsh;
	}
	public String getWsbhUuid() {
		return wsbhUuid;
	}
	public void setWsbhUuid(String wsbhUuid) {
		this.wsbhUuid = wsbhUuid;
	}
	public String getQz() {
		return qz;
	}
	public void setQz(String qz) {
		this.qz = qz;
	}
	
	

}
