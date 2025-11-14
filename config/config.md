First configure your spring boot application to generate log in specific dir let's say logs dir
logging:
file:
path: ./logs

create a docker container of your spring boot application
chat-system:
image: chat-system:1.0.0
environment:
- CHAT_SYSTEM_DB_HOST=chat-system-db
- CHAT_SYSTEM_MINIO_HOST=minio
- LOGGING_FILE_PATH=/app/logs
- APP_PORT=3000
ports:
- "3000:3000"
networks:
- app-net
volumes:
- ./logs:/app/logs  # mount logs from container /app/logs to local ./logs

point to be noted here -> LOGGING_FILE_PATH=/app/logs overrding the logging path with /app/logs as spring boot application runs on /app then it 
will find your logs at logs dir which is in the project dir where your application running

And the logs genereate at /app/logs will be bind to local machine at your project dir and with the folder name logs as configured in volume mounting
volumes:
- ./logs:/app/logs  # mount logs from container /app/logs to local ./logs

Now promtail is configured in such a way that it will read the logs present in logs dir in your project root dir and push it to loki
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

what it does it read logs from ./logs dir and mapped it to /var/logs dir in the container running promtail as configured in volume config
volumes:
- ./config/promtail-config.yaml:/etc/promtail/config.yml
- ./logs:/var/logs  # maps logs generated at local machine to container dir /var/logs


and in promtail config we have written to  read from /var/logs and push to loki running at 3100
server:
http_listen_port: 9080
grpc_listen_port: 0

positions:
filename: ./promtail-positions.yaml

clients:
- url: http://loki:3100/loki/api/v1/push

scrape_configs:
- job_name: spring-boot-logs
  static_configs:
    - targets:
        - host.docker.internal
          labels:
          job: chat-system-logs
          __path__: /var/logs/*.log


