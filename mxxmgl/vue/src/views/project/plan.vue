<template>
  <div class="app-container">
    <div class="filter-container">

      <el-form :inline="true">
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('plan:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" icon="plus" v-if="hasPerm('plan:add')" @click="showInsertDialog">批量导入
          </el-button>
        </el-form-item>
      </el-form>

      <el-form :inline="true" :model="searchForm" class="demo-form-inline" v-if="hasPerm('plan:list')">
        <el-form-item label="计划编号">
          <el-input v-model="searchForm.planCode" clearable placeholder="计划编号"></el-input>
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="searchForm.planDesc" clearable placeholder="计划内容"></el-input>
        </el-form-item>

        <el-form-item label="负责人">
          <el-input v-model="searchForm.taskMembers" clearable placeholder="计划负责人"></el-input>
          <!--<el-select v-model="searchForm.taskMembers" clearable placeholder="请选择">-->
            <!--<el-option-->
              <!--v-for="item in users"-->
              <!--:key="item.createUser"-->
              <!--:label="item.userNickName"-->
              <!--:value="item.createUser">-->
            <!--</el-option>-->
          <!--</el-select>-->
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="searchForm.planStatus" clearable placeholder="请选择">
            <el-option label="立项" value="1"></el-option>
            <el-option label="完成" value="0"></el-option>
            <el-option label="执行" value="2"></el-option>
            <el-option label="作废" value="3"></el-option>
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
            <el-form-item label="计划编号:">
              <span>{{ props.row.planCode }}</span>
            </el-form-item>
            <el-form-item label="创建者:">
              <span>{{ props.row.createUserNickName }}</span>
            </el-form-item>
            <el-form-item label="项目编号:">
              <span>{{ props.row.projectCode }}</span>
            </el-form-item>
            <el-form-item label="计划类型:">
              <span>{{ props.row.planType }}</span>
            </el-form-item>
            <el-form-item label="工期:">
              <span>{{ props.row.planDays }}</span>
            </el-form-item>

            <el-form-item label="负责人:">
              <span>{{ props.row.taskMembers }}</span>
            </el-form-item>
            <el-form-item label="创建时间:">
              <span>{{ pTime(props.row.createTime) }}</span>
            </el-form-item>
            <el-form-item label="任务状态:">
              <span v-if="props.row.planStatus === 0">完成</span>
              <span v-if="props.row.planStatus === 1">立项</span>
              <span v-if="props.row.planStatus === 2">执行</span>
              <span v-if="props.row.planStatus === 3">作废</span>
              <span v-if="props.row.planStatus === 4">加急</span>
            </el-form-item>
            <el-form-item label="计划描述:">
              <span>{{ props.row.planDesc }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="计划编号" prop="planCode" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="项目编号" prop="projectCode" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="计划类型" prop="planType" style="width: 30px;" width="100">
        <!--<template slot-scope="scope">-->
          <!--<el-tag type="info" v-text=""  v-if="scope.row.planType==='新需求'">新需求</el-tag>-->
          <!--<el-tag type="danger" v-text=""  v-if="scope.row.planType==='bug修复'">bug修复</el-tag>-->
        <!--</template>-->
      </el-table-column>
      <el-table-column align="center" label="任务责任人" prop="taskMembers"  style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="状态" prop="planStatus" style="width: 30px;" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.planStatus === 0">完成</span>
          <span v-if="scope.row.planStatus === 1">立项</span>
          <span v-if="scope.row.planStatus === 2">执行</span>
          <span v-if="scope.row.planStatus === 3">作废</span>
          <span v-if="scope.row.planStatus === 4">加急</span>
          <!--<el-tag type="success" v-text="" v-if="scope.row.planStatus===0">{{planStatus[0].value}}</el-tag>-->
          <!--<el-tag type="primary" v-text=""  v-if="scope.row.planStatus===1">{{planStatus[1].value}}</el-tag>-->
          <!--<el-tag type="info" v-text=""  v-if="scope.row.planStatus===2">{{planStatus[2].value}}</el-tag>-->
          <!--<el-tag type="danger" v-text=""  v-if="scope.row.planStatus===3">{{planStatus[3].value}}</el-tag>-->
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" sortable width="170">
        <template scope = "scope">
          {{pTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column align="left" label="管理" width="220" v-if="hasPerm('plan:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="delete" v-if="scope.row.planStatus== '1'"
                     @click="removePlan(scope.$index)">删除
          </el-button>
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
      <el-form class="small-space" :model="tempPlan" label-position="left" label-width="110px"
               style='width: 350px; margin-left:50px;'>
        <el-form-item label="项目编号" required >
          <el-select v-model="tempPlan.projectCode"  placeholder="请选择">
            <el-option
              v-for="item in projectCodes"
              :key="item.id"
              :label="item.projectCode"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="计划编号" required >
          <el-input type="text" v-model="tempPlan.planCode" clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="任务工期" required >
          <el-input type="text" v-model="tempPlan.planDays" clearable placeholder="可以为年/月/日 如：两年">
          </el-input>
        </el-form-item>
        <el-form-item label="计划类型" required >
          <el-select v-model="tempPlan.planType"  placeholder="请选择" clearable>
            <el-option
              v-for="item in planType"
              :key="item.status"
              :label="item.value"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建者" required v-if="dialogStatus=='update'">
          <el-input type="text" :disabled="true" v-model="tempPlan.createUserNickName" >
          </el-input>
        </el-form-item>
        <el-form-item label="状态" required v-if="dialogStatus=='update'">
          <el-select v-model="tempPlan.planStatus" placeholder="请选择">
            <el-option
              v-for="item in planStatus"
              :key="item.status"
              :label="item.value"
              :value="item.status">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任务负责人" required >
          <el-input type="text"  v-model="tempPlan.taskMembers" clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="计划内容" required>
          <el-input type="textarea"
                    :autosize="{ minRows: 4, maxRows: 10}"
                    v-model="tempPlan.planDesc" clearable>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createPlan">创 建</el-button>
        <el-button type="primary" v-else @click="updatePlan">修 改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="文件上传" :visible.sync="uploadDialog">
     <el-form>
      <el-form-item>
        <el-upload v-if="hasPerm('plan:add')"
                   class="upload-demo"
                   ref="upload"
                   action=""
                   :on-preview="handlePreview"
                   :on-remove="handleRemove"
                   :before-remove="beforeRemove"
                   :on-change="handleChange"
                   :limit="1"
                   :on-exceed="handleExceed"
                   :auto-upload="false"
                   :file-list="fileList">
          <el-button slot="trigger" icon="plus" type="primary">选择文件</el-button>
          <el-button type="success" @click="startUpload">开始上传</el-button>
        </el-upload>
      </el-form-item>
     </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="uploadDialog = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'
  import store from '../../store'
  import axios from 'axios'

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
        planStatus:[
          {
            status:0,
            value:'完成'
          },
          {
            status:1,
            value:'新建'
          },
          {
            status:2,
            value:'执行中'
          },
          {
            status:3,
            value:'验收'
          },
          {
            status:4,
            value:'作废'
          }
        ],
        planType:[
          {
            status:0,
            value:'新需求'
          },
          {
            status:1,
            value:'bug修复'
           }
        ],
        tempPlan:{
          planId:'',
          planCode:'',
          planStatus:'',
          planDesc:'',
          planDays:'',
          projectId:'',
          projectCode:'',
          createUser:'',
          createUserNickName:'',
          userNickName:'',
          planType:'',
          taskMembers:''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建计划'
        },
        uploadDialog:false,
        searchForm: {
          planCode: '',
          planDesc: '',
          planStatus:'',
          taskMembers:'',
          pageNum:'',
          pageRow:''
        },
        projectCodes:[],
        fileList: [],
        file:[],
        users:[]
      }
    },
    created() {
      this.getList();
      if (this.hasPerm('plan:add')) {
        this.getProjectCodes();
      }

    },
    computed: {
      ...mapGetters([
        'planId'
      ])
    },
    methods: {
      //弹出上传界面
      showInsertDialog(){
          this.uploadDialog = true;
      },
      //批量插入
      startUpload(){
        let fileObject = this.$refs.upload.uploadFiles[0].raw;
        console.log(fileObject);
        let formData = new FormData();
        formData.append('excel',fileObject)
        axios({
          url: 'http://127.0.0.1:8082/projectPlan/insertPlanBatch',
          method: 'post',
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': store.getters.token
          },
        }).then((data) => {
            this.uploadDialog = false;
            this.listLoading = false;
            this.getList();
      });
      },

      handleChange(file){
        this.file = file;
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },

      //带条件的查询
      onSubmit(){
        this.searchForm.pageNum = this.listQuery.pageNum;
        this.searchForm.pageRow = this.listQuery.pageRow;
        this.listLoading = true;
        this.api({
          url:"/projectPlan/getPlanListBySearch",
          method:"post",
          data:this.searchForm
        }).then(data=>{
          this.listLoading = false;
          this.list = data.data.records;
          this.totalCount = data.totalCount;
        })
      },

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

      //查询列表
      getList() {
        this.listLoading = true;
        this.api({
          url: "/projectPlan/getPlanListPage",
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
        this.tempPlan.projectCode = "";
        this.tempPlan.projectId = "";
        this.tempPlan.planCode = "";
        this.tempPlan.planDays = "";
        this.tempPlan.taskMembers = "";
        this.tempPlan.planDesc = "";
        this.tempPlan.planType = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },

      showUpdate($index) {
        let plan = this.list[$index];
        this.tempPlan.projectCode = plan.projectCode;
        this.tempPlan.projectId = plan.projectId;
        this.tempPlan.planDays = plan.planDays;
        this.tempPlan.createUserNickName = plan.createUserNickName;
        this.tempPlan.planCode = plan.planCode;
        this.tempPlan.planType = plan.planType;
        this.tempPlan.planStatus = plan.planStatus;
        this.tempPlan.planId = plan.planId;
        this.tempPlan.taskMembers = plan.taskMembers;
        this.tempPlan.planDesc = plan.planDesc;

        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },

      createPlan() {
        this.tempPlan.createUser = store.getters.userId;
        //添加新用户
        this.api({
          url: "/projectPlan/insertPlan",
          method: "post",
          data: this.tempPlan
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },

      updatePlan() {
        //修改计划信息
        let _vue = this;
        this.api({
          url: "/projectPlan/updatePlan",
          method: "post",
          data: this.tempPlan
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.planId === this.tempPlan.planId) {
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

      removePlan($index) {
        let _vue = this;
        this.$confirm('确定删除此项目?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let plan = _vue.list[$index];
          console.log(plan);
          _vue.api({
            url: "/projectPlan/deletePlanById",
            method: "post",
            data: plan
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },

    }
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
