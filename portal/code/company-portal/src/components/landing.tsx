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
import { Container, Row, Col } from "react-bootstrap";
import Header from "./cax-header";
import Footer from "./footer";
import { withTranslation, WithTranslation } from "react-i18next";
import Button from "./button";
import { RouteComponentProps } from "react-router-dom";
import BulletList from "./bulletList"

@observer
class Landing extends React.Component<
  WithTranslation & RouteComponentProps,
  any
> {
  private onClick(): void {
    this.props.history.push("/registration");
  }

  public render() {
    return (
      <Container>
        <Header href={window.location.href} />
        <Row>
          <Col>
            <div className="mx-auto col-9 container-body">
              <h4>{this.props.t("landing.greetingMsg")}</h4>
              <h6 className="col-8">{this.props.t("landing.heading1")}</h6>
              <Row className="content">
                <Col>
                <BulletList text={this.props.t("landing.point1")}/>
                <BulletList text={this.props.t("landing.point2")}/>
                <BulletList text={this.props.t("landing.point3")}/>
                  <Button
                    label={this.props.t("landing.buttonText1")}
                    styleClass="button btn-primaryCax"
                    handleClick={() => this.onClick()}
                  />
                </Col>
                <Col className="d-flex align-items-center justify-content-center">
                  <img src="/ID_Card.png" alt="" />
                </Col>
              </Row>
            </div>
            <div className="mx-auto col-9 d-flex align-items-center justify-content-center info small-info">
              <span className="">
                {this.props.t("landing.footerText1")}{" "}
                <a href="">{this.props.t("landing.footerText2")}</a>.
              </span>
            </div>
          </Col>
        </Row>
        <Footer />
      </Container>
    );
  }
}
export default withTranslation()(Landing);
