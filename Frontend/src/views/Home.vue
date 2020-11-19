<template>
    <div class="home">
        <v-container class="pa-11">
            <v-row>
                <v-col
                        cols="12"
                        sm="4"
                        xs="4" v-for="photo in photos" :key="photo.id">
                    <v-card
                            class="mx-auto"
                            max-width="344"
                    >
                        <v-img
                                v-bind:src="`/api/i/${photo.id}.png`"
                                height="200px"
                        ></v-img>

                        <v-card-title>
                            {{photo.title}}
                        </v-card-title>

                        <v-card-actions>
                            <v-btn
                                    color="orange lighten-2"
                                    text
                                    @click="viewSingle(photo.id)"
                            >
                                View Full Image

                            </v-btn>

                            <v-spacer></v-spacer>

                            <v-btn
                                    icon
                            >
                            </v-btn>
                        </v-card-actions>

                    </v-card>

                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
    // @ is an alias to /src
    import axios from 'axios';

    export default {
        name: 'Home',
        data() {
            return {
                photos: []
            }
        },
        mounted() {
            let _this = this;
            axios.get('/api/i').then((body) => {
                _this.photos = body.data
            });
        },
        methods: {
            viewSingle(id) {
                this.$router.push({path: `/i/${id}`})
            }
        }
    }
</script>
