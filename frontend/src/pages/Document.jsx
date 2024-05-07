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
    navigate("/login");
  };
  const [openCDModal, setOpenCDModal] = useState(false);
  const handleOpenCDModal = () => setOpenCDModal(!openCDModal);
  const fetchReq = useMutation(fetchRequest);
  const { id } = useParams();
  const [response, setResponse] = useState("");
  useEffect(() => {
    fetchReq.mutate(`api/files/open/${id}`, {
      onSuccess: (data) => {
        setResponse(data.data.content);
        console.log("fileopened", data.data.content);
      },
      onError: (err) => {},
    });
  }, [id]);

  return (
    <div>
      <div className="my-2 mb-8">
        <Navbar className="sticky  h-max max-w-full rounded-none px-4 py-2 lg:px-8 lg:py-4">
          <div className="flex items-center justify-between text-blue-gray-900">
            <Typography className="mr-4 cursor-pointer py-1.5 font-semibold text-lg" onClick={()=>{navigate('/homepage')}}>
              Ree Docs
            </Typography>
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
      <TextEditor fileContent={response || ""} />
      <CreateDocumentModal open={openCDModal} handleOpen={handleOpenCDModal} />
    </div>
  );
}

export default Document;
