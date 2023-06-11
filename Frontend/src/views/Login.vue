<template>
  <div>
      <div style="display: flex;justify-content: center;margin-top: 150px">
        <el-card style="width: 350px">
          <div slot="header" class="clearfix">
            <span>登录</span>
          </div>
          <table>
            <tr>
              <td>用户名</td>
              <td>
                <el-input v-model="user.userName" placeholder="请输入用户名"></el-input>
              </td>
            </tr>
            <tr>
              <td>密码</td>
              <td>
                <el-input type="password" v-model="user.userPassword" placeholder="请输入密码" @keydown.enter.native="doLogin"></el-input>
                <!-- @keydown.enter.native="doLogin"当按下enter键的时候也会执行doLogin方法-->
              </td>
            </tr>
            <tr>
              <!-- 占两列-->
              <td colspan="2">
                <el-button style="width: 300px" type="primary" @click="doLogin">登录</el-button>
              </td>
            </tr>

          </table>
          <div>
            <el-button type="text" @click="goRegister">没有账号？点击这里注册叭</el-button>
          </div>
          <div>
            <el-button type="text" @click="goVerify">忘记密码</el-button>
          </div>
        </el-card>
      </div>
    </div>
</template>

<script>
  export default {
    name: 'Login',
    data() {
      return{
         user:{
          userName:'',
          userPassword:'',
           },

      }
    },
    methods:{

		doLogin(){
			this.$axios.post("/user/login",this.user)
				.then(res=>{
						console.log(res);//test
						if(res.data==='true'){
							this.$message({
								message: '登陆成功',
								type: 'success',
								center: true
							});

							this.$router.push({name: 'Main'})

						}
						else if(res.data==='noUser'){
							this.$message({
								message: '用户名不存在',
								type: 'error',
								center: true
							});
						}
						else if(res.data==='passwordFalse'){
							this.$message({
								message: '密码错误',
								type: 'error',
								center: true
							});
						}
				})
		},
		goRegister(){
			this.$router.push({name: 'Register'})
		},
    goVerify(){
    	this.$router.push({name: 'Verify'})

    }

    }
  }
</script>
