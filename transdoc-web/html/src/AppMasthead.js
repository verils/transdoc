import React from 'react';

const AppMasthead = ({ minHeight, onUploadButtonClick }) => (
  <div className="ui inverted vertical masthead center aligned segment" style={{minHeight}}>
    <div className="ui container">
      <div className="ui secondary inverted pointing menu">
        <a className="active item" href="/">Transdoc</a>
      </div>
    </div>
    <div className="ui text container">
      <div className="flex-middle">
        <h1 className="ui inverted header">A Simple Tool</h1>
        <h2>Help you to translate Office Word Files to Markdown</h2>
        <div className="ui huge primary button" onClick={onUploadButtonClick}><i className="upload icon"></i> Upload</div>
      </div>
    </div>
  </div>
)

export default AppMasthead;
