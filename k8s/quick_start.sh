#!/bin/bash

# Check if kind is installed
if ! command -v kind &> /dev/null
then
    echo "kind could not be found"
    exit 1
fi

# Create local k8s using kind and set local ingress
echo "Creating local cluster ..."
kind create cluster --config kind-config.yaml

# Check if kubectl is installed
if ! command -v kubectl &> /dev/null
then
    echo "kubectl could not be found"
    exit 1
fi

## Ingress Nginx
#echo "Installing ingress-nginx ..."
#kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
#
#kubectl wait --namespace ingress-nginx \
#  --for=condition=ready pod \
#  --selector=app.kubernetes.io/component=controller \
#  --timeout=90s




# Deploy postgresql
kubectl apply -f postgresql.yaml

# Keycloak Operator
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/24.0.3/kubernetes/keycloaks.k8s.keycloak.org-v1.yml
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/24.0.3/kubernetes/keycloakrealmimports.k8s.keycloak.org-v1.yml
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/24.0.3/kubernetes/kubernetes.yml


# Deploy keycloak operator and keycloak instance
kubectl apply -f keycloak.yaml

