import React, { useState, useRef, useEffect } from "react";
import Quill from "quill";
import "quill/dist/quill.snow.css"; // Import Quill styles
import { useMutation } from "react-query";
import { postRequest } from "../API/User";

let webSocket;
let oldContent = "";
let version = 0;
let changesQueue = [];

const TextEditor = ({ fileContent, fileId }) => {
  const quillRef = useRef(null);

  const saveMutation = useMutation(
    (newContent) =>
      postRequest({
        endPoint: `api/files/save/${fileId}`,
        data: { content: newContent },
      }),
    {
      onSuccess: () => {},
      onError: () => {
        console.log("Error in save mutation");
      },
    }
  );

  useEffect(() => {
    if (!quillRef.current) {
      const editor = new Quill("#editor-container", {
        theme: "snow",
        placeholder: "Write something...",
      });
      if (fileContent) {
        editor.root.innerHTML = fileContent;
      }
      quillRef.current = editor;
    }

    if (quillRef.current) {
      quillRef.current.on("text-change", function (delta, oldDelta, source) {
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
          if (
            operation.attributes &&
            operation.attributes.hasOwnProperty("bold")
          ) {
            const boldValue = operation.attributes.bold;
            if (boldValue == true) {
              isBold = true;
            } else if (boldValue == false) {
              isBold = false;
            } else {
              isBold = null;
            }
          }
          if (
            operation.attributes &&
            operation.attributes.hasOwnProperty("italic")
          ) {
            const val = operation.attributes.italic;
            if (val == true) {
              isItalic = true;
            } else if (val == false) {
              isItalic = false;
            } else {
              isItalic = null;
            }
          }
        });
        saveMutation.mutate(quillRef.current.root.innerHTML);
        if (source == "user" && source != "api") {
          console.log("in handle change");
          handleTextChange(isDelete, isBold, isItalic, isInsert, currentIndex);
          oldContent = quillRef.current.root.innerHTML;
        }
      });
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
  }, []); // Empty dependency array to run only once on mount (window load)
  function setupWebSocket() {
    const url = `ws://192.168.8.102:3001/chat/${fileId}`;
    webSocket = !webSocket ? new WebSocket(url) : webSocket;
    // quill = quillRef.current.getEditor();

    // if (!webSocket || webSocket.readyState === WebSocket.CLOSED) {
    //   console.log("WebSocket connection closed or not found");
    // }
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
      oldContent = quillRef.current.root.innerHTML;
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
    console.log("HII", isBold);

    if (isBold == null) {
      quillRef.current.formatText(index, 1, "bold", false);
    } else if (isItalic == null) {
      quillRef.current.formatText(index, 1, "italic", false);
    } else if (isBold && !isInsert) {
      quillRef.current.formatText(index, 1, "bold", true);
    } else if (isItalic && !isInsert) {
      quillRef.current.formatText(index, 1, "italic", true);
    } else {
      let text = quillRef.current.root.innerHTML.replace(/<\/?[^>]+(>|$)/g, "");
      let length = text.length;
      let formatsArray = [];
      for (let i = 0; i < length; i++) {
        formatsArray.push({
          char: text[i],
          format: quillRef.current.getFormat(i, 1),
        });
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
      quillRef.current.setText(text, "api");
      length = text.length;
      for (let i = 0; i < length; i++) {
        quillRef.current.formatText(i, 1, formatsArray[i].format);
      }
    }

    // Set the cursor position to the end of the editor
    quillRef.current.setSelection(index + 1);
  }

  function findChanges(isDelete, isBold, isItalic, isInsert, currentIndex) {
    // const quill = quillRef.current.getEditor();
    const newContent = quillRef.current.root.innerHTML;
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
    oldContent = quillRef.current.root.innerHTML;
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
      if (webSocket.readyState === WebSocket.OPEN) {
        webSocket.send(JSON.stringify(changes[0]));
      } else {
        console.error('WebSocket connection is not open.');
      }
      
      changesQueue.push(changes[0]);
    }
  }

  return (
    <>
      <div className="flex flex-col mx-10 w-full" id="editor-container"></div>
    </>
  );
};

export default TextEditor;
