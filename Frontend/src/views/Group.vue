<template>
	<div>

	  <div style="margin-bottom: 25px">
	    <el-container >
	      <el-header>
	        <tr>

	          <td style="width: 400px" ><font size=20px >Hi,{{this.$route.params.groupId}}!</font> </td>
	          <td >
	            <el-input  placeholder="search" prefix-icon="el-icon-search" style="width: 500px " ></el-input>
	          </td>


            <td style="width: 100px" align=right><el-button @click="logout">返回</el-button></td>
	        </tr>
	      </el-header>

	    </el-container>
	  </div>


    <el-container>

      <el-radio-group v-model="tabPosition" style="margin-bottom: 30px;"></el-radio-group>

        <el-tabs :tab-position="tabPosition" style="height: 800px;">
          <el-tab-pane label="文件列表">

              <div style="margin-bottom:25px; margin-top: 10px">
                <label class="label">
                  <input type="file" ref="file" @change="uploadFile()"> 点击上传
                  <span class="before" ></span>
                </label>
              </div>



            <!-- 表格 -->
            <el-table
              :data="groupFileList"
              border
              style="width: 100%">
              <el-table-column
                type="index"
                label="序号"
                width="100px"
                align="center">
              </el-table-column>

              <el-table-column
                prop="fileId"
                label="文件Id"
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
                  <el-button type="primary" icon="el-icon-download" circle @click="downloadFile(scope.row.fileName,scope.row.fileType,scope.row.fileId)"></el-button>
                  <el-button type="danger" icon="el-icon-delete" circle @click="deleteFile(scope.row.fileName,scope.row.fileType,scope.row.fileId)"></el-button>
                </div>
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
    name: 'Group',
    data(){
      return{
        userName:'groupId',
        shareFileName:'',
        dialogFormVisible :false,
        formLabelWidth : '120px',
        form:{
          groupId:''
        },

        File:{
          fileName:'',
          fileType:'',
          fileId: -1,
        },//删除或下载的文件
       Group:{
          groupId:this.$route.params.groupId,
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


        groupData:[],

		groupFileList:[],
		groupList:[],
		manageList:[],


      };

    },
	mounted:function(){
	    this.getFilelist();//需要触发的函数

	    },


  methods:{


    dosearch(){
      alert(JSON.stringify(this.search))
    },
	/* getGroupList(){
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
	}, */

    getFilelist(){

      this.$axios.post("/group/filelist",this.Group)
        .then(fileList=>{
          console.log(fileList);
          this.groupFileList=fileList.data;
        })
    },



    logout() {
        this.$router.push({name: 'Main'})

    },

	  deleteFile(fileName, fileType,fileId){
      this.File.fileName = fileName;
      this.File.fileType = fileType;
      this.File.fileId = fileId;
      console.log(this.File);
      this.$axios.post("/group/delete",this.File)
        .then(res=>{
           if(res.data == "failed1"){
              this.$message({
              	message: '删除失败1',
               	type: 'error',
              	center: true
              });
            }
            else if(res.data == "failed2"){
              this.$message({
              	message: '删除失败2',
               	type: 'error',
              	center: true
              });
              this.getFilelist();
            }
            else if(res.data == "failed3"){
              this.$message({
              	message: '删除失败3',
               	type: 'error',
              	center: true
              });
              this.getFilelist();
            }
            else {
              this.$message({
              	message: '删除成功',
               	type: 'success',
              	center: true
              });
              this.getFilelist();
            }
        })


	  },

	  uploadFile(){

	  	var that = this;
	  	var file = this.$refs.file.files[0];

	    if(!file){return} // 如果没有文件就返回
	  	// 拿到上传的文件
	  	var data = new FormData();
	  	// 创建一个表单数据
	  	data.append("file",file);
      //data.append("groupId",this.Group.groupId)

	  	console.log("ASas",data)
	  	// 把图片或文件添加到data
	  	this.$axios.post("/group/upload",
	  	data)
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
						this.groupFileList=fileList.data;
					})
          this.getFilelist();
	  		}

			else if(res.data='already exist'){
				this.$message({
					message: '文件已存在',
				 	type: 'success',
					center: true
				});
			}
      else if(res.data='storage is not enough'){
      	this.$message({
      		message: '存储空间不足',
      	 	type: 'success',
      		center: true
      	});
      }
	  		this.$refs.file.value="";
	  		// 清空表单数据


	  	})
	  },
	  downloadFile(fileName,fileType,fileId){
      this.File.fileName = fileName;
      this.File.fileType = fileType;
      this.File.fileId = fileId;

      console.log(this.File);
      this.$axios.post("/group/download",this.File,{responseType:'blob'})
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


    }
  }
</script>

<style>
</style>
