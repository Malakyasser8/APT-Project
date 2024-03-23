import React, { useState } from "react";
import { Editor } from "react-draft-wysiwyg";
import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import { EditorState } from "draft-js";
import { render } from "react-dom";

function TextEditor() {
  const [editorState, setEditorState] = useState(EditorState.createEmpty());

  const onEditorStateChange = (editorState) => {
    setEditorState(editorState);
  };

  const toolbarOptions = {
    options: [
      "inline",
      "blockType",
      "fontSize",
      "fontFamily",
      "list",
      "textAlign",
      "colorPicker",
      "link",
      "embedded",
      "emoji",
      "remove",
      "history",
    ],
    inline: {
      options: ["bold", "italic", "underline", "strikethrough"],
    },
    blockType: {
      options: [
        "Normal",
        "H1",
        "H2",
        "H3",
        "H4",
        "H5",
        "H6",
        "Blockquote",
        "Code",
      ],
    },
    fontFamily: {
      options: [
        "Arial",
        // "Georgia",
        // "Impact",
        // "Tahoma",
        // "Times New Roman",
        // "Verdana",
      ],
    },
    fontSize: {
      options: [8, 10, 12, 14, 18, 24, 30, 36],
    },
    list: {
      options: ["unordered", "ordered"],
    },
    textAlign: {
      options: ["left", "center", "right", "justify"],
    },
  };

  return (
    <div>
      <Editor
        editorState={editorState}
        toolbarClassName="toolbarClassName"
        wrapperClassName="wrapperClassName"
        editorClassName="editorClassName"
        onEditorStateChange={onEditorStateChange}
        toolbar={toolbarOptions}
      />
    </div>
  );
}

export default TextEditor;
