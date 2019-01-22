package com.naswork.utils;

import java.util.HashMap;
import java.util.Map;

public class PlateUtils {
	
	private static Map<String,String> plate = new HashMap<String,String>();
	
	static{
		plate.put("冀A","石家庄");
		plate.put("冀B","唐山");
		plate.put("冀C","秦皇岛");
		plate.put("冀D","邯郸");
		plate.put("冀E","邢台");
		plate.put("冀F","保定");
		plate.put("冀G","张家口");
		plate.put("冀H","承德");
		plate.put("冀J","沧州");
		plate.put("冀R","廊坊");
		plate.put("冀S","沧州");
		plate.put("冀T","衡水");
		plate.put("辽A","沈阳");
		plate.put("辽B","大连");
		plate.put("辽C","鞍山");
		plate.put("辽D","抚顺");
		plate.put("辽E","本溪");
		plate.put("辽F","丹东");
		plate.put("辽G","锦州");
		plate.put("辽H","营口");
		plate.put("辽J","阜新");
		plate.put("辽K","辽阳");
		plate.put("辽L","盘锦");
		plate.put("辽M","铁岭");
		plate.put("辽N","朝阳");
		plate.put("辽P","葫芦岛");
		plate.put("皖A","合肥");
		plate.put("皖B","芜湖");
		plate.put("皖C","蚌埠");
		plate.put("皖D","淮南");
		plate.put("皖E","马鞍山");
		plate.put("皖F","淮北");
		plate.put("皖G","铜陵");
		plate.put("皖H","安庆");
		plate.put("皖J","黄山");
		plate.put("皖K","阜阳");
		plate.put("皖L","宿州");
		plate.put("皖M","滁州");
		plate.put("皖N","六安");
		plate.put("皖P","宣城");
		plate.put("皖Q","巢湖");
		plate.put("皖R","池州");
		plate.put("皖S","亳州");
		plate.put("苏A","南京");
		plate.put("苏B","无锡");
		plate.put("苏C","徐州");
		plate.put("苏D","常州");
		plate.put("苏E","苏州");
		plate.put("苏F","南通");
		plate.put("苏G","连云港");
		plate.put("苏H","淮安");
		plate.put("苏J","盐城");
		plate.put("苏K","扬州");
		plate.put("苏L","镇江");
		plate.put("苏M","泰州");
		plate.put("苏N","宿迁");
		plate.put("鄂A","武汉");
		plate.put("鄂B","黄石");
		plate.put("鄂C","十堰");
		plate.put("鄂D","荆州");
		plate.put("鄂E","宜昌");
		plate.put("鄂F","襄樊");
		plate.put("鄂G","鄂州");
		plate.put("鄂H","荆门 ");
		plate.put("鄂J","黄冈");
		plate.put("鄂K","孝感");
		plate.put("鄂L","咸宁");
		plate.put("鄂M","仙桃");
		plate.put("鄂N","潜江");
		plate.put("鄂P","神农架林区");
		plate.put("鄂Q","恩施土家族苗族自治州");
		plate.put("鄂R","天门");
		plate.put("鄂S","随州");
		plate.put("晋A","太原");
		plate.put("晋B","大同");
		plate.put("晋C","阳泉");
		plate.put("晋D","长治");
		plate.put("晋E","晋城");
		plate.put("晋F","朔州");
		plate.put("晋H","忻州");
		plate.put("晋J","吕梁地区");
		plate.put("晋K","晋中");
		plate.put("晋L","临汾");
		plate.put("晋M","运城");
		plate.put("吉A","长春");
		plate.put("吉B","吉林");
		plate.put("吉C","四平");
		plate.put("吉D","辽源");
		plate.put("吉E","通化");
		plate.put("吉F","白山");
		plate.put("吉G","白城");
		plate.put("吉H","延边朝鲜族自治州");
		plate.put("吉J","松原");
		plate.put("吉K","长白山");
		plate.put("粤A","广州");
		plate.put("粤B","深圳");
		plate.put("粤C","珠海");
		plate.put("粤D","汕头");
		plate.put("粤E","佛山");
		plate.put("粤F","韶关");
		plate.put("粤G","湛江");
		plate.put("粤H","肇庆");
		plate.put("粤J","江门");
		plate.put("粤K","茂名");
		plate.put("粤L","惠州");
		plate.put("粤M","梅州");
		plate.put("粤N","汕尾");
		plate.put("粤P","河源");
		plate.put("粤Q","阳江");
		plate.put("粤R","清远");
		plate.put("粤S","东莞");
		plate.put("粤T","中山");
		plate.put("粤U","潮州");
		plate.put("粤V","揭阳");
		plate.put("粤W","云浮");
		plate.put("粤X","顺德区");
		plate.put("粤Y","南海区");
		plate.put("粤Z","港澳进入内地车辆");
		plate.put("宁A","银川");
		plate.put("宁B","石嘴山");
		plate.put("宁C","银南");
		plate.put("宁D","固原");
		plate.put("宁E","中卫");
		plate.put("京A","北京");
		plate.put("京B","北京");
		plate.put("京C","北京");
		plate.put("京D","北京");
		plate.put("京E","北京");
		plate.put("京F","北京");
		plate.put("京G","北京");
		plate.put("京H","北京");
		plate.put("京J","北京");
		plate.put("京K","北京");
		plate.put("京L","北京");
		plate.put("京M","北京");
		plate.put("京Y","北京");
		plate.put("豫A","郑州");
		plate.put("豫B","开封");
		plate.put("豫C","洛阳");
		plate.put("豫D","平顶山");
		plate.put("豫E","安阳");
		plate.put("豫F","鹤壁");
		plate.put("豫G","新乡");
		plate.put("豫H","焦作");
		plate.put("豫J","濮阳");
		plate.put("豫K","许昌");
		plate.put("豫L","漯河");
		plate.put("豫M","三门峡");
		plate.put("豫N","商丘");
		plate.put("豫P","周口");
		plate.put("豫Q","驻马店");
		plate.put("豫R","南阳");
		plate.put("豫S","信阳");
		plate.put("豫U","济源");
		plate.put("黑A","哈尔滨");
		plate.put("黑B","齐齐哈尔");
		plate.put("黑C","牡丹江");
		plate.put("黑D","佳木斯");
		plate.put("黑E","大庆");
		plate.put("黑F","伊春");
		plate.put("黑G","鸡西");
		plate.put("黑H","鹤岗");
		plate.put("黑J","双鸭山");
		plate.put("黑K","七台河");
		plate.put("黑L","松花江地区");
		plate.put("黑M","绥化");
		plate.put("黑N","黑河");
		plate.put("黑P","大兴安岭地区");
		plate.put("黑R","农垦系统");
		plate.put("鲁A ","济南");
		plate.put("鲁B","青岛");
		plate.put("鲁C","淄博");
		plate.put("鲁D","枣庄");
		plate.put("鲁E","东营");
		plate.put("鲁F","烟台");
		plate.put("鲁G","潍坊");
		plate.put("鲁H","济宁");
		plate.put("鲁J","泰安");
		plate.put("鲁K","威海");
		plate.put("鲁L","日照");
		plate.put("鲁M","滨州");
		plate.put("鲁N","德州");
		plate.put("鲁P","聊城");
		plate.put("鲁Q","临沂");
		plate.put("鲁R","菏泽");
		plate.put("鲁S","莱芜");
		plate.put("鲁U","青岛增补");
		plate.put("鲁V","潍坊增补");
		plate.put("鲁Y","烟台");
		plate.put("浙A","杭州");
		plate.put("浙B","宁波");
		plate.put("浙C","温州");
		plate.put("浙D","绍兴");
		plate.put("浙E","湖州");
		plate.put("浙F","嘉兴");
		plate.put("浙G","金华");
		plate.put("浙H","衢州");
		plate.put("浙J","台州");
		plate.put("浙K","丽水");
		plate.put("浙L","舟山");
		plate.put("桂A","南宁");
		plate.put("桂B","柳州");
		plate.put("桂C","桂林");
		plate.put("桂D","梧州");
		plate.put("桂E","北海");
		plate.put("桂F","崇左");
		plate.put("桂G","来宾");
		plate.put("桂H","桂林地区");
		plate.put("桂J","贺州");
		plate.put("桂K","玉林");
		plate.put("桂L","百色");
		plate.put("桂M","河池");
		plate.put("桂N","钦州");
		plate.put("桂P","防城港");
		plate.put("桂R","贵港");
		plate.put("蒙A","呼和浩特");
		plate.put("蒙B","包头");
		plate.put("蒙C","乌海");
		plate.put("蒙D","赤峰");
		plate.put("蒙E","呼伦贝尔 ");
		plate.put("蒙F","兴安盟");
		plate.put("蒙G","通辽");
		plate.put("蒙H","锡林郭勒盟");
		plate.put("蒙J","乌兰察布盟");
		plate.put("蒙K","鄂尔多斯 ");
		plate.put("蒙L","巴彦淖尔盟");
		plate.put("蒙M　","　阿拉善盟");
		plate.put("闽A","福州");
		plate.put("闽B","莆田");
		plate.put("闽C","泉州");
		plate.put("闽D","厦门");
		plate.put("闽E","漳州");
		plate.put("闽F","龙岩");
		plate.put("闽G","三明");
		plate.put("闽H","南平");
		plate.put("闽J","宁德");
		plate.put("闽K","直系统");
		plate.put("川A","成都");
		plate.put("川B","绵阳");
		plate.put("川C","自贡");
		plate.put("川D","攀枝花");
		plate.put("川E","泸州");
		plate.put("川F","德阳");
		plate.put("川H","广元");
		plate.put("川J","遂宁");
		plate.put("川K","内江");
		plate.put("川L","乐山");
		plate.put("川M","资阳");
		plate.put("川Q","宜宾");
		plate.put("川R","南充");
		plate.put("川S","达州");
		plate.put("川T","雅安");
		plate.put("川U","阿坝藏族羌族自治州");
		plate.put("川V","甘孜藏族自治州");
		plate.put("川W","凉山彝族自治州");
		plate.put("川X","广安");
		plate.put("川Y","巴中");
		plate.put("川Z","眉山");
		plate.put("渝A","重庆区（江南）");
		plate.put("渝B","重庆区（江北）");
		plate.put("渝C","永川区");
		plate.put("渝F","万州区");
		plate.put("渝G","涪陵区");
		plate.put("渝H","黔江区");
		plate.put("津A","天津");
		plate.put("津B","天津");
		plate.put("津C","天津");
		plate.put("津D","天津");
		plate.put("津E","天津");
		plate.put("津F","天津");
		plate.put("津G","天津");
		plate.put("津H","天津");
		plate.put("云A","昆明");
		plate.put("云A-V","东川");
		plate.put("云C","昭通");
		plate.put("云D","曲靖");
		plate.put("云E","楚雄彝族自治州");
		plate.put("云F","玉溪");
		plate.put("云G","红河哈尼族彝族自治州");
		plate.put("云H","文山壮族苗族自治州");
		plate.put("云J","思茅");
		plate.put("云K","西双版纳傣族自治州");
		plate.put("云L","大理白族自治州");
		plate.put("云M","保山");
		plate.put("云N","德宏傣族景颇族自治州");
		plate.put("云P","丽江");
		plate.put("云Q","怒江傈僳族自治州");
		plate.put("云R","迪庆藏族自治州");
		plate.put("云S","临沧地区");
		plate.put("湘A ","长沙");
		plate.put("湘B","株洲");
		plate.put("湘C","湘潭");
		plate.put("湘D","衡阳");
		plate.put("湘E","邵阳");
		plate.put("湘F","岳阳");
		plate.put("湘G","张家界");
		plate.put("湘H","益阳");
		plate.put("湘J","常德");
		plate.put("湘K","娄底");
		plate.put("湘L","郴州");
		plate.put("湘M","永州");
		plate.put("湘N","怀化");
		plate.put("湘U","湘西土家族苗族自治州");
		plate.put("新A","乌鲁木齐");
		plate.put("新B","昌吉回族自治州");
		plate.put("新C","石河子");
		plate.put("新D","奎屯");
		plate.put("新E","博尔塔拉蒙古自治州");
		plate.put("新F","伊犁哈萨克自治州直辖县");
		plate.put("新G","塔城");
		plate.put("新H","阿勒泰");
		plate.put("新J","克拉玛依");
		plate.put("新K","吐鲁番");
		plate.put("新L","哈密");
		plate.put("新M","巴音郭愣蒙古自治州");
		plate.put("新N","阿克苏");
		plate.put("新P","克孜勒苏柯尔克孜自治州");
		plate.put("新Q","喀什");
		plate.put("新R","和田");
		plate.put("赣A","南昌");
		plate.put("赣B","赣州");
		plate.put("赣C","宜春");
		plate.put("赣D","吉安");
		plate.put("赣E","上饶");
		plate.put("赣F","抚州");
		plate.put("赣G","九江");
		plate.put("赣H","景德镇");
		plate.put("赣J","萍乡");
		plate.put("赣K","新余");
		plate.put("赣L","鹰潭");
		plate.put("赣M","南昌");
		plate.put("甘A","兰州");
		plate.put("甘B","嘉峪关");
		plate.put("甘C","金昌");
		plate.put("甘D","白银");
		plate.put("甘E","天水");
		plate.put("甘F","酒泉");
		plate.put("甘G","张掖");
		plate.put("甘H","武威");
		plate.put("甘J","定西");
		plate.put("甘K","陇南");
		plate.put("甘L","平凉");
		plate.put("甘M","庆阳");
		plate.put("甘N","临夏回族自治州");
		plate.put("甘P","甘南藏族自治州");
		plate.put("陕A","西安");
		plate.put("陕B","铜川");
		plate.put("陕C","宝鸡");
		plate.put("陕D","咸阳");
		plate.put("陕E","渭南");
		plate.put("陕F","汉中");
		plate.put("陕G","安康");
		plate.put("陕H","商洛");
		plate.put("陕J","延安");
		plate.put("陕K","榆林");
		plate.put("陕V","杨凌高新农业示范区");
		plate.put("贵A","贵阳");
		plate.put("贵B","六盘水");
		plate.put("贵C","遵义");
		plate.put("贵D","铜仁");
		plate.put("贵E","黔西南布依族苗族自治州");
		plate.put("贵F","毕节");
		plate.put("贵G","安顺");
		plate.put("贵H","黔东南苗族侗族自治州");
		plate.put("贵J","黔南布依族苗族自治州");
		plate.put("青A","西宁");
		plate.put("青B","海东");
		plate.put("青C","海北藏族自治州");
		plate.put("青D","黄南藏族自治州");
		plate.put("青E","藏族自治州");
		plate.put("青F","果洛藏族自治州");
		plate.put("青G","玉树藏族自治州");
		plate.put("青H","海西蒙古族藏族自治州");
		plate.put("藏A","拉萨");
		plate.put("藏B","昌都地区");
		plate.put("藏C","山南地区");
		plate.put("藏D","日喀则地区");
		plate.put("藏E","那曲地区");
		plate.put("藏F","阿里地区");
		plate.put("藏G","林芝地区");
		plate.put("藏H","天全县车辆管理所");
		plate.put("藏J","格尔木车辆管理所");
		plate.put("琼A","海口");
		plate.put("琼B","三亚");
		plate.put("琼C","琼海");
		plate.put("琼D","五指山");
		plate.put("琼E","洋浦开发区");
		plate.put("沪A","上海");
		plate.put("沪B","上海");
		plate.put("沪C","上海");
		plate.put("沪D","上海");
		plate.put("沪R","崇明、长兴、横沙");
	}
	
