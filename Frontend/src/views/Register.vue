<template>
  <div>
    <div style="display: flex;justify-content: center;margin-top: 150px;">
      <el-card style="width: 350px">
        <div slot="header" class="clearfix">
          <span>注册</span>
        </div>
        <table style="align: center;">
          <tr>
            <td>用户名</td>
            <td>
              <el-input v-model="user.userName" placeholder="请输入用户名"></el-input>
            </td>
          </tr>
          <tr>
            <td>邮箱</td>
            <td>
              <el-input v-model="user.userEmail" @change="testEmail" placeholder="请输入邮箱"></el-input>
            </td>
          </tr>
          <tr>
            <td>密码</td>
            <td>
              <el-input type="password" v-model="user.userPassword" @change="testPassword" placeholder="请输入密码"></el-input>
            </td>
          </tr>
          <tr>
                      <td>确认密码</td>
                      <td>
                        <el-input type="password" v-model="user.userPassword2" placeholder="请再次输入密码" @change="checkPassword" ></el-input>
                      </td>
                    </tr>
          <tr>
            <td colspan="2">
              <el-button style="width: 300px" type="primary" @click="checkEmpty">注册</el-button>
            </td>
          </tr>
        </table>
        <div>
          <el-button type="text" @click="goLogin">已有账号？去登录叭</el-button>
        </div>
        <div id="message"></div>
      </el-card>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return{
        user:{
          userName:'',
          userPassword:'',
		      userEmail:'',
          userPassword2:'',
        }
      }
    },
    methods:{
      goLogin(){
        this.$router.push({name: 'Login'})
      },
      doRegister(){
        this.$axios.post("/user/register",this.user)
          .then(res=>{
            console.log(res);
            if(res.data==='registerSuccess'){
              this.$message({
                message: '注册成功',
                type: 'success',
                center: true
              });
              this.$router.push({name: 'Login'})
            }
            else if(res.data==='emailRepeat'){
              this.$message({
                message: '该邮箱已注册',
                type: 'error',
                center: true
              });
            }
            else if(res.data==='emailIllegal'){
              this.$message({
                message: '邮箱不存在',
                type: 'error',
                center: true
              });
            }
            else if(res.data==='nameRepeat'){
                this.$message({
                  message: '用户名已存在',
                  type: 'error',
                  center: true
                });
            }
            else{
              this.$message({
                message: '注册失败',
                type: 'error',
                center: true
              });
            }
          })
      },
      testEmail(){//判断email是否合理
        var x=this.user. userEmail;
        var reg=/^\w+@[a-zA-Z0-9]{2,10}(?:\.[a-z]{2,4}){1,3}$/;

        if(!reg.test(x)){
        	//document.getElementById("message").innerHTML="邮箱不合理";
          this.$message({
          	message: "邮箱不合理",
          	type: "error",
          	center: true
          });
        }
        else{
          //document.getElementById("message").innerHTML="邮箱合理";
          this.$message({
          	message: "邮箱合理",
          	type: "success",
          	center: true
          });
        }

      },

      testPassword(){
        var x=this.user.userPassword;
        var reg=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$/;//包含大小写和数字.
        if(!reg.test(x)){
         
          this.$message({
          	message:"密码应包含大小写字母和数字",
          	type: "error",
          	center: true
          });
        }
        else{
          
          this.$message({
          	message: "密码合理",
          	type: "success",
          	center: true
          });
        }
      },

      checkPassword(){
              var x=this.user.userPassword;
              var y=this.user.userPassword2;
              if(!(y===x)){
               
                this.$message({
                  message:"密码不一致",
                  type: "error",
                  center: true
                });
              }
              else {
               
                this.$message({
                  message:"密码一致",
                  type: "success",
                  center: true
                });
              }
      },
      checkEmpty(){
                  if (this.user.userName===''){
                    this.$message({
                      message:"请输入用户名",
                      type: "error",
                      center: true
                    });
                  }
                  else if(this.user.userEmail===''){
                    this.$message({
                      message:"请输入邮箱",
                      type: "error",
                      center: true
                    });
                  }
                  else if (this.user.userPassword===''){
                    this.$message({
                      message:"请输入密码",
                      type: "error",
                      center: true
                    });
                  }
                  else if (this.user.userPassword2===''){
                    this.$message({
                      message:"请再次输入密码",
                      type: "error",
                      center: true
                    });
                  }
                  else  {
                    this.doRegister()
                  }
      },
    }
  }
</script>
