import { check, sleep } from "k6";
import http from "k6/http";

export let options = {
  stages: [
    { duration: "30s", target: 1000 },
    { duration: "1m", target: 5000 },
    { duration: "10s", target: 0 },
  ],
  thresholds: {
    http_req_duration: ["p(90)<10"], // 90% of requests must complete below 10ms.
  },
};

export default function () {
  let res = http.get("http://104.198.246.128:80/api/url/4m");
  check(res, {
    "is status 200": (r) => r.status === 200,
    "is response time < 10ms": (r) => r.timings.duration < 10,
  });
  sleep(1);
}
