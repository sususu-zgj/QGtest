<template>
    <form @submit.prevent="submit">
        <div>
            <input type="text" v-model="username" placeholder="用户名" required>
        </div>
        <div>
            <input type="text" v-model="password" placeholder="密码" required>
            <button id="forget" @click="forget" type="reset">忘记密码</button>
        </div>
        <div>
            <button type="submit">登录</button>
        </div>
    </form>
</template>

<script lang="ts">
    export default {
        name: "Login"
    }
</script>

<script lang="ts" setup>
import { ref } from 'vue';
    let username = ref("")
    let password = ref("")

    function submit() {
        alert("登录成功！")
    }

    function forget() {
        alert("重置密码")
    }
</script>

<style scoped>
    button {
        padding: 8px;
        background-color: #bfd3e0;
        border-color: transparent;
        border-radius: 10px;
        margin-left: 100px;
        margin-top: 20px;
        width: 192px;
        transition: background-color 0.5s;
    }
    button:hover {
        cursor: pointer;
        background-color: rgb(121, 196, 234);
    }
    input {
        width: 300px;
        height: 32px;
        margin-left: 50px;
        margin-top: 40px;
        padding: 0.5rem;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
    #forget {
        margin: 0;
        background: transparent;
    }
</style>
