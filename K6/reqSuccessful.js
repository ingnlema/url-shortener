import { check, sleep } from "k6";
import http from "k6/http";

export let options = {
  stages: [
    { duration: "1m", target: 5000 },
    { duration: "2m", target: 5000 },
    { duration: "1m", target: 0 },
  ],
  thresholds: {
    checks: ["rate>0.95"],
  },
};

const URL = __ENV.URL || "http://localhost:8080/api/url/u";

export default function () {
  let res = http.get(URL);
  check(res, {
    "is status 200": (r) => r.status === 200,
  });
  sleep(1);
}
