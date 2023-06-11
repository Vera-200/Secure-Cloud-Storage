<template>
  <div style="display: flex;justify-content: center;margin-top: 150px">

    <el-card style="width: 350px">
      <div slot="header" class="clearfix">
        <span>重置密码</span>
      </div>
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm"   :class="clearfix">
        <el-form-item label="新密码" prop="pass" >
          <el-col>
          <el-input type="password" v-model="ruleForm.newPass" @change="testPassword(ruleForm.newPass)" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-col>
            <el-input type="password" v-model="ruleForm.checkPass" @change="testPassword(ruleForm.checkPass)" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item>
          <el-col>
          <el-button type="info" @click="submitForm('ruleForm')">重置密码</el-button>
          </el-col>
        </el-form-item>
      </el-form>

    </el-card>

  </div>


</template>

<script>
export default {

  name: 'Changepwd',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.newPass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };

    return {
      ruleForm: {
        newPass: '',
        checkPass: ''
      },
      user:{
        userPassword:''
      },
      rules: {
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.user.userPassword = this.ruleForm.newPass;
          //test
          //console.log(this.user);
          //this.$router.push({name: 'Login'})
          //
          this.$axios.post("/user/changepwd",this.user)
          	.then(res=>{
          			console.log(res);//test
                  if(res.data==='success'){
                    this.$message({
                      message: '修改密码成功',
                      type: 'success',
                      center: true
                    });
                    this.$router.push({name: 'Login'})
                  }
                  else {
                    this.$message({
                      message: '修改密码失败',
                      type: 'error',
                      center: true
                    });
                  }
            })
        } else {
          console.log(this.ruleForm.checkPass);
          return false;
        }
      });
    },
    testPassword(psw){

      var reg=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$/;//包含大小写和数字.
      if(!reg.test(psw)){

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


  }
}
</script>

<style>
</style>
