<template>
	<div>

	  <div style="margin-bottom: 25px">
	    <el-container >
	      <el-header>
	        <tr>

	          <td style="width: 400px" ><font size=20px >Hi,{{User.userName}}!</font> </td>
	          <td >
	            <el-input  placeholder="search" prefix-icon="el-icon-search" style="width: 500px " ></el-input>

	          </td>
            <td style="width: 100px" align=right><el-button @click="registerGroup">创建群组</el-button></td>

            <td style="width: 100px" align=right>
              <el-popover
                placement="right"
                width="400"
                trigger="click"
                >

                <div>
                  <div>
                    <el-form :model="form">
                      <el-form-item label="请输入群组号" :label-width="formLabelWidth">
                        <el-input v-model="form.groupId" autocomplete="off"></el-input>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div>
                    <el-button plain @click="addGroup(form.groupId)">确认</el-button>
                  </div>

                </div>
                <el-button slot="reference"  >加入群组</el-button>

              </el-popover>


            </td>

            <td style="width: 100px" align=right><el-button @click="logout">退出登录</el-button></td>
	          <td style="width: 100px" align=right>
	            <el-popover
                placement="right"
                width="400"
                trigger="click">

                <div class="block">
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
                <el-button plain @click="apply(myOwnStorage)" >确认</el-button>


                <el-button slot="reference" >申请内存</el-button>
              </el-popover>


	          </td>
	          <td style="width: 100px" align=right><el-button type="info" plain @click="goManager" v-if="User.userType==='admin'">管理员界面</el-button></td>
	        </tr>
	      </el-header>

	    </el-container>
	  </div>



	  <router-view></router-view>

    <el-container>

      <el-radio-group v-model="tabPosition" style="margin-bottom: 30px;"></el-radio-group>

        <el-tabs :tab-position="tabPosition" style="height: 800px;">
          <el-tab-pane label="文件列表">


              <template>
                <el-progress :text-inside="true" :stroke-width="24" :percentage="(User.userUsage/User.userStorage).toFixed(4)*100" :color="customColors" ></el-progress>
              </template>

              <div style="margin-top: 10px">
                {{User.userUsage}} /  {{User.userStorage}} MB
              </div>



              <div style="margin-bottom:25px; margin-top: 10px">
                <label class="label">
                  <input type="file" ref="file" @change="uploadFile()"> 点击上传
                  <span class="before" :style="{width:pre+'%'}"></span>
                </label>
              </div>



            <!-- 表格 -->
            <el-table
              :data="fileList"
              border
              style="width: 100%">
              <el-table-column
                type="index"
                label="序号"
                width="100px"
                align="center">
              </el-table-column>

              <el-table-column
                prop="fileName"
                label="文件名"
                width="500px"
                align="center"
              >
                <template slot-scope="scope">
                  {{scope.row.fileName}}.{{scope.row.fileType}}
                </template>
              </el-table-column>

              <el-table-column
                prop="fileSize"
                label="文件大小(MB)"
                width="120px"
                align="center"
              >
              </el-table-column>

              <el-table-column
                prop="actions"
                label="操作"
                width="200px"
                align="center"
              >
                <div align="center" slot-scope="scope">
                  <el-button type="primary" icon="el-icon-download" circle @click="downloadFile(scope.row.fileName,scope.row.fileType)"></el-button>

                  <el-popover
                    placement="right"
                    width="400"
                    trigger="click">
                    <el-table :data="groupData">
                      <el-table-column width="150" property="groupId" label="群组号" align="center"></el-table-column>
                      <el-table-column width="150" property="actions" label="" align="center">
                        <div align="center" slot-scope="grp">
                          <!-- <el-button type="warning" icon="el-icon-position" circle @click="makesureShareFile(grp.row.groupId)"></el-button> -->

                        </div>
                        <el-button type="warning" icon="el-icon-position" circle @click="downloadFile(scope.row.fileName,scope.row.fileType)"></el-button>

                      </el-table-column>
                    </el-table>


                   <!-- <el-button type="warning"  slot="reference" @click="shareFile(scope.row.fileName)" icon="el-icon-share" circle></el-button> -->
                  </el-popover>

                  <el-button type="danger" icon="el-icon-delete" circle @click="deleteFile(scope.row.fileName,scope.row.fileType,scope.row.fileId)"></el-button>
                </div>
              </el-table-column>

            </el-table>
          </el-tab-pane>
          <el-tab-pane label="我创建的群组">
            <el-table
              :data="manageList"
              border
              style="width: 100%">
              <el-table-column
                     prop="groupId"
                     label="群组号"
                     width="100px"
                     align="center">

               </el-table-column>

              <el-table-column
                prop="groupUsage,groupStorage"
                label="空间使用"
                width="400px"
                align="center">
                <template slot-scope="scope">
                   <el-progress :text-inside="true" :stroke-width="26" :percentage="(scope.row.groupUsage/scope.row.groupStorage).toFixed(4)*100"></el-progress>
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
                prop="a"
                label="申请扩容"
                width="130px"
                align="center"
                >
                <el-popover
                  placement="center"
                  width="400"
                  trigger="click">

                  <div class="block">
                    <span class="demonstration" ></span>
                    <el-slider  v-model="myOwnStorage" max=1000></el-slider>
                  </div>


                  <el-button slot="reference" type="success" icon="el-icon-top" circle @click="apply" ></el-button>
                </el-popover>



              </el-table-column> -->

              <el-table-column
                prop=""
                label="进入群组"
                width="200px"
                align="center"
                >
                <template slot-scope="scope">
                   <el-button type="info" icon="el-icon-plus" circle @click="goGroup(scope.row.groupId)"></el-button>
                </template>


              </el-table-column>


            </el-table>
          </el-tab-pane>
          <el-tab-pane label="我加入的群组">
            <el-table
              :data="groupList"
              border
              style="width: 100%">
              <el-table-column
                     prop="groupId"
                     label="群组号"
                     width="100px"
                     align="center">

               </el-table-column>

                <el-table-column
                    prop="groupManagerId"
                    label="群组管理员"
                    width="150px"
                    align="center"
                  >

                </el-table-column>



              <el-table-column
                prop="groupUsage,groupStorage"
                label="空间使用"
                width="400px"
                align="center">
                <template slot-scope="scope">
                   <el-progress :text-inside="true" :stroke-width="26" :percentage="(scope.row.groupUsage/scope.row.groupStorage).toFixed(4)*100"></el-progress>
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


              <el-table-column
                prop="groupId"
                label="进入群组"
                width="200px"
                align="center"
                >
                <template slot-scope="scope" >


                <div align="center"><el-button type="info" icon="el-icon-plus" circle @click="goGroup(scope.row.groupId)"></el-button></div>
                  </template>
              </el-table-column>


            </el-table>
          </el-tab-pane>

        </el-tabs>

    </el-container>

  </div>
