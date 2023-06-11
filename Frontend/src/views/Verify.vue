<template>
  <div style="display: flex;justify-content: center;margin-top: 150px">
    <el-card style="width: 350px">
      <div slot="header" class="clearfix">
        <span>忘记密码</span>
      </div>
      <el-form   :model="formLabelAlign"  >
        <el-form-item  >
          <el-input v-model="formLabelAlign.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item >
          <el-col >
            <el-input v-model="formLabelAlign.check" placeholder="请输入验证码"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col >
            <el-button type="info" @click="getCheck">获取验证码</el-button>
          <el-button type="info" @click="goChangepwd">验证</el-button>
            </el-col >
        </el-form-item>
      </el-form>


    </el-card>

  </div>


</template>

<script>
  export default {
    name: 'Verify',




	methods:{
    getCheck() {
      this.user.userEmail = this.formLabelAlign.email;
      console.log(this.user);
     this.$axios.post("/user/forgetpwd",this.user)
      	.then(res=>{
      			console.log(res);//test
              if(res.data==='success'){
                this.$message({
                  message: '验证码已发送',
                  type: 'success',
                  center: true
                });
              }
              else if(res.data==='not exist'){
                this.$message({
                  message: '邮箱不存在',
                  type: 'error',
                  center: true
                });
              }
        })
    },
    goChangepwd(){
      this.Code.sjs = this.formLabelAlign.check;
      console.log(this.Code);
      console.log(this.Code.sjs);
      //test
      //this.$router.push({name: 'Changepwd'})
     this.$axios.post("/user/verifynum",this.Code)
      	.then(res=>{
      			console.log(res);//test
              if(res.data==='success'){
                this.$message({
                  message: '验证成功',
                  type: 'success',
                  center: true
                });
                this.$router.push({name: 'Changepwd'})
              }
              else if(res.data==='false'){
                this.$message({
                  message: '验证码错误',
                  type: 'error',
                  center: true
                });
              }
        })

    }
	},


    data() {
      return {
        user: {
          userEmail:'',
          //check:'',
        },

        formLabelAlign: {
          email: '',
          check: ''
        },
        Code:{
          sjs:'',
         },
        customColors: [
          {color: '#f56c6c', percentage: 20},
          {color: '#e6a23c', percentage: 40},
          {color: '#5cb87a', percentage: 60},
          {color: '#1989fa', percentage: 80},
          {color: '#6f7ad3', percentage: 100}
        ],
      }
    }
  }
</script>

<style>
</style>
