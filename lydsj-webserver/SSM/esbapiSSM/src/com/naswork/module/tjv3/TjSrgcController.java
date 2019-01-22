package com.naswork.module.tjv3;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.service.TjSrgcfxService;

/**
 * 
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 涉旅企业收入构成
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午3:00:40
 *
 */
@RestController
@RequestMapping("/v3")
public class TjSrgcController {
	@Autowired
	TjSrgcfxService tjSrgcfxService;

	@RequestMapping("/srgcfx/{year}/{level}")
	public Map<String, Object> getOneCNYmlnYearly(@PathVariable Integer year, @PathVariable Integer level) {
		return tjSrgcfxService.getOneCNYmlnYearly(year, level);
	}

}
