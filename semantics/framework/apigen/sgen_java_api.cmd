REM
REM Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
REM
REM See the AUTHORS file(s) distributed with this work for additional information regarding authorship.
REM
REM See the LICENSE file(s) distributed with this work for additional information regarding license terms.
REM

REM Command to regenerated client stub code for DSC
REM Embedded Swagger Codegen CLI licensed under Apache License 2.0

set PATH=C:\Data\Programme\jdk-11.0.2\bin;C:\Data\Programme\apache-maven-3.8.1\bin;%PATH%
set JAVA_HOME=C:\Data\Programme\jdk-11.0.2\
rmdir /S /Q idsconnector
java -jar swagger-codegen-cli-3.0.27.jar generate -l java -o idsconnector -i openapi.yaml  -c config.json --library=feign --api-package com.tsystems.catenax.idsadapter.client.api --model-package com.tsystems.catenax.idsadapter.client.model --invoker-package com.tsystems.catenax.idsadapter.client.invoker