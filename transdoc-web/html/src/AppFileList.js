import React from 'react';

const AppFileList = ({ minHeight, isProcessDone, isProcessSuccess, files, onUploadButtonClick, onDownloadButtonClick }) => (
  <div id="filelist" style={{minHeight}}>
    <div className="ui vertical clearing segment">
      <div className="ui container">
        <button className={`ui primary basic ${isProcessDone ? '' : 'disabled'} button`}
          onClick={onUploadButtonClick}>Upload</button>
        <button className={`ui primary right floated ${(isProcessDone && isProcessSuccess) ? '' : 'disabled'} button`}
          onClick={onDownloadButtonClick}>Download</button>
      </div>
    </div>
    <div className="ui vertical segment">
      <div className="ui container">
        <div className="ui segment">
          <div className="ui very relaxed divided list">
            { files.map((file) => (
              <div className="item" key={file.name}>
                <i className="large sticky note outline middle aligned icon"></i>
                <div className="content">
                  <a className="header">{file.name}</a>
                  <div className="description">
                    <span>{file.message}</span>{' '}
                    { file.isLoading && (<div className="ui tiny active inline loader"></div>) }
                  </div>
                </div>
              </div>
            )) }
          </div>
        </div>
      </div>
    </div>
  </div>
)

export default AppFileList;
