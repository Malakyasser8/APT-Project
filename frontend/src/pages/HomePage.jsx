import React, { useState } from "react";
import { DocumentList } from "../container/DocumentList";
import { DocumentPlusIcon } from "@heroicons/react/24/outline";
import { useNavigate } from "react-router-dom";
import { useMutation } from "react-query";
import { postRequest } from "../API/User";
import { Navbar, Typography, Button } from "@material-tailwind/react";
import CreateDocumentModal from "../components/CreateDocumentModal";
function Homepage() {
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
  return (
    <div>
      <div className="my-2">
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
      <div className="flex justify-between items-center my-20 mx-14 ">
        <div>
          <Typography className="mb-8 font-semibold text-lg">
            Your Documents
          </Typography>
          <DocumentList endpoint="api/files/owned" owned={true} />
        </div>
        <div>
        <Typography className="mb-8 font-semibold text-lg">
            Shared Documents
          </Typography>
          <DocumentList endpoint="api/files/shared" owned={false} />
        </div>
      </div>
      <CreateDocumentModal open={openCDModal} handleOpen={handleOpenCDModal} />
    </div>
  );
}

export default Homepage;
