import React, { useState } from "react";
import { FileUploader } from "react-drag-drop-files";

const fileTypes = ["PEM", "CRT", "CER", "KEY"];

function DragDrop() {
  const [file, setFile] = useState(null);
  const handleChange = file => {
    setFile(file);
  };
  return (
    <div className='ml30'>
    <FileUploader handleChange={handleChange} name="file" types={fileTypes} />
    <p>{file ? `File name: ${file.name}` : "no files uploaded yet"}</p>
  </div>
  );
}

export default DragDrop;