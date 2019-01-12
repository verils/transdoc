import React, { Component } from 'react';
import AppMasthead from './AppMasthead';
import AppFileList from './AppFileList';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      isProcessDone: true,
      isProcessSuccess: false,
      files: []
    };

    this.protocol = window.location.protocol;
    this.hostname = window.location.hostname;
    this.port = '8080';

    this.uploadWordDocuments = this.uploadWordDocuments.bind(this);
    this.initFileList = this.initFileList.bind(this);
    this.uploadAllFiles = this.uploadAllFiles.bind(this);
    this.uploadFile = this.uploadFile.bind(this);

    this.handleFilesDrop = this.handleFilesDrop.bind(this);
    this.handleUploadButtonClick = this.handleUploadButtonClick.bind(this);
    this.handleDownloadButtonClick = this.handleDownloadButtonClick.bind(this);
  }

  componentWillMount() {
    this.screenHeight = window.innerHeight
      || document.documentElement.clientHeight
      || document.body.clientHeight;
  }

  componentDidMount() {
    const bodyElement = document.body;
    bodyElement.addEventListener('dragenter', (e) => e.preventDefault(), false);
    bodyElement.addEventListener('dragover', (e) => e.preventDefault(), false);
    bodyElement.addEventListener('dragleave', (e) => e.preventDefault(), false);
    bodyElement.addEventListener('drop', (e) => e.preventDefault(), false);
  }

  uploadWordDocuments(files) {
    if (this.state.isProcessDone) {
      this.initFileList(files);
      window.location.href = '#filelist';
      this.uploadAllFiles(files);
    }
  }

  initFileList(files) {
    this.setState({
      isProcessDone: true,
      isProcessSuccess: false,
      files
    });
  }

  uploadAllFiles(files) {
    const self = this;
    for (let i = 0; i < files.length; i++) {
      files[i].isLoading = true;
      files[i].message = '正在准备...';
      this.setState({ files });
    }
    fetch(`${this.protocol}//${this.hostname}:${this.port}/transdoc/fetchToken`)
    .then(resp => resp.text())
    .then(token => {
      self.token = token;
      for (let i = 0; i < files.length; i++) {
        files[i].token = token;
        this.uploadFile(files, i);
      }
      const intervalId = setInterval(() => {
        let isAllProccess = true;
        for (let i = 0; i < files.length; i++) {
          if (files[i].isLoading) {
            isAllProccess = false;
            break;
          }
        }
        if (isAllProccess) {
          this.setState({
            isProcessDone: true,
            isProcessSuccess: true,
            files
          });
          clearInterval(intervalId);
        }
      }, 100);
    })
    .catch(() => {
      for (let i = 0; i < files.length; i++) {
        files[i].isLoading = false;
        files[i].message = '上传失败：无法连接到服务器';
      }
      self.setState({
        isProcessDone: true,
        isProcessSuccess: false,
        files
      });
    });
  }

  uploadFile(files, i) {
    const file = files[i];
    const xhr = new XMLHttpRequest();
    xhr.open('POST', `${this.protocol}//${this.hostname}:${this.port}/transdoc/upload`, true);
    xhr.upload.onprogress = (evt) => {
      if (evt.lengthComputable) {
        let speed = (evt.loaded - file.loaded) / (Date.now() - file.timestamp) * 1000;
        file.message = `正在上传...${Math.round(evt.loaded / evt.total * 100)}%`;
        file.timestamp = Date.now();
        file.loaded = evt.loaded;
        file.speed = speed;
        this.setState({ files });
      }
    };
    xhr.onreadystatechange = () => {
      if (xhr.readyState === 4) {
        let message;
        if (xhr.status === 200) {
          var resp = JSON.parse(xhr.response);
          if (resp.success) {
            message = '处理完成';
          } else {
            message = `处理失败${resp.message ? '，' + resp.message : ''}`;
          }
        } else {
          message = '上传失败';
        }
        file.isLoading = false;
        file.message = message;
        this.setState({ files });
      }
    };
    const formData = new FormData();
    formData.append('token', file.token);
    formData.append('file', file);
    xhr.send(formData);
  }

  handleFilesDrop(evt) {
    evt.preventDefault()
    const dropFiles = evt.dataTransfer.files;
    const files = [];
    for (let i = 0; i < dropFiles.length; i++) {
      if (dropFiles[i].type !== 'application/msword'
        || dropFiles[i].type !== 'application/vnd.openxmlformats-officedocument.wordprocessingml.document') {
        files.push(dropFiles[i]);
      }
    }
    this.uploadWordDocuments(files);
  }

  handleUploadButtonClick() {
    const input = document.createElement('input');
    input.type = input.name = 'file';
    input.multiple = true;
    input.accept = 'application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document';
    input.onchange = () => {
      const selectFiles = input.files;
      const files = [];
      for (let i = 0; i < selectFiles.length; i++) {
        files.push(selectFiles[i]);
      }
      this.uploadWordDocuments(files);
    }
    input.click();
  }

  handleDownloadButtonClick() {
    var token = this.token;
    fetch(`${this.protocol}//${this.hostname}:${this.port}/transdoc/checkToken?token=${token}`)
    .then(resp => resp.json())
    .then(success => {
      if (success) {
        window.location.href = `${this.protocol}//${this.hostname}:${this.port}/transdoc/download?token=${token}`;
      }
    });
  }

  render() {
    const hasFiles = this.state.files.length > 0;
    return (
      <div onDrop={this.handleFilesDrop}>
        <AppMasthead
          minHeight={this.screenHeight}
          onUploadButtonClick={this.handleUploadButtonClick}
        />
        { hasFiles && (
          <AppFileList
            minHeight={this.screenHeight}
            files={this.state.files}
            isProcessDone={this.state.isProcessDone}
            isProcessSuccess={this.state.isProcessSuccess}
            onUploadButtonClick={this.handleUploadButtonClick}
            onDownloadButtonClick={this.handleDownloadButtonClick}
          />
        ) }
      </div>
    );
  }
}

export default App;
