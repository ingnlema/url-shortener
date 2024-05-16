FROM grafana/k6:latest

COPY k6-script.js /k6-script.js

ENTRYPOINT ["k6", "run", "/k6-script.js"]