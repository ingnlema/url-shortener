import http from 'k6/http';
import { check, sleep } from 'k6';

if (!__ENV.BASE_URL) {
    throw new Error('La variable BASE_URL no está definida. Por favor, define la variable al ejecutar el script.');
}

export let options = {
    stages: [
        { duration: '1s', target: 50000 },
        { duration: '10s', target: 50000 },
        { duration: '10s', target: 0 }
    ],
    thresholds: {
        http_req_duration: ['p(90) < 10'],
        http_reqs: ['rate>=50000']
    },
    noConnectionReuse: true,
};

export default function () {
    const BASE_URL = __ENV.BASE_URL;

    let response = http.get(BASE_URL);
    check(response, {
        'is status 200': r => r.status === 200,
        'response time < 10 ms': r => r.timings.duration < 10
    });
    sleep(1);
}