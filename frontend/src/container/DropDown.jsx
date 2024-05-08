import {
  Menu,
  MenuHandler,
  MenuList,
  MenuItem,
  Button,
} from "@material-tailwind/react";
import { useMutation } from "react-query";
import { useState } from "react";
// import { useNavigate } from "react-router-dom";
import { postRequest } from "../API/User";
import RenameShareModal from "../components/RenameShareModal";
export function DropDown({ documentId, refetch, owned }) {
  const mutation = useMutation(postRequest);
  const handleDelete = () => {
    console.log("fileid", documentId);
    mutation.mutate(
      {
        endPoint: `api/files/delete/${documentId}`,
        data: { fileId: documentId },
      },
      {
        onSuccess: () => {
          console.log("success");
          refetch();
        },
      }
    );
  };
  const [openRSModal, setOpenRSModal] = useState(false);
  const handleOpenRSModal = () => setOpenRSModal(!openRSModal);

  return (
    <>
      <Menu>
        <MenuHandler>
          <Button className="bg-black text-white px-4 py-2   focus:outline-none focus:ring focus:border-blue-300">
            ...
          </Button>
        </MenuHandler>
        <MenuList className="bg-black text-white">
          <MenuItem onClick={handleOpenRSModal}>Rename</MenuItem>
          {owned && <MenuItem onClick={handleDelete}>Delete</MenuItem>}
          <MenuItem>Share</MenuItem>
        </MenuList>
      </Menu>
      <RenameShareModal
        handleOpen={handleOpenRSModal}
        open={openRSModal}
        endpoint={`api/files/rename/${documentId}`}
        rename={true}
        refetch={refetch}
      />
    </>
  );
}
