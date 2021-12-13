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

import { Administration } from '../data/administration';
const A = {
    id: '0253dd4d-35af-43f5-a84c-7cc2808000001',
    title: 'Company Data Management',
    subtitle: 'Draft',
    pathname: ''
}

const B = {
    id: '0253dd4d-35af-43f5-a84c-7cc2808000002',
    title: 'User Management',
    subtitle: 'Draft',
    pathname: ''
}

const C = {
    id: '0253dd4d-35af-43f5-a84c-7cc2808000003',
    title: 'Business Partner Management',
    subtitle: 'Draft',
    pathname: ''
}

export class AdminState {
    public static state = new AdminState();
    public apps: Administration[] = [A, B, C];
    public readonly categories: any[] = [{ text: 'Administration', apps: this.apps }]
}