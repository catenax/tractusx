// Copyright (c) 2021 T-Systems
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
const TWIN_URL = `${process.env.REACT_APP_SEMANTIC_SERVICE_LAYER_URL}twins`;

interface httpEndpoint {
  id: string,
  method: string,
  url: string
}

interface aspect {
  httpEndpoints: httpEndpoint[],
  id: string,
  modelReference: {
    urn: string
  }
}

export interface DigitalTwin {
  aspects: aspect[],
  description: string,
  id: string,
  localIdentifiers: [
    {
      key: string,
      value: string
    }
  ],
  manufacturer: string
}

function handleRequest(res: Response){
  if(res.status >= 400) {
    throw new Error(`Server responds with ${res.status} error!`);
  }
  return res.json();
}

export function getTwins(){
  const requestOptions = {
    method: 'GET',
    headers: new Headers({"Content-Type": "application/json"})
  }
  return fetch(`${TWIN_URL}`, requestOptions)
    .then(handleRequest);
}

export function getTwinById(id: string){
  const requestOptions = {
    method: 'GET',
    headers: new Headers({"Content-Type": "application/json"})
  }
  return fetch(`${TWIN_URL}/${id}`, requestOptions)
    .then(handleRequest);
}
