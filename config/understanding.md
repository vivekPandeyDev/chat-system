# Prometheus, Loki, and Promtail Overview

## Prometheus
**Category:** Metrics collection & storage  
**Purpose:** Collects numeric metrics over time.

### What it does
- Scrapes metric endpoints (e.g., Spring Actuator at `/actuator/prometheus`)
- Stores time-series metrics such as:
    - JVM memory usage
    - CPU usage
    - HTTP request counts
    - 95th percentile latency
- Provides a powerful query language (**PromQL**)
- Integrates with **Alertmanager** to generate alerts

### Why you need it
- To track performance, system health, and quantitative metrics across services and infrastructure.

---

## Loki
**Category:** Log aggregation system  
**Purpose:** Centralized log storage (similar to ELK but cheaper and simpler).

### What it does
- Stores logs from all services and containers
- Works like Elasticsearch but:
    - Does **not** index log content
    - Stores **labels** (e.g., app name, container, job)
- Highly efficient storage—reduced cost and improved performance

### Why you need it
- To search, filter, and analyze logs across distributed microservices without the heavy cost of ELK.

---

## Promtail
**Category:** Log collector / agent  
**Purpose:** Read logs on hosts or containers and send them to Loki.

### What it does
- Runs on each host, VM, node, or Kubernetes pod
- Reads logs from:
    - `/var/log/*`
    - Docker container logs
    - Kubernetes pod logs
    - Spring Boot log files
- Adds useful labels (app, env, pod, instance)
- Pushes the logs to **Loki**

### Why you need it
- To reliably ship logs from machines or containers to Loki for centralized log storage and analysis.
