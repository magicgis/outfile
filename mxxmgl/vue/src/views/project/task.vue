<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('task:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" icon="plus" v-if="hasPerm('task:add')" @click="showInsertDialog">批量导入
          </el-button>
        </el-form-item>
      </el-form>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline" v-if="hasPerm('task:list')">
        <el-form-item label="任务编号">
          <el-input v-model="searchForm.taskCode" clearable placeholder="任务编号"></el-input>
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input v-model="searchForm.taskOutCome" clearable placeholder="处理结果"></el-input>
        </el-form-item>
        <el-form-item label="任务完成者">
          <el-input v-model="searchForm.finishUser" clearable placeholder="任务完成者"></el-input>
        </el-form-item>
        <el-form-item label="完成天数">
          <el-input v-model="searchForm.finishDays" clearable placeholder="完成天数"></el-input>
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
            <el-form-item label="任务编号:">
              <span>{{ props.row.taskCode }}</span>
            </el-form-item>
            <el-form-item label="创建者:">
              <span>{{ props.row.createUserNickName }}</span>
            </el-form-item>
            <el-form-item label="计划编号:">
              <span>{{ props.row.planCode }}</span>
            </el-form-item>
            <el-form-item label="完成天数:">
              <span>{{ props.row.finishDays }}</span>
            </el-form-item>
            <el-form-item label="完成人:">
              <span>{{ props.row.finishUser }}</span>
            </el-form-item>
            <el-form-item label="创建时间:">
              <span>{{ pTime(props.row.createTime) }}</span>
            </el-form-item>
            <el-form-item label="完成结果:">
              <span>{{ props.row.taskOutcome }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="任务编号" prop="taskCode" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="计划编号" prop="planCode" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="处理结果" prop="taskOutcome" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="完成天数(天)" prop="finishDays" style="width: 30px;" ></el-table-column>
      <el-table-column align="center" label="任务完成者" prop="finishUser"  style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" sortable width="170">
        <template scope="scope">
            {{pTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column align="left" label="管理" width="220" v-if="hasPerm('task:update') || hasPerm('task:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('task:update')">修改</el-button>
          <el-button type="danger" icon="delete" v-if="hasPerm('task:delete')"
                     @click="removeTask(scope.$index)">删除
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
      <el-form class="small-space" :model="tempTask" label-position="left" label-width="110px"
               style='width: 350px; margin-left:50px;'>
        <el-form-item label="计划编号" required >
          <el-input type="text" v-model="tempTask.planCode" clearable @blur="checkPlanCode">
          </el-input>
        </el-form-item>
        <el-form-item label="任务编号" required >
          <el-input type="text" v-model="tempTask.taskCode" clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="完成天数" required >
          <el-input type="text" v-model="tempTask.finishDays" clearable placeholder="可以带小数 如：3.5天">
          </el-input>
        </el-form-item>
        <el-form-item label="完成时间" required >
          <el-date-picker
            v-model="tempTask.finishTime"
            type="datetime"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建者" required v-if="dialogStatus=='update'">
          <el-input type="text" :disabled="true" v-model="tempTask.createUserNickName" >
          </el-input>
        </el-form-item>
        <el-form-item label="任务负责人" required >
          <el-input type="text"  v-model="tempTask.finishUser" clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="处理结果" required>
          <el-input type="textarea"
                    :autosize="{ minRows: 4, maxRows: 10}"
                    v-model="tempTask.taskOutcome" clearable>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createTask">创 建</el-button>
        <el-button type="primary" v-else @click="updateTask">修 改</el-button>
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
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建计划'
        },
        uploadDialog:false,
        searchForm: {
          projectName: '',
          projectDesc: '',
          projectStatus:'',
          createUser:'',
          pageNum:'',
          pageRow:''
        },
        fileList: [],
        file:[],
        tempTask:{
          taskCode:'',
          taskId:'',
          taskOutcome:'',
          finishUser:'',
          finishTime:'',
          finishDays:'',
          createTime:'',
          updateTime:'',
          planCode:'',
          planId:''
        },
      }
    },
    created() {
      this.getList();


    },
    computed: {
      ...mapGetters([
        'taskId'
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
          url: 'http://127.0.0.1:8082/projectTask/insertTaskBatch',
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

      //验证计划代码是否存在
      checkPlanCode(){
        let _vue = this;
        if(this.tempTask.planCode ==='' || this.tempTask.planCode === null){
            return;
        }else{
          this.api({
              url:'/projectPlan/checkPlanCode',
              method:'post',
              params: {
                planCode:this.tempTask.planCode
              }
          }).then((data)=>{
              if(data.status === 0){
                this.tempTask.planId = data.data.id;
              }else{
                _vue.$message.error("计划编码不存在")
              }
          })
        }


      },

      //带条件的查询
      onSubmit(){
        this.searchForm.pageNum = this.listQuery.pageNum;
        this.searchForm.pageRow = this.listQuery.pageRow;
        this.listLoading = true;
        this.api({
          url:"/projectTask/getTaskListBySearch",
          method:"post",
          data:this.searchForm
        }).then(data=>{
          this.listLoading = false;
          this.list = data.data.records;
          this.totalCount = data.totalCount;
        })
      },

      //查询列表
      getList() {
        this.listLoading = true;
        this.api({
          url: "/projectTask/getTaskListPage",
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
        this.tempTask.taskCode = '';
        this.tempTask.taskOutcome = '';
        this.tempTask.finishUser = '';
        this.tempTask.finishDays = '';
        this.tempTask.finishTime = '';
        this.tempTask.planId = '';
        this.tempTask.planCode = '';
        this.tempTask.taskCode = '';
        this.tempTask.taskId = '';

        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },

      showUpdate($index) {
        let task = this.list[$index];
        this.tempTask.taskCode = task.taskCode;
        this.tempTask.taskOutcome =task.taskOutcome;
        this.tempTask.finishUser =task.finishUser;
        this.tempTask.finishDays =task.finishDays;
        this.tempTask.finishTime = task.finishTime;
        this.tempTask.planId = task.planId;
        this.tempTask.planCode = task.planCode;
        this.tempTask.taskCode = task.taskCode;
        this.tempTask.taskId = task.taskId;

        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },

      createTask() {
        this.tempTask.createUser = store.getters.userId;
        //添加新任务
        this.api({
          url: "/projectTask/insertOrupdateTask",
          method: "post",
          data: this.tempTask
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },

      updateTask() {
        //修改任务信息
        let _vue = this;
        this.api({
          url: "/projectTask/insertOrupdateTask",
          method: "post",
          data: this.tempTask
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.taskId === this.tempTask.taskId) {
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

      removeTask($index) {
        let _vue = this;
        this.$confirm('确定删除此任务?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let task = _vue.list[$index];
          console.log(task);
          _vue.api({
            url: "/projectTask/deleteProjectTaskById",
            method: "delete",
            data: task
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
