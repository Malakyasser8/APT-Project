import React from "react";
import TextEditor from "../container/TextEditor";
import { useMutation } from "react-query";
import { useState, useEffect } from "react";
import { fetchRequest } from "../API/User";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { postRequest } from "../API/User";
import { Navbar, Typography, Button } from "@material-tailwind/react";
import CreateDocumentModal from "../components/CreateDocumentModal";
import { DocumentPlusIcon } from "@heroicons/react/24/outline";
function Document() {
  const navigate = useNavigate();
  const [username, setUsername] = useState(localStorage.getItem("username"));
  const mutation = useMutation(postRequest);
  const handleLogout = () => {
    mutation.mutate({
      endPoint: "api/users/logout",
      data: { username: username },
    });
    localStorage.clear();
    navigate("/");
  };
  const [openCDModal, setOpenCDModal] = useState(false);
  const handleOpenCDModal = () => setOpenCDModal(!openCDModal);
  const fetchReq = useMutation(fetchRequest);
  const { id } = useParams();
  const [response, setResponse] = useState("");
  const [showEditor, setShowEditor] = useState(false);
  const [canEdit, setCanEdit] = useState(true);
  useEffect(() => {
    fetchReq.mutate(`api/files/open/${id}`, {
      onSuccess: (data) => {
        console.log("fileopened", data.data.content);
        if(data.data.viewers.map((viewer) => {
          if(viewer.username == localStorage.getItem("username")) 
            {
              setCanEdit(false)
            }
        }));
        setShowEditor(true);
        setResponse(data.data.content);
      },
      onError: (err) => {},
    });
  }, [id]);

  return (
    <div>
      <div >
        <Navbar className="sticky  h-max max-w-full rounded-none px-4 py-2 lg:px-8 lg:py-4">
          <div className="flex items-center justify-between text-blue-gray-900">
            <Typography
              className="mr-4 cursor-pointer py-1.5 font-semibold text-lg"
              onClick={() => {
                navigate("/homepage");
              }}
            >
              Ree Docs
            </Typography>
            {!canEdit && <div className=" bg-orange-400 rounded p-1 text- w-28 text-center">
              <Typography className="font-semibold">
                View Only
              </Typography>
            </div>}
      
            <div className="flex items-center gap-4">
              <div className="mr-4 lg:block">{username}</div>
              <div onClick={handleOpenCDModal}>
                <DocumentPlusIcon
                  color="black"
                  strokeWidth={2.5}
                  className="h-9 w-9"
                />
              </div>
              <div className="flex items-center gap-x-1">
                <Button
                  size="sm"
                  className=" lg:inline-block"
                  onClick={handleLogout}
                >
                  Log out
                </Button>
              </div>
            </div>
          </div>
        </Navbar>
      </div>
      
   
      {showEditor && <TextEditor fileContent={response || ""} fileId={id} canEdit={canEdit}/>}
      <CreateDocumentModal open={openCDModal} handleOpen={handleOpenCDModal} />
    </div>
  );
}

export default Document;
