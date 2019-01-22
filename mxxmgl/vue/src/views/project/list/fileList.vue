<template>
  <div class="app-container">
    <div class="filter-container">

      <el-form :inline="true">
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('file:add')" @click="showInsertDialog">添加文件
          </el-button>
          <el-button type="danger" icon="plus" v-if="hasPerm('file:list')" @click="downloadBatch">批量下载
          </el-button>
        </el-form-item>
      </el-form>

      <!--<el-form :inline="true" :model="searchForm" class="demo-form-inline" v-if="hasPerm('file:list')">-->
        <!--<el-form-item label="文件名称">-->
          <!--<el-input v-model="searchForm.fileName" clearable placeholder="文件名称"></el-input>-->
        <!--</el-form-item>-->

        <!--<el-form-item label="创建人">-->
          <!--<el-select v-model="searchForm.createUser" clearable placeholder="请选择">-->
            <!--<el-option-->
              <!--v-for="item in users"-->
              <!--:key="item.createUser"-->
              <!--:label="item.userNickName"-->
              <!--:value="item.createUser">-->
            <!--</el-option>-->
          <!--</el-select>-->
        <!--</el-form-item>-->

        <!--<el-form-item>-->
          <!--<el-button type="primary" @click="onSubmit">查询</el-button>-->
        <!--</el-form-item>-->
      <!--</el-form>-->

    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row :default-sort = "{prop: 'createTime', order: 'ascending'}" @selection-change="handleSelectionChange">
      <el-table-column
        align="center"
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="文件名" prop="fileName" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="文件大小(字节：b)" prop="fileSize" style="width: 30px;"></el-table-column>
      <el-table-column align="center" label="项目编号" prop="projectCode" style="width: 30px;" width="100">
      </el-table-column>
      <el-table-column align="center" label="创建者" prop="userNickName" style="width: 30px;" width="100">
    </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" sortable width="170">
        <template scope = "scope">
          {{pTime(scope.row.createTime)}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="下载次数" prop="downloadTimes" style="width: 30px;"></el-table-column>
      <el-table-column align="left" label="管理" width="220" v-if="hasPerm('file:update')">
        <template slot-scope="scope">
          <el-button type="success" icon="edit" @click="downlodFile(scope.row.fileId,scope.row.fileName)">下载
          </el-button>
          <el-button type="danger" icon="delete" v-if="scope.row.deleteStatus== '1'"
                     @click="removeFile(scope.$index)">删除
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

    <el-dialog title="文件上传" :visible.sync="uploadDialog">
     <el-form>
       <el-form-item>
        <el-upload v-if="hasPerm('file:add')"
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
  import store from '../../../store'
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
          projectId:''//项目id
        },
        tempFile:{

        },
        dialogFormVisible: false,
        uploadDialog:false,
        searchForm: {
          fileName:'',
          fileId:'',
          userNickName:'',
          createUser:''
        },
        fileList: [],
        file:[],
        users:[],
        project:{
          projectId:''
        },
        projectCode:'',
        multipleSelection:[],
        fileIds:[]
      }
    },
    created() {
      this.getList();
      if (this.hasPerm('file:list')) {
        this.getUsers();
      }

    },
    computed: {
      ...mapGetters([
        'fileId'
      ])
    },
    methods: {
      //文件批量下载
      downloadBatch(){
          this.projectCode = store.getters.projectCode;
          if(this.fileIds.length == 0){
            this.$message.warning(`请选择文件之后再提交`);
          }else{
            axios({
              url:'http://127.0.0.1:8082/file/downloadBatch',
              // url:'http://172.31.250.2:8080/backend/file/downloadBatch',
              method:'post',
              data: this.fileIds,
              headers: {
                'Authorization': store.getters.token
              },
              responseType:'blob'
            }).then((data)=>{
              this.getList();
              //创建一个blob对象,file的一种
              console.log(data);
              let blob = new Blob([data.data]);
              let link = document.createElement('a');
              link.style.display = 'none';
              link.href = window.URL.createObjectURL(blob);
              //配置下载的文件名
              link.download = this.projectCode +'.zip';
              link.click();
              link.remove();
            })
          }
      },

      //表格复选框
      handleSelectionChange(val){
        this.multipleSelection = val;
        this.fileIds = [];
        if(val){
            val.forEach(item =>{
              this.fileIds.push(item.fileId)
            })
          }else{
            return;
          }
        console.log(this.fileIds);
      },

      //弹出上传界面
      showInsertDialog(){
        this.project.projectId = store.getters.projectId;
        // console.log(this.project.projectId);
        this.uploadDialog = true;
      },

      //文件下载
      downlodFile(fileId,fileName){
        console.log(fileId);
        // let _vue = this;
        // let file = _vue.list;
        // downloadTimes = downloadTimes+1;
        axios({
          url:'http://127.0.0.1:8082/file/downloadFile',
          // url:'http://172.31.250.2:8080/backend/file/downloadFile',
          method:'get',
          params:{
            id:fileId
          },
          headers: {
            'Authorization': store.getters.token
          },
          responseType:'blob'
        }).then((data)=>{
          this.getList();
          //创建一个blob对象,file的一种
          console.log(data);
          let blob = new Blob([data.data]);
          let link = document.createElement('a');
          link.style.display = 'none';
          link.href = window.URL.createObjectURL(blob);
          //配置下载的文件名
          link.download = fileName;
          link.click();
          link.remove();
        })
      },

      //文件上传
      startUpload(){
        if(this.project.projectId == "" ){
          this.$message.warning(`请选择所属项目再提交`);
          return;
        }
        let fileObject = this.$refs.upload.uploadFiles[0].raw;
        console.log(fileObject);
        let formData = new FormData();
        formData.append('file',fileObject)
        formData.append("projectId",this.project.projectId)
        axios({
          url: 'http://127.0.0.1:8082/file/insertFile',
          // url: 'http://172.31.250.2:8080/backend/file/insertFile',
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
          url:"/file/getFileListBySearch",
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
        this.listQuery.projectId = store.getters.projectId;
        this.listLoading = true;
        this.api({
          url: "/file/getFileListPage",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.data.records;
          this.totalCount = data.totalCount;
        })
      },

      //获取添加用户
      getUsers(){
        this.api({
          url:'/file/getUsers',
          method:'get'
        }).then((data)=>{
          this.listLoading = false;
          this.users = data.data;
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

      removeFile($index) {
        let _vue = this;
        this.$confirm('确定删除此项目?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let file = _vue.list[$index];
          console.log(file);
          _vue.api({
            url: "/file/deleteFileById",
            method: "delete",
            data: file
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
