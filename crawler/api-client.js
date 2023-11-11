const axios = require('axios')

class ApiClient {
    constructor(){
        
        //HTTP 클라이언트
        const client = axios.create({
            baseURL: 'API서버주소'
        })
        // 응답 객체 대신 data필드만 반환
        client.interceptors.response.use(response => {
            return response.data
        })
        // client를 멤버 변수로 설정
        this.client = client;
    }

    async postPracticeRoom(data){
        return await this.client.post('/practice-rooms', data)
    }

    async postRooms(data){
        return await this.client.post('/rooms', data)
    }

    async postReservation(data){
        return await this.client.post('/reservation', data)
    }
}