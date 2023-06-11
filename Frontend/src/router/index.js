import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Main from '@/views/Main.vue'
import Manager from '@/views/Manager.vue'
import Changepwd from '@/views/Changepwd.vue'
import Verify from '@/views/Verify.vue'
import Group from '@/views/Group.vue'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    /* {
      path: '/user/login',
      name: 'Login',
      component: Login
    }, */
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/main',
      name: 'Main',
      component: Main
    },
    {
      path: '/main/manager',
      name: 'Manager',
      component: Manager
    },

    {
      path: '/changepwd',
      name: 'Changepwd',
      component: Changepwd
    },
    {
      path: '/verify',
      name: 'Verify',
      component: Verify
    },
    {
      path: '/Group',
      name: 'Group',
      component: Group
    },


    ]
})
