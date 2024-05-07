import {
  Dialog,
  DialogBody,
  DialogFooter,
  DialogHeader,
  Button
} from "@material-tailwind/react";
import React, { useState } from "react";
import { postRequest } from "../API/User";
import { XMarkIcon } from "@heroicons/react/24/outline";

export default function CreateDocumentModal({ handleOpen, open}) {
  const [text, setText] = useState("");
  const handleChange = (event) => {
    const newText = event.target.value;
    setText(newText);
  };
  const handleSaveButton = () => {
    postRequest({
      endPoint: "api/files/create",
      data: {
        filename:text
      },
    });
    handleOpen();
  };
  return (
    <>
      <Dialog size="sm" open={open} handler={handleOpen} className="pb-5">
        <DialogHeader className=" text-center  justify-between border-b border-lines-color">
          <h2 className="pl-36">Enter File name</h2>
          <XMarkIcon
            strokeWidth={3.5}
            className="h-[20px] w-[20px]"
            onClick={handleOpen}
          />
        </DialogHeader>
        <DialogBody className="flex flex-col gap-2">
          <textarea
            placeholder="Title"
            className=" resize-none !border rounded border-[#EDEFF1] bg-white text-gray-900  shadow-none ring-4 ring-transparent placeholder:text-gray-500 w-full p-2 pt-3 h-20"
            onChange={handleChange}
            maxLength={40}
            value={text}
          />
          <div>{text.length}/40</div>
        </DialogBody>
        <DialogFooter>
          <Button
            className="block px-4 py-2 bg-black text-white rounded-md hover:bg-black mt-6 text-center"
            onClick={handleSaveButton}
          >
            Save
          </Button>
         
        </DialogFooter>
      </Dialog>
    </>
  );
}
