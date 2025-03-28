<template >
    <div class="app">
        <div class="top">
            <h1><span v-for="(char, index) in 'Hello,World!'" :key="index" class="char">{{ char }}</span></h1>
            <hr>
        </div>
        <div class="main">
            <div class="form">
                <div>
                    <button @click="clickLogin" id="Login" :class="{ active: state=='Login'}">登录</button>
                    <button @click="clickRegister" id="Register" :class="{ active: state=='Register'}">注册</button>
                </div>
                <div v-if="state=='Login'">
                    <login></login>
                </div>
                <div v-if="state=='Register'">
                    <register></register>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import { ref } from 'vue';
    import Login from './components/Login.vue';
    import Register from './components/Register.vue';

    export default {
        components: {Login, Register},
        name: "App"
    }
</script>

<script lang="ts" setup>
    let state = ref("Login")

    function clickLogin() {
        state.value = "Login"
    } 

    function clickRegister() {
        state.value = "Register"
    }
</script>

<style scoped>
    .app {
        margin: 0;
        background-color: #ffffff;
        height: 100vh;
        overflow: hidden;
    }
    .top {
        margin: 0;
        padding: 10px;
        height: 10%;
    }
    .main {
        display: flex;
        background-color: transparent;
        margin: 0%;
        height: 80%;
    }
    .form {
        width: 400px;
        height: 500px;
        background-color: transparent;
        box-shadow: 0 0 10px;
        border-radius: 10px;
        padding: 20px;
        margin: auto;
    }
    button {
        font-size: 20px;
        padding: 0.5rem 1rem;
        height: 60px;
        width: 120px;
        background-color: rgb(208, 237, 248);
        border-color: transparent;
        transition: 0.5s;
    }
    button#Login {
        margin-left: 80px;
    }
    button:hover {     
        cursor: pointer;
        background-color: rgb(0, 183, 255);
    }
    .active {
        background-color: rgb(0, 183, 255);
    }
    .char {
        transition: 0.5s;
    }
    .char:hover {
        font-size: 50px;
    }
</style>
