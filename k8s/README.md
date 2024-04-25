# Dev Kubernetes cluster setup

For local development environment we will use [kind](https://kind.sigs.k8s.io/) as our local k8s cluster.
[kind](https://kind.sigs.k8s.io/) is a tool for running local Kubernetes clusters using Docker container “nodes”. So docker will also be needed.

We will deploy to local cluster:

**keycloak**: authentication service. 

We will install Keycloak as Operator, the recommended installation method is using OLM (Operator Lifecycle Manager), however for our local installation we will use kubectl. As explain in: https://www.keycloak.org/operator/installation 

Install the CRDs by entering the following commands:
```
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/24.0.3/kubernetes/keycloaks.k8s.keycloak.org-v1.yml
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/24.0.3/kubernetes/keycloakrealmimports.k8s.keycloak.org-v1.yml
```

Install the Keycloak Operator deployment by entering the following command:
```
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/24.0.3/kubernetes/kubernetes.yml
```

Currently, the Operator watches only the namespace where the Operator is installed.

Once operator is installed, you can create a Keycloak instance using standard Operator yaml file, example: *keycloak.yaml*


**postgresql**: database for keycloak. 
See *postgres.yaml* for details

