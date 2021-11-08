## Git token

Create a [personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) in GitHub, limited to **read:packages** scope.

Configure the following GitHub *repository secrets*:
- PRS_EDC_PKG_USERNAME (your_github_username)
- PRS_EDC_PKG_PASSWORD (your_github_pat_token)

## Local build

Copy `settings.xml` into your `~/.m2/` folder (or merge it with a file already there), and replace the environment variable references with the following:

```
<username>your_github_username</username>
<password>your_github_pat_token</password>
```

## Docker build

See `run-integration-test.sh` file.

## Running tests

```
export PRS_EDC_PKG_USERNAME=your_github_username
export PRS_EDC_PKG_PASSWORD=your_github_pat_token
./run-integration-test.sh
```
