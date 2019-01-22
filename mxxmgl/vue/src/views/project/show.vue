<template>
  <div id="show">
      <div id="show-top">
        <el-row >
          <el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12">
            <my-pie></my-pie>
          </el-col>
          <el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12">
            <my-histogram></my-histogram>
          </el-col>
        </el-row>
      </div>
      <div id = show-bottom>
        <el-row >
          <el-col :offset="1":xs="22" :sm="22" :md="22" :lg="22" :xl="22">
            <my-line></my-line>
          </el-col>
        </el-row>
      </div>
  </div>
</template>

<script>
    import MyLine from './charts/myline'
    import MyPie from './charts/mypie'
    import MyHistogram from './charts/myhistogram'
    export default {
        name: "show",
        data(){
          return{

          }
        },
       components:{
         MyLine,
         MyPie,
         MyHistogram
        },
        created(){

        },
        methods:{


        },
    }
</script>

<style lang="stylus">
#show{
  margin-top 20px;
}

</style>
