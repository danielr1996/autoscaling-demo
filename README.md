# autoscaling-demo

## Deployment

### Build JARs
```shell
./gradlew build
```

### Build images
```shell
for app in consumer producer metrics
do
  docker buildx build --push -t "ghcr.io/danielr1996/autoscaling-demo-$app" "$app"
done
```

### Deploy to Kubernetes
```shell
helm dependency update chart && helm upgrade --install -n autoscaling-demo --create-namespace autoscaling-demo chart/ -f values-common.yaml -f values-scaling.yaml 
```

