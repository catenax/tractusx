# Consume aspects

This document explains how aspects will be consumed in the Speedboat environment.

## Problem statement

If company A wants to share an artifact with other companies, company A has to register the artifact once in the company-A-provider-connector.
Then, each company needs to request an agreement between their own consumer and the company A provider. Each of them would get a URL to access the artifact. The URL point to their own consumer.
If company B wants to access the artifact from company A, it will use the following URL: company-b-consumer/artifacts/123/data, company C would use: company-c-consumer/artifacts/456/data.
This makes it impossible to provide a common URL to access a specific artifact. Each of the company needs to negotiate a contract and generate their own url to access the artifact.

This document provides a temporary solution that work with Kaputt as the only aspect consumer.

## Consume aspects design

