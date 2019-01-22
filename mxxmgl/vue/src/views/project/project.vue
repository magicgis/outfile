<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('project:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline" v-if="hasPerm('project:list')">
        <el-form-item label="项目编号">
          <el-input v-model="searchForm.projectCode" clearable placeholder="项目编号"></el-input>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="searchForm.projectName" clearable placeholder="项目名称"></el-input>
        </el-form-item>
        <el-form-item label="创建人">
          <el-select v-model="searchForm.createUser" clearable placeholder="请选择">
            <el-option
              v-for="item in users"
              :key="item.createUser"
              :label="item.userNickName"
              :value="item.createUser">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.projectStatus" clearable placeholder="请选择">
            <el-option label="新建" value="1"></el-option>
            <el-option label="验收" value="0"></el-option>
            <el-option label="立项" value="2"></el-option>
            <el-option label="建设中" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row :default-sort = "{prop: 'createTime', order: 'ascending'}">
      <el-table-column type="expand" label="详情" width="80">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="项目编号:">
              <span>{{ props.row.projectCode }}</span>
            </el-form-item>
            <el-form-item label="项目名称:">
              <span>{{ props.row.projectName }}</span>
            </el-form-item>
            <el-form-item label="项目简介:">
              <span>{{ props.row.projectDesc }}</span>
            </el-form-item>
            <el-form-item label="项目状态:">
              <span v-if="props.row.projectStatus === '0'">验收</span>
              <span v-if="props.row.projectStatus === '1'">新建</span>
              <span v-if="props.row.projectStatus === '2'">立项</span>
              <span v-if="props.row.projectStatus === '3'">建设中</span>
              <!--<span v-if="props.row.projectStatus === '4'">加急</span>-->
            </el-form-item>
            <el-form-item label="项目金额(万):">
              <span>{{ props.row.projectMoney }}</span>
            </el-form-item>
            <el-form-item label="创建者:">
              <span>{{ props.row.userNickName }}</span>
            </el-form-item>
            <el-form-item label="计划天数:">
              <span>{{ props.row.planDays }}</span>
            </el-form-item>
            <el-form-item label="创建时间:">
              <span>{{ pTime(props.row.createTime) }}</span>
            </el-form-item>
            <el-form-item label="更新时间:">
              <span>{{ pTime(props.row.updateTime)}}</span>
            </el-form-item>
            <el-form-item label="项目成员:">
              <span>{{ props.row.userNickName }}</span>
            </el-form-item>
            <el-form-item label="当前进度:">
              <span>{{ props.row.projectProgress }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="序号" width="80" >
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="项目编号" prop="projectCode" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="项目名" prop="projectName" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="状态" prop="projectStatus" style="width: 30px;" width="100">
      <template slot-scope="scope">
        <span v-if="scope.row.projectStatus === '0'">验收</span>
        <span v-if="scope.row.projectStatus === '1'">新建</span>
        <span v-if="scope.row.projectStatus === '2'">立项</span>
        <span v-if="scope.row.projectStatus === '3'">建设中</span>
        <!--<el-tag type="success" :hit=false v-text="" v-if="scope.row.projectStatus==='0'">{{projectStatus[0].value}}</el-tag>-->
        <!--<el-tag type="primary" v-text=""  v-if="scope.row.projectStatus==='1'">{{projectStatus[1].value}}</el-tag>-->
        <!--<el-tag type="info" v-text=""  v-if="scope.row.projectStatus==='2'">{{projectStatus[2].value}}</el-tag>-->
        <!--<el-tag type="danger" v-text=""  v-if="scope.row.projectStatus==='3'">{{projectStatus[3].value}}</el-tag>-->
        <!--<el-tag type="danger" v-text=""  v-if="scope.row.projectStatus==='4'">{{projectStatus[4].value}}</el-tag>-->
      </template>
    </el-table-column>
      <el-table-column align="center" label="创建者" prop="userNickName"  style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" sortable width="170">
        <template scope="scope">
            {{pTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column align="left" label="管理" width="300" v-if="hasPerm('project:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="delete" v-if="scope.row.projectStatus== '1'"
                     @click="removeProject(scope.$index)">删除
          </el-button>
          <el-button type="success" icon="edit" @click="showFiles(scope.$index)">文件管理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempProject" label-position="left" label-width="80px"
               style='width: 350px; margin-left:50px;'>
        <el-form-item label="项目编号" required >
        <el-input type="text" v-model="tempProject.projectCode">
        </el-input>
      </el-form-item>
        <el-form-item label="项目名称" required >
          <el-input type="text" v-model="tempProject.projectName">
          </el-input>
        </el-form-item>
        <el-form-item label="计划天数" required >
          <el-input type="text" v-model="tempProject.planDays" placeholder="可以为年/月/日 如：两年">
          </el-input>
        </el-form-item>
        <el-form-item label="项目金额" required >
          <el-input type="text" v-model="tempProject.projectMoney" placeholder="单位为万">
          </el-input>
        </el-form-item>
        <el-form-item label="创建者" required v-if="dialogStatus=='update'">
          <el-input type="text" :disabled="true" v-model="tempProject.userNickName" >
          </el-input>
        </el-form-item>
        <el-form-item label="状态" required v-if="dialogStatus=='update'">
          <el-select v-model="tempProject.projectStatus" placeholder="请选择">
            <el-option
              v-for="item in projectStatus"
              :key="item.status"
              :label="item.value"
              :value="item.status">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目简介" required>
          <el-input type="textarea"
                    :autosize="{ minRows: 4, maxRows: 10}"
                    v-model="tempProject.projectDesc">
          </el-input>
        </el-form-item>

        <el-form-item label="当前进度" required v-if="dialogStatus=='update'">
          <el-input type="textarea"
                    :autosize="{ minRows: 4, maxRows: 10}"
                    v-model="tempProject.projectProgress">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createProject">创 建</el-button>
        <el-button type="primary" v-else @click="updateProject">修 改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="文件管理" :visible.sync="fileDialogFormVisible">


      <div slot="footer" class="dialog-footer">
        <el-button @click="fileDialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'
  import store from '../../store'
  import {parseTime} from "../../utils/index";


  export default {
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 10,//每页条数
        },
        projectStatus:[
          {
            status:0,
            value:'验收'
          },
          {
            status:1,
            value:'新建'
          },
          {
            status:2,
            value:'立项'
          },
          {
            status:3,
            value:'建设中'
          },
          // {
          //   status:4,
          //   value:'加急'
          // }
        ],
        tempProject:{
          projectName:'',
          projectDesc:'',
          projectStatus:'',
          planDays:'',
          projectId:'',
          // createUser:store.getters.userId,
          createUser:'',
          userNickName:'',
          projectMoney:'',
          projectCode:'',
          currentProgress:'',
          projectProgress:''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建项目'
        },
        searchForm: {
          projectName: '',
          projectDesc: '',
          projectStatus:'',
          createUser:'',
          pageNum:'',
          pageRow:''
        },
        users:[],
        fileDialogFormVisible:false
      }
    },
    created() {
      this.getList();
      if (this.hasPerm('project:list')) {
        this.getUsers();
      }
    },
    computed: {
      ...mapGetters([
        'projectId'
      ])
    },
    methods: {
      // 弹出文件管理对话框
      showFiles($index){
        let _vue = this;
        let project = _vue.list[$index];
        store.commit("SET_PROJECTID",project.projectId);
        store.commit("SET_PROJECTCODE",project.projectCode);
        this.$router.push({ path:'/project/fileList'});
      },

      //带条件的查询
      onSubmit(){
        this.searchForm.pageNum = this.listQuery.pageNum;
        this.searchForm.pageRow = this.listQuery.pageRow;
        this.listLoading = true;
        this.api({
          url:"/project/getProjectListBySearch",
          method:"post",
          data:this.searchForm
        }).then(data=>{
          this.listLoading = false;
          this.list = data.data.records;
          this.totalCount = data.totalCount;
        })
      },

      //查询项目列表
      getUsers(){
        this.api({
          url:"/project/getUsers",
          method:"get",
        }).then(data=>{
          this.listLoading = false;
          this.users = data.data;
        })
      },

      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/project/getProjectListPage",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.data.records;
          this.totalCount = data.totalCount;
        })
      },

      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },

      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },

      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },

      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },

      showCreate() {
        //显示新增对话框
        this.tempProject.projectProgress = "";
        this.tempProject.projectCode = "";
        this.tempProject.projectName = "";
        this.tempProject.projectDesc = "";
        this.tempProject.projectStatus = "";
        this.tempProject.planDays = "";
        this.tempProject.projectMoney = "";
        this.tempProject.createUser =store.getters.userId;
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },

      showUpdate($index) {
        let project = this.list[$index];
        this.tempProject.projectProgress = project.projectProgress;
        this.tempProject.projectCode = project.projectCode;
        this.tempProject.projectName = project.projectName;
        this.tempProject.projectDesc = project.projectDesc;
        this.tempProject.projectId = project.projectId;
        this.tempProject.planDays = project.planDays;
        this.tempProject.userNickName = project.userNickName;
        this.tempProject.projectMoney = project.projectMoney;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },

      createProject() {
        //添加新项目
        this.api({
          url: "/project/insertProject",
          method: "post",
          data: this.tempProject
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },

      updateProject() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/project/updateProject",
          method: "post",
          data: this.tempProject
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.projectId === this.tempProject.projectId) {
            msg = '修改成功,部分信息重新登录后生效'
          }
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },

      removeProject($index) {
        let _vue = this;
        this.$confirm('确定删除此项目?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let project = _vue.list[$index];
          console.log(project);
          // project.projectStatus = '2';
          _vue.api({
            url: "/project/deleteProjectById",
            method: "post",
            data: project
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },

      formatDate(time){
        return parseTime(time);
      }

    },
    // filters:{
    //   formatDate(time){
    //     return parseTime(time,'yyyy-MM-dd hh:mm:ss');
    //   }
    // }
  }
</script>
<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 110px;
    color: #99a9bf;
    text-align: right;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
