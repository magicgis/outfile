<%@page import="com.naswork.model.Menu"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>BetterAir客户关系管理系统</title>
    <jsp:include page="/WEB-INF/template/common/resource.jsp" />
        <script type="text/javascript">
            var tab = null;
            var accordion = null;
            var tree = null;
            var tabItems = [];
            $(function (){
                //布局
                $("#layout1").ligerLayout({ leftWidth: 230, height: '100%',heightDiff:-30,space:4, onHeightChanged: f_heightChanged });
              
                var height = $(".l-layout-center").height();
                //Tab
                $("#framecenter").ligerTab({
                    height: height,
                    showSwitchInTab : true,
                    showSwitch: true,
                    onAfterAddTabItem: function (tabdata)
                    {
                        tabItems.push(tabdata);
                        saveTabStatus();
                    },
                    onAfterRemoveTabItem: function (tabid)
                    { 
                        for (var i = 0; i < tabItems.length; i++)
                        {
                            var o = tabItems[i];
                            if (o.tabid == tabid)
                            {
                                tabItems.splice(i, 1);
                                saveTabStatus();
                                break;
                            }
                        }
                    },
                    onReload: function (tabdata)
                    {
                        var tabid = tabdata.tabid;
                        addFrameSkinLink(tabid);
                    }
                });

                //面板
                $("#accordion1").ligerAccordion({ height: height - 34, speed: null });

                $(".l-link").hover(function ()
                {
                    $(this).addClass("l-link-over");
                }, function ()
                {
                    $(this).removeClass("l-link-over");
                });

                tab = liger.get("framecenter");
                accordion = liger.get("accordion1");
                
                <%
                	List<Menu> menus = (List<Menu>)request.getAttribute("rootmenus");
                	for(int i=0;i<menus.size();i++){
                		Menu menu = menus.get(i);
                %>
	              	//树
	             	PJ.pidTree('tree<%=i+1%>','<%=path%>/userSubMenus/<%=menu.getMenuId()%>',null,{isExpand:3});
	                <%}%>
	                
	                tree = liger.get("tree1");
	                $("#pageloading").hide();
	                
	                //注销
	                $("#logout").click(function(){
	                	PJ.confirm('您确定要注销登录吗？', function (yes){
							if(yes){
								window.location.href = "<%=path%>/logout";
							}
						});
	                });
	                //修改密码
	                $("#changePass").click(function(){
	                	var iframeId = 'changePass';
						PJ.openDialog(iframeId, '修改密码', '<%=path%>/updatepass', {
							save:function(item,dialog){
								//调用子页面的校验方法
								var flag = window.frames[iframeId].validate();
								if(flag){
									dialog.hide();//先不关闭,否则子窗口会被销毁
									PJ.ajax({
										url:'<%=path%>/updatepassword',
										//调用子页面的数据获取方法
										data:window.frames[iframeId].getFormData(),
										loading:"正在保存...",
										success:function(result){
											if(result.success){
												PJ.success(result.message,function(){});
											}else{
												PJ.error(result.message);
											    
											}
											dialog.close();
										}
									});
							   }
							},
							cancle:function(item,dialog){
								dialog.close();
							},
							width:350,
							height:210
						});
	                });
	                
	                //切换身份
	                $("#qhsf").click(function(){
	                	var id = "qhsfiframe";
	                	var title = "切换身份";
	                	var url = "<%=path%>/qhsf";
	                	PJ.openDialog(id, title, url, {
	                		save:function(item,dialog){
	                			//调用子页面的校验方法
								var flag = window.frames[id].validate();
								if(flag){
									dialog.hide();
									PJ.ajax({
										url:'<%=path%>/qhsf',
										//调用子页面的数据获取方法
										data:'id='+window.frames[id].getFormData(),
										loading:"正在切换...",
										success:function(result){
											if(result.success){
												window.location.reload();
											}else{
												PJ.error(result.message);
											}
											dialog.close();
										}
									});
								}else{
									PJ.warn("请选择一个身份进行切换");								
								}
	                		},
	                		cancle:function(item,dialog){
								dialog.close();
							},
							width:450,
							height:200,
							saveTitle:'切换'
	                	});
	                });
	                
	              //时间日期
	               //$("#headDay").jclock({ withDate: true, withWeek: true });
            });
            
            function f_heightChanged(options)
            {  
                if (tab)
                    tab.addHeight(options.diff);
                if (accordion && options.middleHeight - 34 > 0)
                    accordion.setHeight(options.middleHeight - 34);
            }
            function f_addTab(tabid, text, url)
            {
            	if(url.startWith("/")){
            		if(!url.startWith("<%=path%>")){
	            		url = '<%=path%>'+url;
            		}
            	}
            	
                tab.addTabItem({
                    tabid: tabid,
                    text: text,
                    url: url,
                    callback: function ()
                    {
                        addFrameSkinLink(tabid); 
                    }
                });
            }
            function addFrameSkinLink(tabid)
            {
                var prevHref = getLinkPrevHref(tabid) || "";
                var skin = getQueryString("skin");
                if (!skin) return;
                skin = skin.toLowerCase();
                attachLinkToFrame(tabid, prevHref + skin_links[skin]);
            }
            function saveTabStatus()
            { 
                $.cookie('liger-home-tab', JSON2.stringify(tabItems));
            }
            
            function getQueryString(name)
            {
                var now_url = document.location.search.slice(1), q_array = now_url.split('&');
                for (var i = 0; i < q_array.length; i++)
                {
                    var v_array = q_array[i].split('=');
                    if (v_array[0] == name)
                    {
                        return v_array[1];
                    }
                }
                return false;
            }
            function attachLinkToFrame(iframeId, filename)
            { 
                if(!window.frames[iframeId]) return;
                var head = window.frames[iframeId].document.getElementsByTagName('head').item(0);
                var fileref = window.frames[iframeId].document.createElement("link");
                if (!fileref) return;
                fileref.setAttribute("rel", "stylesheet");
                fileref.setAttribute("type", "text/css");
                fileref.setAttribute("href", filename);
                head.appendChild(fileref);
            }
            function getLinkPrevHref(iframeId)
            {
                if (!window.frames[iframeId]) return;
                var head = window.frames[iframeId].document.getElementsByTagName('head').item(0);
                var links = $("link:first", head);
                for (var i = 0; links[i]; i++)
                {
                    var href = $(links[i]).attr("href");
                    if (href && href.toLowerCase().indexOf("ligerui") > 0)
                    {
                        return href.substring(0, href.toLowerCase().indexOf("<%=path %>/resources/js") );
                    }
                }
            }
            
            //关闭当前tab
            function closeCurrentTab(){
            	tab.removeSelectedTabItem();
            }
           	
            //刷新 tab
            function reloadTab(tabid){
            	tab.reload(tabid);
            }
            
            $(function(){
            	tab.addTabItem({
                    tabid: "home",
                    text: "我的主页",
                    showClose:false,
                    url: "<%=path%>/home/index"
                });
            });
     </script> 
</head>
<body style="padding:0px;background:#EAEEF5;">  
<div id="pageloading"></div>  
 <!-- 头部 start -->
 <jsp:include page="common/header.jsp" />
 <!-- 头部 end -->
 <!-- 主体 start -->
  <div id="layout1"> 
        <div position="left" title="菜单" id="accordion1"> 
        	<c:forEach items="${rootmenus }" var="menu" varStatus="s">
        		 <div title="${menu.menuName }">
                         <ul id="tree${s.index+1 }" style="margin-top:3px;">
                    </div>
        	</c:forEach>
        </div>
        <div position="center" id="framecenter"> 
        </div> 
    </div>
    <!-- 主体 end -->
    <!-- 页脚 start-->
    <jsp:include page="common/footer.jsp" />
    <!-- 页脚 end-->
</body>
</html>
