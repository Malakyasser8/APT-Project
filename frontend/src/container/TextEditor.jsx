import React, { useState, useRef, useEffect } from "react";
import Quill from "quill";
import "quill/dist/quill.snow.css"; // Import Quill styles

let webSocket;
let oldContent = "";
let version = 0;
let changesQueue = [];

const TextEditor = ({ fileContent }) => {
  const [quill, setQuill] = useState(fileContent);
  // const quillRef = useRef(null);

  useEffect(() => {
    if (!quill) {
      const editor = new Quill("#editor-container", {
        theme: "snow",
        placeholder: "Write something...",
      });
      setQuill(editor);
    }
    // Setup WebSocket connection on component mount (equivalent to window.onload)
    setupWebSocket();
    oldContent = "";
    return (editor) => {
      if (webSocket && webSocket.readyState === WebSocket.OPEN) {
        webSocket.close();
      }
      if (editor) {
        editor.off("text-change");
        editor.removeAllListeners();
        editor.destroy();
      }
    };
  }, [fileContent]); // Empty dependency array to run only once on mount (window load)

  useEffect(() => {
    if (quill) {
      // Handle text change in Quill editor
      quill.on("text-change", (delta, oldDelta, source) => {
        if (source === "user") {
          handleTextChange(delta);
        }
      });
    }
  }, [quill]); // Run effect when quill instance changes

  function setupWebSocket() {
    const url = "ws://localhost:3001/chat";
    webSocket = !webSocket ? new WebSocket(url) : webSocket;
    // quill = quillRef.current.getEditor();

    webSocket.onopen = function (event) {
      console.log("WebSocket connection established");
      // console.log("WebSocket", webSocket);
    };
    webSocket.onmessage = function (event) {
      const data = JSON.parse(event.data);
      console.log("recieved", data);
      // data.forEach((change) => {
      // });
      if (!data.isAck) {
        changesQueue.forEach(function (change) {
          if (data.index > change.index) {
            // console.log("ADDED 1", data.index);
            data.index = data.index + 1;
          }
        });
        modifyCharInText(
          data.index,
          data.character,
          data.isDelete,
          data.isBold,
          data.isItalic,
          data.isInsert
        );
      } else {
        changesQueue = changesQueue.filter((change) => {
          return !(
            change.index == data.index &&
            change.character == data.character &&
            change.isDelete == data.isDelete &&
            change.isBold == data.isBold &&
            change.isItalic == data.isItalic &&
            change.isInsert == data.isInsert
          );
        });
      }
      version++;
      oldContent = quill.root.innerHTML;
    };

    webSocket.onclose = function (event) {
      console.log("WebSocket connection closed");
    };
  }

  function modifyCharInText(
    index,
    character,
    isDelete,
    isBold,
    isItalic,
    isInsert
  ) {
    console.log("HII");

    if (isBold && !isInsert) {
      quill.formatText(index, 1, "bold", true);
    } else if (isItalic && !isInsert) {
      quill.formatText(index, 1, "italic", true);
    } else {
      let text = quill.root.innerHTML.replace(/<\/?[^>]+(>|$)/g, "");
      let length = text.length;
      let formatsArray = [];
      for (let i = 0; i < length; i++) {
        formatsArray.push({ char: text[i], format: quill.getFormat(i, 1) });
      }
      if (isDelete) {
        text = text.slice(0, index) + text.slice(index + 1);
        formatsArray.splice(index, 1);
      } else {
        text = text.slice(0, index) + character + text.slice(index);
        formatsArray.splice(index, 0, {
          format: { italic: isItalic, bold: isBold },
        });
      }
      quill.setText(text, "api");
      length = text.length;
      for (let i = 0; i < length; i++) {
        quill.formatText(i, 1, formatsArray[i].format);
      }
    }

    // Set the cursor position to the end of the editor
    quill.setSelection(index + 1);
  }

  function findChanges(isDelete, isBold, isItalic, isInsert, currentIndex) {
    // const quill = quillRef.current.getEditor();
    const newContent = quill.root.innerHTML;
    const strippedOldContent = oldContent.replace(/<\/?[^>]+(>|$)/g, ""); //mal
    const strippedNewContent = newContent.replace(/<\/?[^>]+(>|$)/g, ""); //ml
    let character = strippedNewContent[currentIndex]; //abc
    if (currentIndex >= strippedNewContent.length)
      character = strippedOldContent[currentIndex];
    let changes = [];
    changes.push({
      index: currentIndex,
      character,
      isDelete,
      isAck: false,
      isBold,
      isItalic,
      isInsert,
      version,
    });
    oldContent = quill.root.innerHTML;
    return changes;
  }

  function handleTextChange(
    isDelete,
    isBold,
    isItalic,
    isInsert,
    currentIndex
  ) {
    const changes = findChanges(
      isDelete,
      isBold,
      isItalic,
      isInsert,
      currentIndex
    );
    if (changes.length > 0) {
      console.log("changes", changes);
      // console.log("webSocket", webSocket);
      webSocket.send(JSON.stringify(changes[0]));
      changesQueue.push(changes[0]);
    }
  }

  const handleEditorChange = (html) => {
    // const cleanText =
    //   new DOMParser().parseFromString(html, "text/html").body.textContent || "";
    // setEditorHtml(html);
    // const quill = quillRef.current?.getEditor();
    if (quill) {
      // Attach event listener to detect text change in Quill editor
      quill.on("text-change", function (delta, oldDelta, source) {
        console.log("DELTA", delta.ops);
        let isDelete = false;
        let isInsert = false;
        let isBold = false;
        let isItalic = false;
        let currentIndex = 0;
        delta.ops.forEach((operation) => {
          if (operation.delete) {
            isDelete = true;
          }
          if (operation.retain && !operation.attributes) {
            currentIndex += operation.retain;
          }
          if (operation.insert) {
            isInsert = true;
          }
          if (operation.attributes?.bold) {
            isBold = true;
          }
          if (operation.attributes?.italic) {
            isItalic = true;
          }
        });
        if (source == "user" && source != "api") {
          console.log("in handle change");
          handleTextChange(isDelete, isBold, isItalic, isInsert, currentIndex);
          oldContent = quill.root.innerHTML;
        }
      });
    }
  };

  return (
    <>
      <div className="flex flex-col mx-10 w-full" id="editor-container"></div>
    </>
  );
};

export default TextEditor;
