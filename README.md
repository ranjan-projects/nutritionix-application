# nutritionix-application

## Introduction
nutritionix-application is a multi module application built on Springboot to manage the nutrition for the user. 
Modules have been created based on their responsilities.

* api-gateway - Entry point for any incoming request
* auth-serviices - Responsible to handle authentication for the user 
* config-server - Responsible for managing the configurations externally for all modules
* eureka-server - Responsible for Service discovery
* nutrition-services - Responsible for fetching the nutrition for input food item from third party URL
* user-services - Responsible for managing the users
* wishlish-services - Responsible for fetching the food items which has been marked as favourite by the user

Frontend has been created using react libraries under react-nutrionix-app directory

Note - All endpoints needs to be fired to API Gateway and API gateway will run through list of filters and authenticate the user. 
If the authentication filter passes. only then request will be routed to target service.

## Toolset requirement
You need to have docker installed on the machine to run this application.

## Build Application
* mvn -T 4 clean install
* docker compose up --build

***
## Run application
* Run the command **docker compose up --build** to start the application from home directory of nutrition-application.
* Access the application using http://localhost:3000

***
## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.stackroute.in/ranjanrahul/nutritionix-application/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***