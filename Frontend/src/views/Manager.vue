<template>
  <div>
    <div style="margin-bottom: 25px">
    	    <el-container >
    	      <el-header>
    	        <tr>

    	          <td style="width: 300px" ><font size=20px >Hi,管理员!</font> </td>
    	          <td >
    	            <el-input  placeholder="search" prefix-icon="el-icon-search" style="width: 500px " ></el-input>
    	          </td>
                <td style="width: 100px" align=right><el-button @click="goMain">退出管理员界面</el-button></td>
              </tr>
            </el-header>
          </el-container >
        </div>

		<el-tabs :tab-position="tabPosition" style="height: 800px;">
		  <el-tab-pane label="用户列表">

        <el-table
          :data="userData"
          border
          style="width: 100%">
         <el-table-column
                 prop="userId"
                 label="序号"
                 width="100px"
                 align="center">
           </el-table-column>

            <el-table-column
                prop="userName"
                label="用户名"
                width="150px"
                align="center"
              >

            </el-table-column>



          <el-table-column
            prop="userUsage,userStorage"
            label="空间使用"
            width="500px"
            align="center">
            <template slot-scope="scope">
               <el-progress :text-inside="true" :stroke-width="26" :percentage="(scope.row.userUsage/scope.row.userStorage).toFixed(4)*100" :color="customColors"></el-progress>
            </template>



          </el-table-column>

          <el-table-column
            prop="userUsage"
            label="已用空间(M)"
            width="120px"
            align="center"
          >

          </el-table-column>

          <el-table-column
            prop="userStorage"
            label="总空间(M)"
            width="120px"
            align="center"
          >

          </el-table-column>

          <el-table-column
             prop="storageSetup"
             label="容量管理"
             width="150px"
             align="center"
             >

             <el-popover
               placement="right"
               width="400"
               trigger="click"
               slot-scope="scope">


               <div class="block" >
                   <span class="demonstration">拖动设置内存容量</span>
                   <el-slider
                     max=1000
                     v-model="myOwnStorage"
                     :step="100"
                     :format-tooltip="formatToolLip"
                     show-input
                     show-stops>
                   </el-slider>
               </div>

                 <el-button plain @click="setUserstorage(scope.row.userName,myOwnStorage)" >确认</el-button>



               <el-button slot="reference" type="success" icon="el-icon-setting" circle ></el-button>


             </el-popover>






           </el-table-column>
         <!-- <el-table-column
            prop="storageSetup"
            label="容量管理"
            width="200px"
            >
            <div align="center"><el-button type="success" icon="el-icon-setting" circle></el-button></div>

          </el-table-column> -->


        </el-table>
      </el-tab-pane>

		  <el-tab-pane label="群组列表">
        <el-table
          :data="groupData"
          border
          style="width: 100%">
          <el-table-column
                 type="index"
                 label="序号"
                 width="100px"
                 align="center">
          </el-table-column>

          <el-table-column
                 prop="groupId"
                 label="群组号"
                 width="100px"
                 align="center">

           </el-table-column>

          <el-table-column
            prop="groupUsage,groupStorage"
            label="空间使用"
            width="500px"
            align="center">
            <template slot-scope="scope">
               <el-progress :text-inside="true" :stroke-width="26" :percentage="(scope.row.groupUsage/scope.row.groupStorage).toFixed(4)*100" :color="customColors"></el-progress>
            </template>

          </el-table-column>

          <el-table-column
            prop="groupUsage"
            label="已用空间(M)"
            width="120px"
            align="center"
          >

          </el-table-column>

          <el-table-column
            prop="groupStorage"
            label="总空间(M)"
            width="120px"
            align="center"
          >

          </el-table-column>

         <!-- <el-table-column
            prop="storageSetup"
            label="容量管理"
            width="200px"
            >
            <div align="center"><el-button type="success" icon="el-icon-setting" circle></el-button></div>

          </el-table-column> -->

        </el-table>

      </el-tab-pane>

      <el-tab-pane label="消息列表">

        <el-table
          :data="applyData"
          border
          style="width: 100%">
          <el-table-column
                 type="index"
                 label="消息序号"
                 width="100px"
                 align="center">
          </el-table-column>

 <!--         <el-table-column
                 prop="applyID"
                 label="消息ID"
                 width="100px"
                 align="center">
          </el-table-column>
 -->

          <el-table-column
            prop="applyRequestId"
            label="申请者"
            width="302px"
            align="center"
          >

          </el-table-column>
