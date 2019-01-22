package com.naswork.module.marketing.controller.clientinquiry;

import java.util.List;

public class Menu {

	private Integer id;
	
	private String menuName;
	
	private Integer firstMenu;
	
	private List<Menu> secondMenu;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the firstMenu
	 */
	public Integer getFirstMenu() {
		return firstMenu;
	}

	/**
	 * @param firstMenu the firstMenu to set
	 */
	public void setFirstMenu(Integer firstMenu) {
		this.firstMenu = firstMenu;
	}

	/**
	 * @return the secondMenu
	 */
	public List<Menu> getSecondMenu() {
		return secondMenu;
	}

	/**
	 * @param secondMenu the secondMenu to set
	 */
	public void setSecondMenu(List<Menu> secondMenu) {
		this.secondMenu = secondMenu;
	}
	
	
	
}
