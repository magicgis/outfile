<template>
    <div id="mypie">
      <ve-pie :data="chartData"></ve-pie>
    </div>
</template>

<script>
     import VePie from 'v-charts/lib/pie'
      export default {
        name: "mypie",
        data(){
          return{
            chartData: {
              columns: ['状态', '所占比例'],
              rows: [
                { '状态': '新建', '所占比例': 1393 },
                { '状态': '立项', '所占比例': 3530 },
                { '状态': '建设中', '所占比例': 2923 },
                { '状态': '验收', '所占比例': 1723 },
              ]
            },
            projects:[],
            searchForm:{
              projectStatus:''
            }
          }
        },
        created(){

       },
        methods:{



        },
        components:{
          VePie
      }
    }
</script>

<style scoped>

</style>
