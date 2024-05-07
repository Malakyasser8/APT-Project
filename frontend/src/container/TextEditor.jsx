import React, { useState } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';



const TextEditor = ({ fileContent }) => {
  const [editorHtml, setEditorHtml] = useState(fileContent);

  const handleEditorChange = (html) => {
    const cleanText =
      new DOMParser().parseFromString(html, 'text/html').body.textContent || '';
    setEditorHtml(html);
  };

  return (
    <>
      <div className='flex flex-col'>
        <div className='w-full sm:w-full md:w-full lg:w-full xl:w-full'>
          <ReactQuill
            theme='snow'
            value={editorHtml}
            onChange={handleEditorChange}
            className='w-full h-40 sm:h-20 md:h-40 lg:h-144 xl:h-160 mb-4'
            placeholder='Text (optional) '
          />
        </div>
      </div>
    </>
  );
};

export default TextEditor;
