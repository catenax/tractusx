import { render } from "@testing-library/react";
import { observer } from "mobx-react";
import { observable } from "mobx";
import React, { Component, useCallback } from "react";
import Dropzone from "react-dropzone";
import { withTranslation, WithTranslation } from 'react-i18next';
import { toJS } from "mobx";

class File {
  name: string;
  size: string;
  type: string;
  path: string;
}
@observer
class DragDropUploadFiles extends React.Component<WithTranslation> {
  
  selectedFiles: File[] = observable.array();
  @observable currentFile: File[];
  @observable progress = 0;
  @observable message = "";
  @observable fileInfos: File[] = [];


  onDrop(files: any[]) { 
    if (files.length > 0) {
      console.log(files);
      console.log(typeof files);
      this.selectedFiles.push(...files);
    }
    this.selectedFiles = toJS(this.selectedFiles);
    console.log(this.selectedFiles);
  }

  upload() {}
  render() {
    return (
      <div className="mx-auto col-9 container-registration">
        <div className="head-section">
          <div className="mx-auto step-highlight d-flex align-items-center justify-content-center">
            4
          </div>
          <h4 className="mx-auto d-flex align-items-center justify-content-center">
          {this.props.t('documentUpload.title')}
          </h4>
          <div className="mx-auto text-center col-9">
          {this.props.t('documentUpload.subTitle')}
          </div>
        </div>
        <div className="companydata-form mx-auto col-9">
          <div>
            {this.currentFile && (
              <div className="progress mb-3">
                <div
                  className="progress-bar progress-bar-info progress-bar-striped"
                  role="progressbar"
                  aria-valuenow={this.progress}
                  aria-valuemin={0}
                  aria-valuemax={100}
                  style={{ width: this.progress + "%" }}
                >
                  {this.progress}%
                </div>
              </div>
            )}

            <Dropzone onDrop={(e) => this.onDrop(e)} multiple={true}>
              {({ getRootProps, getInputProps }) => (
                <section>
                  <div {...getRootProps({ className: "dropzone" })}>
                    <input {...getInputProps()} />
                    <span>
                    {this.props.t('documentUpload.dragDropMessage')}
                    </span>
                  </div>

                  {this.selectedFiles &&
                  Array.isArray(this.selectedFiles) &&
                  this.selectedFiles.length ? (
                    <div className="selected-file">
                      {this.selectedFiles.map((file) => file.name).join(", ")}
                    </div>
                  ) : (
                    ""
                  )}
                  <div className="selected-file-wrapper">
                    <button
                      className="btn btn-success"
                      disabled={!this.selectedFiles}
                      onClick={this.upload}
                    >
                    {this.props.t('documentUpload.upload')}
                    </button>
                  </div>
                </section>
              )}
            </Dropzone>

            <div className="alert alert-light" role="alert">
              {this.message}
            </div>

            {this.fileInfos.length > 0 && (
              <div className="card">
                <div className="card-header">List of Files</div>
                <ul className="list-group list-group-flush">
                  {this.fileInfos.map((file, index) => (
                    <li className="list-group-item" key={index}>
                      <a href={file.path}>{file.name}</a>
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
}
export default withTranslation()(DragDropUploadFiles);
