package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjnewjdrcMonthly;
import com.naswork.service.TjnewjdrcMonthlyService;
/**
 * 全市/本县区/景区/全市3A景区每月接待人次
 * @author zhangwenwen
 *
 */

@RestController
@RequestMapping("/v2/myjdrc")
public class TjnewjdrcMonthlyController extends BaseController {
	@Resource
	private TjnewjdrcMonthlyService tjnewjdrcMonthlyService;
	@RequestMapping("/{year_id}/{id_type}/{id}/{displayOption}")
	public HashMap<String, Object> getQsmyjdrc(@PathVariable Integer year_id,@PathVariable Integer id_type,
			@PathVariable Integer id,@PathVariable Integer displayOption){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<TjnewjdrcMonthly> list=new ArrayList<TjnewjdrcMonthly>();
		List<String> dateList=new ArrayList<String>();
		List<Integer> rcList=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
		if (id_type==1||id_type==2||id_type==3) {
			 list=tjnewjdrcMonthlyService.getTjnewjdrcMonthly(year_id,id);/**查询全市/本县区/景区每月接待人次  **/
			 if (displayOption==1) {
					for(int i=0;i<list.size();i++){
						dateList.add(list.get(i).getRecordmonth()+"月");
						rcList.add(list.get(i).getValue());
					}
			}else if(displayOption==2) {
				for(int i=0;i<list.size();i++){
					dateList.add(list.get(i).getRecordyear()+"年"+list.get(i).getRecordmonth()+"月");
					rcList.add(list.get(i).getValue());
				}
			}

				list3.add(rcList);
		}else if(id_type==4) {    															/**全市3A景区每月接待人次 **/
			list=tjnewjdrcMonthlyService.getQs3ajdrcMonthly(year_id, id_type-1);
			if (displayOption==1) {
				for(int i=0;i<list.size();i++){
					dateList.add(list.get(i).getRecordmonth()+"月");
					rcList.add(list.get(i).getValue());
				}
			} else if(displayOption==2) {
				for(int i=0;i<list.size();i++){
					dateList.add(list.get(i).getRecordyear()+"年"+list.get(i).getRecordmonth()+"月");
					rcList.add(list.get(i).getValue());
				}
			}

		}else if (id_type==5) {
			/**某县3A景区每月接待人次 **/
			list=tjnewjdrcMonthlyService.getMx3ajqjdrs(year_id, id);
			if (displayOption==1) {
			for(int i=0;i<list.size();i++){
			dateList.add(list.get(i).getRecordmonth()+"月");
			rcList.add(list.get(i).getValue());
			}
			} else if(displayOption==2) {
			for(int i=0;i<list.size();i++){
			dateList.add(list.get(i).getRecordyear()+"年"+list.get(i).getRecordmonth()+"月");
			rcList.add(list.get(i).getValue());
			}
			}
		}
		list3.add(rcList);
		hashMap.put("x", dateList);
		hashMap.put("y", list3);
		return hashMap;
		
	}
}
