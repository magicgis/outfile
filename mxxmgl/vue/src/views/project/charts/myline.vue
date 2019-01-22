<template>
    <div id="app-container">
      <div class="filter-container">
        <el-form :inline="true" :model="searchData" class="demo-form-inline" v-if="hasPerm('outcome:list')">
          <el-form-item label="项目编号" required >
            <el-select v-model="project.projectCode"  placeholder="请选择" clearable>
              <el-option
                v-for="item in projectCodes"
                :key="item.id"
                :label="item.projectName"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <ve-line :data="chartData"></ve-line>
    </div>
</template>

<script>
    import VeLine from 'v-charts/lib/line'

    export default {
        name: "myline",
        data(){
          return{
            project:{
              projectCode:''
            },
            projectCodes:[],
            searchData:{
              projectId:'',
              projectName:''
            }
          }
        },
        created(){
          this.chartData = {
            columns: ['日期', '项目进度'],
            rows: [
              { '日期': '12月1日', '项目进度': 0.1 },
              { '日期': '12月2日', '项目进度': 0.15 },
              { '日期': '12月3日', '项目进度': 0.2 },
              { '日期': '12月4日', '项目进度': 0.3 },
              { '日期': '12月5日', '项目进度': 0.5 },
              { '日期': '12月6日', '项目进度': 0.7 }
            ]
          }
          if(this.hasPerm('file:list')){
            this.getProjectCodes();
          }

        },
        methods:{
          //查询项目代码列表
          getProjectCodes(){
            this.api({
              url:"/project/getProjectCodes",
              method:"get"
            }).then(data=>{
              this.listLoading = false;
              this.projectCodes = data.data;
            })
          },

        },
      components: { VeLine }
    }
</script>

<style >



</style>
