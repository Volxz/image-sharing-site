<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
    >
      <div class="d-flex align-center">
        <h2>
          Image Site
        </h2>
      </div>

      <v-spacer></v-spacer>
      <router-link to="/" class="mr-2 white--text">Home</router-link>
      <router-link to="/upload" class="mr-2 white--text">Upload</router-link>

    </v-app-bar>

    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>

export default {
  name: 'App',

  data: () => ({
    //
  }),
};
</script>
