Spring Boot → Docker → Promtail → Loki Logging Setup

1\. Configure Spring Boot to Write Logs in a Specific Directory
---------------------------------------------------------------

In your **application.yml**, configure Spring Boot to generate logs inside a folder named `logs`:

logging:
file:
path: ./logs

* * *

2\. Create a Docker Container for Your Spring Boot Application
--------------------------------------------------------------

Your Docker Compose entry for the application might look like this:

chat-system:
image: chat-system:1.0.0
environment:
- CHAT\_SYSTEM\_DB\_HOST=chat-system-db
- CHAT\_SYSTEM\_MINIO\_HOST=minio
- LOGGING\_FILE\_PATH=/app/logs
- APP\_PORT=3000
ports:
- "3000:3000"
networks:
- app-net
volumes:
- ./logs:/app/logs   # mount logs from container /app/logs to local ./logs

**Important Note:**  
The environment variable `LOGGING_FILE_PATH=/app/logs` overrides Spring Boot's default log path. Since the Spring Boot application runs inside `/app`, it writes logs to `/app/logs`. This path is bind-mounted to your local machine's `./logs` directory using:

volumes:
- ./logs:/app/logs

This means logs written inside the container at `/app/logs` will appear in your project's `./logs` directory on your local machine.

* * *

3\. Promtail Configuration to Read Logs and Push to Loki
--------------------------------------------------------

Promtail is configured to read logs from your project directory's `./logs` folder because you mount it inside the promtail container.

promtail:
image: grafana/promtail:2.9.0
ports:
- "9080:9080"
volumes:
- ./config/promtail-config.yaml:/etc/promtail/config.yml
- ./logs:/var/logs
deploy:
resources:
limits:
memory: 512M
cpus: '0.5'
networks:
- app-net

This mounting does the following:

volumes:
- ./config/promtail-config.yaml:/etc/promtail/config.yml
- ./logs:/var/logs   # maps logs generated locally to /var/logs inside promtail container

So Promtail reads logs from `/var/logs` which actually reflect the `./logs` directory on your machine.

* * *

4\. Promtail Config to Read from /var/logs and Push to Loki
-----------------------------------------------------------

**promtail-config.yaml**

server:
http\_listen\_port: 9080
grpc\_listen\_port: 0

positions:
filename: ./promtail-positions.yaml

clients:
- url: http://loki:3100/loki/api/v1/push

scrape\_configs:
- job\_name: spring-boot-logs
  static\_configs:
    - targets:
        - host.docker.internal
          labels:
          job: chat-system-logs
          \_\_path\_\_: /var/logs/\*.log

Promtail reads log files from `/var/logs` (which maps to your local `./logs` folder) and ships them to Loki running on port `3100`.

* * *

Workflow Summary
----------------

1.  Spring Boot writes logs to `./logs` (overridden to `/app/logs` in Docker).
2.  Docker binds `/app/logs` → `./logs` (local machine).
3.  Promtail mounts the same `./logs` folder to `/var/logs`.
4.  Promtail scrapes `/var/logs/*.log`.
5.  Promtail pushes logs to Loki endpoint `http://loki:3100/loki/api/v1/push`.


-------------------

Spring Boot Metrics + Prometheus + Docker Setup

1\. Updated `application.yml` (Spring Boot Observability + Metrics + Prometheus)
--------------------------------------------------------------------------------

Below is the updated **application.yml** that now includes `management.endpoints` configuration, enabling metrics, health, info, and Prometheus scraping endpoints. It also enables percentiles and histograms for `http.server.requests` and adds global tags.

logging:
file:
path: ./logs

management:
endpoints:
web:
exposure:
include: metrics,health,info,prometheus
endpoint:
health:
show-details: always
metrics:
distribution:
percentiles-histogram:
http.server.requests: true
percentiles:
"http.server.requests": 0.5, 0.95, 0.99
tags:
application: ${spring.application.name}

These configurations ensure Spring Boot exposes full actuator information and telemetry via:

*   `/actuator/metrics`
*   `/actuator/prometheus`
*   `/actuator/health`
*   `/actuator/info`

* * *

2\. Run Prometheus in Docker (Reading Config from `config/` folder)
-------------------------------------------------------------------

Prometheus will be configured to scrape Spring Boot’s `/actuator/prometheus` endpoint. Place your Prometheus config file at:

./config/prometheus.yml

**Example Prometheus config:**

global:
scrape\_interval: 5s

scrape\_configs:
- job\_name: 'spring-boot-app'
  metrics\_path: '/actuator/prometheus'
  static\_configs:
    - targets: \['chat-system:3000'\]

Now add Prometheus to your `docker-compose.yml` and mount the config from the **project root config folder**:

prometheus:
image: prom/prometheus:latest
container\_name: prometheus
ports:
- "9090:9090"
volumes:
- ./config/prometheus.yml:/etc/prometheus/prometheus.yml
networks:
- app-net

This ensures Prometheus reads configuration from your project directory and automatically scrapes the Spring Boot container.

* * *

3\. Full Observability Workflow (Explained Simply)
--------------------------------------------------

1.  **Spring Boot** writes logs to `/app/logs`, which are bind-mounted to `./logs` on your machine.
2.  **Promtail** reads log files from `./logs` via the `/var/logs` mount and pushes them to Loki.
3.  **Spring Boot metrics** are exposed at `/actuator/prometheus` along with histogram and percentile data.
4.  **Prometheus** runs in Docker, loads config from `./config/prometheus.yml`, and scrapes `chat-system:3000/actuator/prometheus`.
5.  **Loki** receives log streams. **Prometheus** receives metrics. Both can be visualized later using Grafana.

* * *

4\. Final Notes
---------------

*   `management.endpoints.web.exposure` enables Prometheus + metrics endpoints.
*   Prometheus reads config from your local `config` folder.
*   Scraping begins immediately once Prometheus is up.
*   Spring Boot now supports full observability (logs + metrics).

If you want, I can also generate a complete **docker-compose.yml** containing Spring Boot + Promtail + Loki + Prometheus + Grafana.

