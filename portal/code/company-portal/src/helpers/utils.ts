// Copyright (c) 2021 Microsoft
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

import { CompanyDetails, CompanyRole, ConsentForCompanyRoles, UserRole } from "../data/companyDetails";
import UserService from '../helpers/UserService';

const url = process.env.REACT_APP_ONBOARDING_URL;
const endpoint = process.env.REACT_APP_ONBOARDING_ENDPOINT;
export function getCompanyDetails(oneId: String): Promise<CompanyDetails[]> {
  console.log('API called getCompanyDetails');
  const realm = UserService.realm;
  const token = UserService.getToken();
  const u = `${url}/${endpoint}/${realm}/${oneId}`;
  let myResponseData: CompanyDetails[] = [];
  const promise = new Promise<CompanyDetails[]>((resolve, reject) => {
    fetch(u, { method: 'GET', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } })
      .then((val) => val.json().then((data) => {
        if (val.ok) {
          Object.assign(myResponseData, data)
          resolve(myResponseData);
        } else {
          reject(val.statusText);
        }
      })).catch((error) => {
        alert(error);
        console.log(error, error.message, error.status);
        reject(error.message);
      });
  });

  return promise;
}

export function getCompanyRoles(): Promise<CompanyRole[]> {
  const realm = UserService.realm;
  const token = UserService.getToken();
  const u = `${url}/${endpoint}/${realm}/companyRoles`;
  let companyRolesRes: CompanyRole[] = [];
  const promise = new Promise<CompanyRole[]>((resolve, reject) => {
    fetch(u, { method: 'GET', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } })
      .then((val) => val.json().then((data) => {
        if (val.ok) {
          Object.assign(companyRolesRes, data)
          resolve(companyRolesRes);
        } else {
          reject(val.statusText);
        }
      })).catch((error) => {
        alert(error);
        console.log(error, error.message, error.status);
        reject(error.message);
      });
  });

  return promise;
}

export function getConsentForCompanyRoles(roleId: Number): Promise<ConsentForCompanyRoles[]> {
  const realm = UserService.realm;
  const endpoint = 'api/onboarding/company';
  const token = UserService.getToken();
  const u = `${url}/${endpoint}/${realm}/consentsFoCompanyRole/${roleId}`;
  let myConsentResponseData: ConsentForCompanyRoles[] = [];
  const promise = new Promise<ConsentForCompanyRoles[]>((resolve, reject) => {
    fetch(u, { method: 'GET', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } })
      .then((val) => val.json().then((data) => {
        if (val.ok) {
          Object.assign(myConsentResponseData, data)
          resolve(myConsentResponseData);
        } else {
          reject(val.statusText);
        }
      })).catch((error) => {
        alert(error);
        console.log(error, error.message, error.status);
        reject(error.message);
      });
  });
  return promise;
}

export function getUserRoles(): Promise<UserRole[]> {
  const realm = UserService.realm;
  const token = UserService.getToken();
  const u = `${url}/${endpoint}/${realm}/userRoles`;
  let userRolesRes: UserRole[] = [];
  const promise = new Promise<UserRole[]>((resolve, reject) => {
    fetch(u, { method: 'GET', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } })
      .then((val) => val.json().then((data) => {
        if (val.ok) {
          Object.assign(userRolesRes, data)
          resolve(userRolesRes);
        } else {
          reject(val.statusText);
        }
      })).catch((error) => {
        alert(error);
        console.log(error, error.message, error.status);
        reject(error.message);
      });
  });

  return promise;
}

export function getOwnUserRoles(): Promise<UserRole[]> {
  const realm = UserService.realm;
  const token = UserService.getToken();
  const u= `${url}/${endpoint}/${realm}/ownUserRoles`;
  let userRolesRes: UserRole[] = [];
  const promise = new Promise<UserRole[]>((resolve, reject) => {
    fetch(u, { method: 'GET', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' } })
    .then((val) => val.json().then((data) => {
      if (val.ok) {
        Object.assign(userRolesRes,data)
        resolve(userRolesRes);
      } else {
        reject(val.statusText);
      }
    })).catch((error) => {
        alert(error);
        console.log(error, error.message, error.status);
        reject(error.message);
      });
  });

  return promise;
}