</template>


<script>
import axios from 'axios';
import qs from 'qs';
  export default {
    name: 'Main',
    data(){
      return{
        User:{
          userName:'',
          userStorage: 0,
          userUsage:0,
          userType:''
        },
       // shareFileName:'',
        dialogFormVisible :false,
        formLabelWidth : '120px',
        form:{
          groupId:''
        },
        Apply:{
          applySize:100,
        },//申请扩容空间
        File:{
          fileName:'',
          fileType:'',
          fileId:-1
        },//删除或下载的文件
        Group:{
          groupId:0,
        },
        search:'',
        tabPosition: 'left',
        myOwnStorage: 500,
        mySpentStorage: 50,
        percentage:20,

        customColor: '#409eff',
        customColors: [
          {color: '#43d400', percentage: 50},
          {color: '#f0cc00', percentage: 80},
          {color: '#d51717', percentage: 100}
        ],

        pre:0,

        groupData:[],

		fileList:[],
		groupList:[],
		manageList:[],


      };

    },
	mounted:function(){
	    this.getFilelist();//需要触发的函数
		  this.getGroupList();
		  this.getManageGroup();
      this.getUser();
	    },


  methods:{
    getUser(){
      this.$axios.get("/user/getusername")
      	.then(res=>{
      		console.log(res);
          console.log(res.status);
          if(res.status === 202){
            this.$router.push({name: 'Login'})
          }
      	this.User.userName=res.data.userName;
      	this.User.userStorage=res.data.userStorage;
      	this.User.userUsage=res.data.userUsage;
      	this.User.userType=res.data.userType;
      	})
    },
    goGroup(groupId){

      this.$router.push({
        name: 'Group',
        params:{
          groupId:groupId
        }
      });q
      /* this.$axios.post("/group/registerGroup",)
      	.23343hen(res=>{
          if(res.data=="Fail"){
            this.$message({
            	message: "创建群组失败",
            	type: "error",
            	center: true
            });
          }
          else{
            this.getManageGroup();
            this.getGroupList();
            this.$message({
            	message: "群组id：" + res.data,
            	type: "success",
            	center: true
            });
    }
    }) */
   },
    registerGroup(){
    		  this.$axios.get("/group/registerGroup")
    		  	.then(res=>{
              if(res.data=="Fail"){
                this.$message({
                	message: "创建群组失败",
                	type: "error",
                	center: true
                });
              }
              else{
                this.getManageGroup();
                this.getGroupList();
                this.$message({
                	message: "群组id：" + res.data,
                	type: "success",
                	center: true
                });
                console.log(res);
              }

    		  	})

    },

    addGroup(groupId){
          this.Group.groupId = groupId;
    		  this.$axios.post("/group/addGroup",this.Group)
    		  	.then(res=>{

              console.log(res)
    		  		if(res.data =='Success'){
    		  		  this.$message({
    		  		    message: "加入成功",
    		  		    type: 'success',
    		  		    center: true
    		  		  });
                this.getGroupList()
    		  		}
    		  		else if(res.data == 'noGroup'){
    		  		  this.$message({
    		  		    message: "群组不存在",
    		  		    type: 'error',
    		  		    center: true
    		  		  });
    		  		}
    		  		else {
    		  		  this.$message({
    		  		    message: "申请失败",
    		  		    type: 'error',
    		  		    center: true
    		  		  });
    		  		}

    		  	})
    },

    dosearch(){
      alert(JSON.stringify(this.search))
    },
	getGroupList(){
			  this.$axios.get("/group/list")
			  	.then(groupList=>{
			  		console.log(groupList);
					this.groupList=groupList.data;
			  	})
	},
	getManageGroup(){
			  this.$axios.get("/group/manageList")
			  	.then(groupList=>{
			  		console.log(groupList);
					this.manageList=groupList.data;
			  	})
	},

    getFilelist(){
      this.$axios.get("/file/list")
        .then(fileList=>{
          console.log(fileList);
          this.fileList=fileList.data;
        })
    },

    customColorMethod(percentage) {
      if (percentage < 30) {
        return '#909399';
      }
      else if (percentage < 70) {
        return '#e6a23c';
      }
      else {
        return '#67c23a';
      }
    },


    getFile(item) {
      console.log(item.file)
        this.file = item.file
      },
    goManager(){
      this.$router.push({name: 'Manager'})
    },
    apply(storage){
      console.log(storage);
      this.Apply.applySize=storage;
      this.$axios.post("/user/apply",this.Apply)
      .then(res=>{
        if(res.data=='applySuccess'){
          this.$message({
            message: '申请已提交',
            type: "success",
            center: true
          });
        }
        else if(res.data=='alreadyApply'){
          this.$message({
            message: '重复申请',
            type: "error",
            center: true
          });
        }
        else if(res.data=='alreadyFalse'){
          this.$message({
            message: '申请失败',
            type: "error",
            center: true
          });
        }

      })
    },
    logout() {

            this.$confirm('？？？', '确定退出登录吗', {
              confirmButtonText: '确定',
              }).then(() =>{

                  this.$axios.post("/user/logout")
                    .then(res=>{
                        if(res.data==='Logout'){
                          this.$message({
                            message: '退出成功',
                            type: 'success',
                            center: true
                          });
                          this.$router.push({name: 'Login'})
                        }
                    })

              })

        },

	  deleteFile(fileName, fileType,fileId){
      console.log("1111")
      this.File.fileName = fileName;
      this.File.fileType = fileType;
      this.File.fileId=fileId;
      console.log(this.File);
      this.$axios.post("/file/delete",this.File)
        .then(res=>{
            this.$message({
                  message: '删除成功',
                  type: 'success',
                  center: true
                });
        })

      this.$axios.get("/file/list")
        .then(fileList=>{
          console.log(fileList);
          this.fileList=fileList.data;
        })
      this.getUser();
	  },

	  uploadFile(){

	  	var that = this;
	  	var file = this.$refs.file.files[0];

	    if(!file){return} // 如果没有文件就返回
	  	// 拿到上传的文件
	  	var data = new FormData();
	  	// 创建一个表单数据
	  	data.append("file",file);
	  	console.log("ASas",data)
	  	// 把图片或文件添加到data
	  	this.$axios.post("/file/upload",
	  	data,
	  	// 第1个参数 url 第二参数 data数据，第三个是配置渲染，
	  	// onUploadProgress 当上传进度是
	  	{onUploadProgress:e=>{

	  		that.pre =Math.floor(e.loaded/e.total*100);
	  		// e.loaded 已经上传的字节数据，e.total 字节数据  转换为1-100的比例值 赋值个pre
	  	}}
	  	)
	  	.then(res=>{
	  		console.log(res);
	  		if(res.data==true){
	  			this.$message({
	  				message: '上传成功',
	  			 	type: 'success',
	  				center: true
	  			});
				this.$axios.get("/file/list")
					.then(fileList=>{
						console.log(fileList);
						this.fileList=fileList.data;
					})
          this.getUser();
	  		}

			else if(res.data='File already exists motherxxxxx'){
				this.$message({
					message: '文件已存在',
				 	type: 'success',
					center: true
				});
			}
	  		this.$refs.file.value="";
	  		// 清空表单数据
	  		this.pre = 0;
	  		// 清空上传进度数据

	  	})
	  },
	  downloadFile(fileName,fileType){
      this.File.fileName = fileName;
      this.File.fileType = fileType;

      console.log(this.File);
      this.$axios.post("/file/download",this.File,{responseType:'blob'})
        .then(
          res => {
            const url = window.URL.createObjectURL(new Blob([res.data])) //创建指向Blob对象的Url
            const link = document.createElement('a')//创建a标签
            link.style.display = 'none'//设置a标签的样式
            link.href = url//设置a标签链接地址
        //var fileName = res.headers.fn
            link.setAttribute('download', decodeURIComponent(fileName + '.' + fileType))//此处fileName也可以从后端返回的响应头里拿到
            document.body.appendChild(link)//把新建的a标签追加到body
            link.click()//触发单机事件
            URL.revokeObjectURL(link.href) // 释放URL对象
            document.body.removeChild(link)//移除a标签
            this.$message({
              message: '下载成功'+this.File,
              type: 'success',
              center: true
            });
          }
        )
	  },
   /* shareFile(fileName){
      this.shareFileName=fileName;
      this.$message({
        message: 'share成功'+ this.shareFileName,
        type: 'success',
        center: true
      });
    },
    makesureShareFile(fileName){
      this.shareFileName=fileName;
      this.$message({
        message: 'share成功'+ fileName,
        type: 'success',
        center: true
      });
    } */


    }
  }
</script>

<style>
</style>
