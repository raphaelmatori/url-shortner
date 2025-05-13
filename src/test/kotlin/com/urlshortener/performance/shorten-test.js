import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 100,
    duration: '100s',
};

export default function () {
    const payload = JSON.stringify({
        url: `https://google.com/${Math.random()}`,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post('http://localhost:8080/shorten', payload, params);
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
