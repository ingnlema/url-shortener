import { check, sleep } from "k6";
import http from "k6/http";

export let options = {
  stages: [
    { duration: "30s", target: 50000 }, // Ramp-up to 50,000 users over 30 seconds
    { duration: "30s", target: 50000 }, // Stay at 50,000 users for 30 seconds
    { duration: "30s", target: 0 },     // Ramp-down to 0 users over 30 seconds
  ],
  thresholds: {
    http_req_duration: ["p(90)<50"], // Adjust as needed for high load
  },
};

const URL = __ENV.URL || "http://localhost:8080/api/url/u";

export default function () {
  let res = http.get(URL);
  check(res, {
    "is status 200": (r) => r.status === 200,
  });
  sleep(0.01); // Adjust sleep time to increase the number of requests per second
}

