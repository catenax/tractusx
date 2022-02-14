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

import * as React from 'react';
import { observer } from 'mobx-react';
import { Row, Navbar, Nav, Container } from 'react-bootstrap';

@observer
export default class Footer extends React.Component {
    public render() {
        return (
            <Row>
                <div className='footer-container flex-column d-flex align-items-center justify-content-center'>
                    <div>
                        <Navbar collapseOnSelect expand="lg">
                            <Container>
                                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                                <Navbar.Collapse id="responsive-navbar-nav">
                                    <Nav className="me-auto">
                                        <Nav.Link href="#features">Help</Nav.Link>
                                        <Nav.Link href="#pricing">Contact</Nav.Link>
                                        <Nav.Link href="#features">Imprint</Nav.Link>
                                        <Nav.Link href="#pricing">Privacy</Nav.Link>
                                        <Nav.Link href="#features">Terms of Service</Nav.Link>
                                        <Nav.Link href="#pricing">Cookie Policy</Nav.Link>
                                        <Nav.Link href="#features">Third Party License Notes</Nav.Link>
                                    </Nav>
                                </Navbar.Collapse>
                            </Container>
                        </Navbar>
                    </div>
                    <div>Copyright © by Catena-X Automotive Network.</div>
                </div>
            </Row>
        );
    }
}