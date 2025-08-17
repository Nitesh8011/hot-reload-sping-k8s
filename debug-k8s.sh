#!/bin/bash

echo "=== KUBERNETES DEBUG SCRIPT ==="
echo

# Get current namespace
CURRENT_NS=$(kubectl config view --minify -o jsonpath='{..namespace}')
echo "Current namespace: $CURRENT_NS"
echo

# Check if ConfigMaps exist
echo "=== CONFIGMAPS ==="
kubectl get configmaps
echo

# Check ConfigMap details
echo "=== CONFIGMAP 1 DETAILS ==="
kubectl describe configmap hot-reload-cm
echo

echo "=== CONFIGMAP 2 DETAILS ==="
kubectl describe configmap hot-reload-cm-2
echo

# Check if pods are running
echo "=== PODS ==="
kubectl get pods -l app=hot-reload
echo

# Check pod logs for Spring Cloud Kubernetes
echo "=== POD LOGS (last 20 lines) ==="
POD_NAME=$(kubectl get pods -l app=hot-reload -o jsonpath='{.items[0].metadata.name}')
if [ ! -z "$POD_NAME" ]; then
    kubectl logs $POD_NAME --tail=20
else
    echo "No pods found with label app=hot-reload"
fi
echo

# Check RBAC
echo "=== RBAC ==="
kubectl get serviceaccount hot-reload-sa
kubectl get role hot-reload-role
kubectl get rolebinding hot-reload-rb
echo

echo "=== DEBUG COMPLETE ==="
