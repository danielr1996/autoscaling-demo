# autoscaling-demo

## Deployment

### Build JARs
```shell
./gradlew build
```

### Build images
```shell
for app in consumer producer
do
  docker buildx build --push -t "ghcr.io/danielr1996/autoscaling-demo-$app" "$app"
done
```

### Deploy to Kubernetes
```shell
helm dependency update chart
helm upgrade --install -n autoscaling-demo --create-namespace autoscaling-demo chart/ -f values-common.yaml 
```


### Examples
```shell
# Normal Operation (no backpressue, queue gets written to slower than it gets read from)
helm upgrade --install -n autoscaling-demo --create-namespace autoscaling-demo chart/ -f values-common.yaml -f values-live.yaml -f values-open.yaml  
# Degraded Operation (backpressure, queue gets written to faster than it gets read from) 
helm upgrade --install -n autoscaling-demo --create-namespace autoscaling-demo chart/ -f values-common.yaml -f values-live.yaml -f values-throttled.yaml  
```