	public static String getCity(String province,String city){
		
		String plateCity = plate.get(province+city);
		if(StringUtil.isEmpty(plateCity)){
			plateCity = getProvince(province);
			
		}
		return plateCity;
	}
	
	public static String getProvince(String province){
		switch(province){
		case "京":
			return"北京";
		case "津":
			return"天津";
		case "沪":
			return"上海";
		case "渝":
			return"重庆";
		case "冀":
			return"河北";
		case "豫":
			return"河南";
		case "云":
			return"云南";
		case "辽":
			return"辽宁";
		case "黑":
			return"黑龙江";
		case "湘":
			return"湖南";
		case "皖":
			return"安徽";
		case "闽":
			return"福建";
		case "鲁":
			return"山东";
		case "新":
			return"新疆";
		case "苏":
			return"江苏";
		case "浙":
			return"浙江";
		case "赣":
			return"江西";
		case "鄂":
			return"湖北";
		case "桂":
			return"广西";
		case "甘":
			return"甘肃";
		case "晋":
			return"山西";
		case "蒙":
			return"内蒙";
		case "陕":
			return"陕西";
		case "吉":
			return"吉林";
		case "贵":
			return"贵州";
		case "粤":
			return"广东";
		case "青":
			return"青海";
		case "藏":
			return"西藏";
		case "川":
			return"四川";
		case "宁":
			return"宁夏";
		case "琼":
			return"海南";
		default:
			return"未知";
		}
	}
	
	public static String getYueCity(String str) {
		switch (str) {
		case "A":
			return "广州";
		case "B":
			return "深圳";
		case "C":
			return "珠海";
		case "D":
			return "汕头";
		case "E":
			return "佛山";
		case "F":
			return "韶关";
		case "G":
			return "湛江";
		case "H":
			return "肇庆";
		case "J":
			return "江门";
		case "K":
			return "茂名";
		case "L":
			return "惠州";
		case "M":
			return "梅州";
		case "N":
			return "汕尾";
		case "P":
			return "河源";
		case "Q":
			return "阳江";
		case "R":
			return "清远";
		case "S":
			return "东莞";
		case "T":
			return "中山";
		case "U":
			return "潮州";
		case "V":
			return "揭阳";
		case "W":
			return "云浮";
		case "X":
			return "顺德区";
		case "Y":
			return "南海区";
		case "Z":
			return "港澳进入内地车辆";
		default:
			return "未知";
		}
	}
	
}
