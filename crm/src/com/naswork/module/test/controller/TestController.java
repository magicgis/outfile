package com.naswork.module.test.controller;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.module.marketing.controller.clientinquiry.Menu;
import com.naswork.vo.ResultVo;

@Controller
@RequestMapping(value="/test/wx")
public class TestController extends BaseController {

	
	@RequestMapping(value="/getMenu",method=RequestMethod.POST)
	public @ResponseBody ResultVo getMenu(){
		List<Menu> list = new ArrayList<Menu>();
		for (int i = 0; i < 5; i++) {
			Menu firstMenu = new Menu();
			firstMenu.setId(i+1);
			firstMenu.setMenuName("分类-"+(i+1));
			List<Menu> seconMenus = new ArrayList<Menu>();
			for (int j = 0; j < 10; j++) {
				Menu seconMenu = new Menu();
				seconMenu.setId(j+10);
				seconMenu.setMenuName("商品-"+(j+1));
				seconMenu.setFirstMenu(i+1);
				seconMenus.add(seconMenu);
			}
			firstMenu.setSecondMenu(seconMenus);
			list.add(firstMenu);
		}
		JSONArray json = new JSONArray();
		json.add(list);
		return new ResultVo(true, json.toString());
	}
	
}
