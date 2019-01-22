<template>
    <div id="myhistogram">
      <ve-histogram :data="chartData"></ve-histogram>
    </div>
</template>

<script>
    import VeHistogram from 'v-charts/lib/histogram'
    export default {
        name: "myhistogram",
        data(){
          return{
            chartData: {
              columns: ['日期', '计划数量', '完成量', '完成率'],
              rows: [
                { '日期': '1/1', '计划数量': 1393, '完成量': 1093, '完成率': 0.32 },
                { '日期': '1/2', '计划数量': 3530, '完成量': 3230, '完成率': 0.26 },
                { '日期': '1/3', '计划数量': 2923, '完成量': 2623, '完成率': 0.76 },
                { '日期': '1/4', '计划数量': 1723, '完成量': 1423, '完成率': 0.49 },
                { '日期': '1/5', '计划数量': 3792, '完成量': 3492, '完成率': 0.323 },
                { '日期': '1/6', '计划数量': 4593, '完成量': 4293, '完成率': 0.78 }
              ]
            }
          }
        },
        created(){

        },
        methods:{

        },
        components:{
          VeHistogram
        }
    }
</script>

<style scoped>

</style>
