# Dev Kubernetes cluster setup

For local development environment we will use [kind](https://kind.sigs.k8s.io/) as our local k8s cluster.
[kind](https://kind.sigs.k8s.io/) is a tool for running local Kubernetes clusters using Docker container “nodes”. So docker will also be needed.

We will deploy to local cluster:

**keycloak**: authentication service. 

We will install Keycloak as Operator, the recommended installation method is using OLM (Operator Lifecycle Manager), however for our local installation we will use kubectl. As explain in: https://www.keycloak.org/operator/installation 

See *quick_start.sh* for details

Currently, the Operator watches only the namespace where the Operator is installed.

Once operator is installed, you can create a Keycloak instance using standard Operator yaml file, example: *keycloak.yaml*

**postgresql**: database for keycloak. 
See *postgres.yaml* for details

To expose ports on local machine, we are using NodePort service type.
ports range: 30000-30005 

See *kind-config.yaml* and each service yaml for details on exposed ports.

## Keycloak

currently and for simplicity, maybe there is a better way do the following:

```
kubectl port-forward service/kc-service 8180:8180
```

Then you can access keycloak at: http://localhost:8180

kubectl get secret example-kc-initial-admin -o jsonpath='{.data.username}' | base64 --decode
kubectl get secret example-kc-initial-admin -o jsonpath='{.data.password}' | base64 --decode


a860b2073da148448865b082fbfc7d68