# Catena-X Portal (Derived from Create React App)

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `copy ${workspace}.env .env`

The checked in .env configuration points against the CX staging environment. If you want to configure a different one,
copy the preconfigured environment envs over the default file.

### `npm install npm@latest`

Setup and actualize node dependencies and packages. Needed before start or build or test.

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

### Dockerize the Catena-X Portal

There are two Dockerfiles available:
- [Dockerfile](Dockerfile) is the (former) standard build using a "production" npm build which is then injected statically into an nginx server. Did only work when deploying the final app into an app service instead of the k8 target. So this is deprecated.
- [Dockerfile.develop](Dockerfile.develop) uses a "develop" npm build and server possibly providing more console output. This works well in the k8 setting using an ingress.

Both Dockerfiles expose the portal on port 80.

To build the image, use the following command (where $PROXY should be set to your corporate proxy, if any, $CONTAINER_REGISTRY points to the deployment environment's docker/container registry (usually `catenaxacr.azurecr.io`), $WORKSPACE is your target environment (`int` for staging) and $VERSION hints to the respective deployment version, typically `latest`).

```
docker build -f Dockerfile.develop --build-arg HTTP_PROXY=${PROXY} --build-arg HTTPS_PROXY=${PROXY} -t ${CONTAINER_REGISTRY}/frontend/portal${WORKSPACE}:${VERSION} .
```

To push the image to the container registry, use the following command (you need to be logged in using `docker login` or `az acr login`), use the following command

```
docker push ${CONTAINER_REGISTRY}/frontend/portal${WORKSPACE}:${VERSION}
```

### Deploy the Catena-X Portal

Usually the Portal deployment happens automatically upon merging a pull request (see [Github Action for Portal Deployment](../../../.github/workflows/portal.yml)).

You can do it manually using the [Kubernetes Deploment Descriptor](../../../infrastructure/manifests/portal.yaml), by performing

`cat portal.yaml | envsubst | kubectl apply -n portal -f -`

Be sure to set the additional environment variables (e.g. export CATENA_PORTAL_URL = "catenaxintaksportal.germanywestcentral.cloudapp.azure.com"; export IMAGE_PULL_POLICY="Always")

#### Perform a rollout

If the k8 deployment of the portal in the preceding steps has already been made, rolling out or restarting the portal is as easy as

`kubectl rollout restart deployment portal -n portal`

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).
