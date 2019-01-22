<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.everygold.dao.${className}Dao">
	
	<resultMap type="com.everygold.model.${className}" id="${className}">
	<#list fieldList as var>
		<result property="${var.name}" column="${var.column}" jdbcType="${var.type}" />
	</#list>
	</resultMap>
	
	<!-- 根据主键查询 -->
	<select id="findById" parameterType="String" resultMap="${className}">
		SELECT * FROM ${tableName}
		where ${pkMap.column} = ${r"#{"}${pkMap.name}${r"}"}
	</select>
	
	<!-- 查询分页 -->
	<select id="findPage" parameterType="page" resultMap="${className}">
		SELECT * FROM ${tableName}
		<where>
			${r"${where}"}
		</where>
		<if test="orderby!=null">
			${r"${orderby}"}
		</if>
	</select>
	
	<!-- 新增 -->
	<insert id="insert" parameterType="com.everygold.model.${className}">
   	<selectKey resultType="String" order="BEFORE" keyProperty="${pkMap.name}">
		 SELECT sys_guid() FROM DUAL
	</selectKey>
	 insert into ${tableName} (
	 	<#list fieldList as var>
	 	  <#if var_has_next>
	 	  	${var.column},
	 	  <#else>
	 	    ${var.column}
	 	  </#if>
 		</#list>
	 ) 
	 values (
	 	<#list fieldList as var>
	 	  <#if var_has_next>
	 	  	${r"#{"}${var.name},jdbcType=${var.type}${r"}"},
	 	  <#else>
	 	    ${r"#{"}${var.name},jdbcType=${var.type}${r"}"}
	 	  </#if>
 		</#list>
	 )
   </insert>
   
   <!-- 更新 -->
   <update id="update" parameterType="com.everygold.model.${className}">
	 update ${tableName}
	 <set>
	 	<#list fieldList as var>
	 	<#if var.name != pkMap.name>
	 	<if test="${var.name} != null ">
	 	 ${var.column} =  ${r"#{"}${var.name}${r"}"},
	 	 </if>
	 	 </#if>
	 	</#list>
	 </set>
	 where ${pkMap.column}=${r"#{"}${pkMap.name}${r"}"}
   </update>
   
   <!-- 删除 -->
   <delete id="delete" parameterType="String">
   		delete from ${tableName} where ${pkMap.column}=${r"#{"}${pkMap.name}${r"}"}
   </delete>  
   

</mapper>