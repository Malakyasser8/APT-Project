import {
  Dialog,
  DialogBody,
  DialogFooter,
  DialogHeader,
  Button,
  Typography
} from "@material-tailwind/react";
import React, { useState } from "react";
import { postRequest } from "../API/User";
import { XMarkIcon } from "@heroicons/react/24/outline";

export default function RenameShareModal({
  handleOpen,
  open,
  endpoint,
  rename,
  refetch,
}) {
  const [text, setText] = useState("");
  const handleChange = (event) => {
    const newText = event.target.value;
    setText(newText);
  };
  const handleSaveButton = () => {
    if (rename) {
      postRequest({
        endPoint: endpoint,
        data: {
          filename: text,
        },
        onSuccess: (data) => {
          refetch();
        },
      });
    } else {
      postRequest({
        endPoint: endpoint,
        data: {
          userToShareWith: text,
        },
        onSuccess: (data) => {},
      });
    }
    handleOpen();
  };
  return (
    <>
      <Dialog size="sm" open={open} handler={handleOpen} className="pb-5">
        <DialogHeader className=" text-center  justify-between border-b border-lines-color">
          {rename ? (
            <h2 className="pl-36">Enter new File name</h2>
          ) : (
            <h2 className="pl-36">Enter username</h2>
          )}
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
          {!rename && (
            <>
              <div className="flex flex-col gap-4 mt-5">
                <div className="flex items-center">
                  <input
                    type="radio"
                    id="viewer"
                    name="viewer"
                    value="viewer"
                    //checked={selectedOption === "Required domains"}
                    //onChange={() => setSelectedOption("Required domains")}
                    className="mr-2"
                  />
                  <Typography
                    color="black"
                    className="font-normal flex text-sm gap-2 items-center"
                  >
                    Share as viewer
                  </Typography>
                </div>
                <div className="flex items-center">
                  <input
                    type="radio"
                    id="editor"
                    name="editor"
                    value="editor"
                    //checked={selectedOption === "Blocked domains"}
                    //onChange={() => setSelectedOption("Blocked domains")}
                    className="mr-2"
                  />
                  <Typography
                    color="black"
                    className="font-normal flex text-sm gap-2 items-center"
                  >
                    Share as editor
                  </Typography>
                </div>
              </div>
            </>
          )}
        </DialogBody>
        <DialogFooter>
          <Button
            className="block px-4 py-2 bg-black text-white rounded-md hover:bg-black mt-6 text-center"
            onClick={handleSaveButton}
          >
            {rename ? <>Save</> : <>Share</>}
          </Button>
        </DialogFooter>
      </Dialog>
    </>
  );
}
