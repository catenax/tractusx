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
const MODEL_URL = `${process.env.REACT_APP_SEMANTIC_SERVICE_LAYER_URL}models`;

interface newModel{
  model: string,
  private: boolean,
  type: string
}

export function encodeID(id: string){
  const idSplit = id.split('#');
  return `${idSplit[0]}${encodeURIComponent(`#${idSplit[1]}`)}`
}

function handleRequest(res: Response){
  if(res.status >= 400) {
    throw new Error(`Server responds with ${res.status} error!`);
  }
  return res.json();
}

export function getModels(modelParams = {}){
  const requestOptions = {
    method: 'GET',
    headers: new Headers({"Content-Type": "application/json"})
  }
  return fetch(`${MODEL_URL}?${modelParams}`, requestOptions)
    .then(handleRequest);
}

export function getModelById(id: string){
  const requestOptions = {
    method: 'GET',
    headers: new Headers({"Content-Type": "application/json"})
  }
  return fetch(`${MODEL_URL}/${id}`, requestOptions)
    .then(handleRequest);
}

export function addModel(model: newModel){
  const requestOptions = {
    method: 'POST',
    headers: new Headers({"Content-Type": "application/json"}),
    body: JSON.stringify(model)
  }

  return fetch(MODEL_URL, requestOptions).then(handleRequest);
}

export function deleteModel(id: string){
  const requestOptions = {
    method: 'DELETE',
    headers: new Headers({"Content-Type": "application/json"})
  }
  return fetch(`${MODEL_URL}/${id}`, requestOptions)
    .then(handleRequest);
}

export function getModelDiagram(id){
  return `${MODEL_URL}/${id}/diagram`;
}

export function getDocumentationUrl(id){
  return `${MODEL_URL}/${id}/documentation`;
}

export function getFileUrl(id){
  return `${MODEL_URL}/${id}/file`;
}
