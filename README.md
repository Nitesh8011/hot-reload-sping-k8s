## Spring Boot + Kubernetes Hot Reload (ConfigMap Watch)

Demonstrates hot-reloading configuration in a Spring Boot 3 app running on Kubernetes using Spring Cloud Kubernetes. When you change a ConfigMap, the app refreshes its configuration without restarting the pod.

### Stack
- Java 24, Maven
- Spring Boot 3.1.2
- Spring Cloud 2022.0.x (Kilburn)
- Spring Cloud Kubernetes 3.2.x
- Kubernetes (minikube or kind for local)

### What it does
- Loads configuration from Kubernetes ConfigMaps via `spring.config.import: "optional:kubernetes:"`.
- Watches ConfigMaps and hot-reloads beans annotated with `@RefreshScope`.
- Exposes REST endpoints to view current configuration.

### Endpoints
- `GET /message` – returns current `app.message`
- `GET /config` – shows `app.message`, `app.environment`, `app.refreshCount`
- `GET /health`
- `GET /reload-info`
- `GET /log-config`
- `POST /refresh` – manual refresh trigger (fallback)

### Prerequisites
- Java 24, Maven
- Docker
- kubectl
- One of: minikube or kind

### Build
```bash
mvn -DskipTests package
```

### Run locally (no Kubernetes)
```bash
java -jar target/*.jar
# http://localhost:8080/message
```
Note: outside Kubernetes, the app only reads `src/main/resources/application.yml`.

### Deploy to Kubernetes (minikube)
1) Start cluster and point Docker to it
```bash
minikube start
eval $(minikube -p minikube docker-env)
```

2) Build image (optional if you use the public image)
```bash
mvn -DskipTests package
docker build -t sample-app:latest .
```

3) Apply manifests (namespace: `default`)
```bash
kubectl apply -f k8s/rbac.yml
kubectl apply -f k8s/configmap.yml
kubectl apply -f k8s/deployment.yml
kubectl apply -f k8s/service.yml
kubectl rollout status deploy/sample-app -n default
```
The deployment is set to `nitesh8011/application:hot-reload` (Always pull). To use your local image instead, run:
```bash
kubectl set image deploy/sample-app sample-app=sample-app:latest -n default
kubectl patch deploy sample-app -n default -p '{"spec":{"template":{"spec":{"containers":[{"name":"sample-app","imagePullPolicy":"IfNotPresent"}]}}}}'
```

4) Port-forward and test
```bash
kubectl port-forward service/sample-app -n default 8080:8080
# In another terminal
curl http://localhost:8080/message
```

5) Hot reload by updating ConfigMap
```bash
kubectl patch configmap sample-app -n default \
  --type=merge --patch '{"data":{"app.message":"Hello from ConfigMap $(date +"%H:%M:%S")"}}'
# Verify
curl http://localhost:8080/message
```
You should see the message change without a pod restart.

6) Optional manual refresh
```bash
curl -X POST http://localhost:8080/refresh
```

### Configuration notes
- `src/main/resources/application.yml` contains:
  - `spring.config.import: "optional:kubernetes:"` (quoted to avoid YAML parse issues)
  - `spring.profiles.active: local` by default; Deployment sets `SPRING_PROFILES_ACTIVE=kubernetes`.
  - Kubernetes reload enabled (event mode).
- `k8s/configmap.yml` provides `app.message`, `app.environment`, `app.refreshCount`.
- `k8s/rbac.yml` sets `ServiceAccount` and grants `get/list/watch` on ConfigMaps (and pods) so the app can watch changes.
- `k8s/deployment.yml` runs the app with the `reload-sa` service account and exposes port 8080.

### Troubleshooting
- Seeing the `application.yml` message instead of ConfigMap?
  - Ensure pod and ConfigMap are in the same namespace (`default`).
  - Confirm `SPRING_PROFILES_ACTIVE=kubernetes` in the pod env.
  - Check `application.yml` has `spring.config.import: "optional:kubernetes:"`.
  - Verify RBAC applied in `default` and `serviceAccountName: reload-sa` set.
  - Logs:
    ```bash
    kubectl logs deploy/sample-app -n default | grep -i kubernetes
    ```
  - Inspect effective properties:
    ```bash
    curl http://localhost:8080/actuator/configprops | grep -A2 app.message
    ```

### Cleanup
```bash
kubectl delete -f k8s/service.yml
kubectl delete -f k8s/deployment.yml
kubectl delete -f k8s/configmap.yml
kubectl delete -f k8s/rbac.yml
```