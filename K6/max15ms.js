import { check, sleep } from "k6";
import http from "k6/http";

export let options = {
  stages: [
    { duration: "30s", target: 1000 },
    { duration: "1m", target: 5000 },
    { duration: "10s", target: 0 },
  ],
  thresholds: {
    http_req_duration: ["p(90)<10"],
  },
};

const URL = __ENV.URL || "http://localhost:8080/api/url/u";

export default function () {
  let res = http.get(URL);
  check(res, {
    "is status 200": (r) => r.status === 200,
    "is response time < 15ms": (r) => r.timings.duration < 15,
  });
  sleep(1);
}
