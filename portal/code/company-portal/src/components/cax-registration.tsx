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

import * as React from "react";
import { observer } from "mobx-react";
import { observable } from "mobx";
import { Container, Row, Col } from "react-bootstrap";
import Footer from "./footer";
import Header from "./cax-header";
import ReactTooltip from "react-tooltip";
import "react-datepicker/dist/react-datepicker.css";
import { withTranslation, WithTranslation } from "react-i18next";
import CompanyDataCax from "./cax-companyData";
import Button from "./button";
import ResponsibilitiesCax from "./cax-responsibilities";
import DragDropUploadFiles from "./dragdrop";
import CompanyRoleCax from "./cax-companyRole";
import { FaEdit } from "react-icons/fa"
import UserService from '../helpers/UserService';
import { RouteComponentProps } from "react-router-dom";


@observer

class RegistrationCax extends React.Component<
  WithTranslation & RouteComponentProps,
  any
> {
  @observable currentActiveStep = 1;
 
  private nextClick() {
    
    if(this.currentActiveStep === 5){
    const url = process.env.REACT_APP_ONBOARDING_URL;
    const endpoint = process.env.REACT_APP_ONBOARDING_ENDPOINT;
    const token = UserService.getToken();
    const realm = UserService.realm;
    const featchUrl = `${url}/${endpoint}/${realm}/custodianWallet`;
    const data = {
      bpn : "BPNL890867291",
      name : "German Car Factory"    
    }
      fetch(featchUrl, { method: 'POST', headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' }, body: JSON.stringify(data) })
      .then((response) => {
        if (response.ok) {
          this.props.history.push("/finish");
        }
        else {
          this.props.history.push("/finish");
        }
      }

      ).catch((error) => {
        this.props.history.push("/finish");
      });

    }else{
      this.currentActiveStep = this.currentActiveStep + 1;
    }
  }

  private backClick() {
    this.currentActiveStep = this.currentActiveStep - 1;
  }

  private editClick(n) {
    this.currentActiveStep = n;
  }

  public render() {
    return (
      <Container>
        <Header href={window.location.href} />
        <Row>
          <Col>
            <div className="mx-auto col-9">
              <h4>{this.props.t("registration.registration")}</h4>
              <div>{this.props.t("registration.regiStep")}.</div>
              <div>{this.props.t("registration.regiSubHeading")}</div>
            </div>
            <div className="mx-auto col-11 reg-steps">
              <Row className="stepper-wrapper row-cols-5">
                <div className="stepper-item completed col">
                  <Row className="stepper-row">
                    <div className="step-counter col-3">1</div>
                    <div className="step-name col-9">
                      {this.props.t("registration.companyData")}
                    </div>
                  </Row>
                  <Row>
                    <div
                      className={
                        this.currentActiveStep === 1
                          ? "step-border col-10 mx-auto"
                          : ""
                      }
                    ></div>
                  </Row>
                </div>
                <div className="stepper-item completed col">
                  <Row className="stepper-row">
                    <div className="step-counter col-3">2</div>
                    <div className="step-name col-9">
                      {this.props.t("registration.responsAdmin")}
                    </div>
                  </Row>
                  <Row>
                    <div
                      className={
                        this.currentActiveStep === 2
                          ? "step-border col-10 mx-auto"
                          : ""
                      }
                    ></div>
                  </Row>
                </div>
                <div className="stepper-item active col">
                  <Row className="stepper-row">
                    <div className="step-counter col-3">3</div>
                    <div className="step-name col-9">
                      {this.props.t("registration.companyRole")}
                    </div>
                  </Row>
                  <Row>
                    <div
                      className={
                        this.currentActiveStep === 3
                          ? "step-border col-10 mx-auto"
                          : ""
                      }
                    ></div>
                  </Row>
                </div>
                <div className="stepper-item col">
                  <Row className="stepper-row">
                    <div className="step-counter col-3">4</div>
                    <div className="step-name col-9">
                      {this.props.t("registration.uploadDocument")}
                    </div>
                  </Row>
                  <Row>
                    <div
                      className={
                        this.currentActiveStep === 4
                          ? "step-border col-10 mx-auto"
                          : ""
                      }
                    ></div>
                  </Row>
                </div>
                <div className="stepper-item col">
                  <Row className="stepper-row">
                    <div className="step-counter col-3">5</div>
                    <div className="step-name col-9">
                      {this.props.t("registration.verifyEntries")}
                    </div>
                  </Row>
                  <Row>
                    <div
                      className={
                        this.currentActiveStep === 5
                          ? "step-border col-10 mx-auto"
                          : ""
                      }
                    ></div>
                  </Row>
                </div>
              </Row>
            </div>
            {this.currentActiveStep === 1 ? (
              <CompanyDataCax />
            ) : this.currentActiveStep === 2 ? (
              <ResponsibilitiesCax />
            ) : this.currentActiveStep === 3 ? (
               <CompanyRoleCax />
            ) : this.currentActiveStep === 4 ? (
                <DragDropUploadFiles/>
            ) : (
              <div className="mx-auto col-9 container-registration">
                <div className="head-section">
                  <div className="mx-auto step-highlight d-flex align-items-center justify-content-center">
                    5
                  </div>
                  <h4 className="mx-auto d-flex align-items-center justify-content-center">
                  Verify your registration data
                  </h4>
                  <div className="mx-auto text-center col-9">
                  Lorem ipsum sapientem ne neque dolor erat,eros solet invidunt duo Quisque aliquid leo. Pretium patrioque sociis eu nihil Cum enim ad.
                  </div>
                </div>
                <div className="companydata-form mx-auto col-9">
                  <Row>
                    <ul className="list-group-cax px-2">
                      <li className="list-group-item-cax list-header">
                        <Row>
                          <span className="col-11">Company Data</span>
                          <span className="col-1" onClick={()=>this.editClick(1)}><FaEdit className="editIcon"/></span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">BPN</span>
                          <span className="col-6">BPNL890867291</span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">Legal Entity Name</span>
                          <span className="col-6">German Car Factory</span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">Registered Name</span>
                          <span className="col-6"></span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">Street</span>
                          <span className="col-6">Munich Street</span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">PLZ / City</span>
                          <span className="col-6">80807 Munich</span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">Country</span>
                          <span className="col-6">Germany</span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">State of activity</span>
                          <span className="col-6"></span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">Valid from</span>
                          <span className="col-6"></span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-6">Valid till</span>
                          <span className="col-6"></span>
                        </Row>
                      </li>
                     
                    </ul>
                  </Row>
                  <Row>
                  <ul className="list-group-cax px-2">
                      <li className="list-group-item-cax list-header">
                        <Row>
                          <span className="col-11">Active Role</span>
                          <span className="col-1" onClick={()=>this.editClick(2)}><FaEdit className="editIcon"/></span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-12">Active participant</span>
                        </Row>
                      </li>
                      </ul>
                  </Row>
                  <Row>
                  <ul className="list-group-cax px-2">
                      <li className="list-group-item-cax list-header">
                        <Row>
                          <span className="col-11">Uploaded certificates</span>
                          <span className="col-1" onClick={()=>this.editClick(3)}><FaEdit  className="editIcon"/></span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-12">certificate of approval.pdf</span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-12">certificate of incorporation.pdf</span>
                        </Row>
                      </li>
                      <li className="list-group-item-cax">
                        <Row>
                          <span className="col-12">certificate of deposit.pdf</span>
                        </Row>
                      </li>
                      </ul>
                  </Row>
                </div>
              </div>
            )}
            <div className="mx-auto col-9 info">
              <Row>
                <div className="col12 d-flex align-items-center justify-content-center">
                  Please enter all the required information before proceeding.
                  More information in our <a href=""> help section</a>.
                </div>
                <div className="col12 d-flex align-items-center justify-content-center button-section">
                  <Button
                    styleClass="button btn-default"
                    label="Back"
                    handleClick={() => this.backClick()}
                  />
                  <Button
                    label="Confirm"
                    styleClass="button btn-primaryCax"
                    handleClick={() => this.nextClick()}
                  />
                </div>
              </Row>
            </div>
          </Col>
        </Row>
        <Footer />
        <ReactTooltip />
      </Container>
    );
  }
}
export default withTranslation()(RegistrationCax);