<!--
          <el-table-column
            prop="space"
            label="空间使用"
            width="500px"
            align="center">
            <template slot-scope="scope">
              <el-progress :text-inside="true" :stroke-width="26" :percentage="scope.row.space" :color="customColors"></el-progress>
            </template> -->


<!--
          </el-table-column>

          <el-table-column
            prop="uspace"
            label="已用空间(M)"
            width="120px"
            align="center"
          >

          </el-table-column>
 -->
  <!--        <el-table-column
            prop="tspace"
            label="总空间(M)"
            width="120px"
            align="center"
          >

          </el-table-column> -->

          <el-table-column
            prop="applySize"
            label="申请扩容"
            width="120px"
            align="center"
          >

          </el-table-column>


          <el-table-column
            prop="a"
            label="容量管理"
            width="200px"
            align="center"
            scope-slop="scope"
            >
            <template slot-scope="scope">
              <el-button type="success" icon="el-icon-check" circle @click="agreeApply(scope.row.applySize,scope.row.applyRequestId)"></el-button>
              <el-button type="danger" icon="el-icon-close" circle @click="rejectApply(scope.row.applySize,scope.row.applyRequestId)"></el-button>

            </template>


          </el-table-column>


        </el-table>
      </el-tab-pane>

		</el-tabs>

  </div>
</template>

<script>

  export default {
    name: 'Manager',




	methods:{
    goMain(){
      this.$router.push({name: 'Main'});
    },
    setUserstorage(userName,storage){
      this.user.userName = userName;
      this.user.userStorage = storage;
      console.log(this.user.userStorage)
      this.$axios.post("/mana/setUserstorage",this.user)
        .then(res=>{
          console.log(res);
          if(res.data == 'reset succeeded'){
            this.$message({
            	message: '设置成功',
            	type: 'success',
            	center: true
            });
          }
          else if(res.data == 'error'){
            this.$message({
            	message: '小于用户已用空间大小',
            	type: 'error',
            	center: true
            });
          }
          else {
            this.$message({
            	message: '设置失败',
            	type: 'error',
            	center: true
            });
          }
          this.getUserData()
        })
    },
		checkFilelist(){
      index=0;
		},
		uploadFile(){

		},
		getUserData(){
                this.$axios.post("/mana/queryalluser")
                  .then(res=>{
                    console.log(res);
                   this.userData=res.data
                  })
		},
    getGroupData(){
                this.$axios.post("/mana/queryGroup")
                  .then(res=>{
                    console.log(res);
                   this.groupData=res.data
                  })
    },
		getApplyData(){
		        this.$axios.post("/mana/queryallapply")
		          .then(res=>{
		            console.log(res);
		           this.applyData=res.data
		          })
		},
    agreeApply(size,Id){
      this.Apply.applyRequestId= Id;
      this.Apply.applySize= size;
      console.log(size);
      this.$axios.post("/mana/agreeApply", this.Apply)
        .then(res=>{
          console.log(res);
          if(res.data == 'agree succeeded'){
            this.$message({
            	message: '扩容成功',
            	type: 'success',
            	center: true
            });
          }
          else {
            this.$message({
            	message: '扩容失败',
            	type: 'error',
            	center: true
            });
          }
          this.getApplyData()
          this.getUserData()
        })
    },
    rejectApply(size,Id){
      this.Apply.applyRequestId= Id;
      this.Apply.applySize= size;
      console.log(this.Apply);
      this.$axios.post("/mana/refuseApply", this.Apply)
        .then(res=>{
          console.log(res);
          if(res.data == 'refuse succeeded'){
            this.$message({
            	message: '已拒绝',
            	type: 'success',
            	center: true
            });
          }
          else {
            this.$message({
            	message: '未知错误',
            	type: 'error',
            	center: true
            });
          }
          this.getApplyData()
          this.getUserData()
        })
    }



	},

  mounted:function(){
        this.getUserData();//需要触发的函数
        this.getGroupData();
        this.getApplyData();
      },


    data() {
      return {
        myOwnStorage:0,
        tabPosition:'left',
        userData: [    ],
        groupData: [    ],
        applyData: [    ],
        user:{
          userName:'',
          userStorage:0,
        },
        Apply:{
          applyId:123,
          applySize:100,
          applyRequestId:14,
        },
        customColors: [
          {color: '#43d400', percentage: 50},
          {color: '#f0cc00', percentage: 80},
          {color: '#d51717', percentage: 100}
        ],
      }
    }
  }
</script>

<style>
</style>
