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
const MODEL_URL = '/api/v1/models';

export function getModels(modelParams = {}){
  const requestOptions = {
    method: 'GET',
    headers: new Headers({"Content-Type": "application/json"})
  }
  const params = new URLSearchParams(modelParams);
  return fetch(`${MODEL_URL}?${params}`, requestOptions)
    .then(response => response.json());
}

export function getModelById(id){
  const requestOptions = {
    method: 'GET',
    headers: new Headers({"Content-Type": "application/json"})
  }
  return fetch(`${MODEL_URL}/${id}`, requestOptions)
    .then(response => response.json());
}
