import {
  List,
  ListItem,
  ListItemSuffix,
  Card,
  IconButton,
  ListItemPrefix,
} from "@material-tailwind/react";
import { DropDown } from "./DropDown";
import { useMutation } from "react-query";
import { useEffect, useState } from "react";
import { fetchRequest } from "../API/User";
import { useNavigate } from "react-router-dom";
export function DocumentList({ endpoint, owned }) {
  const fetchReq = useMutation(fetchRequest);
  const [response, setResponse] = useState();
  const [refetchKey, setRefetchKey] = useState(0);
  let isViewer = false;
  const navigate = useNavigate();
  useEffect(() => {
    fetchReq.mutate(endpoint, {
      onSuccess: (data) => {
        setResponse(data.data);
      },
      onError: (err) => {},
    });
  }, [refetchKey]);
  const triggerRefetch = () => {
    // Increment the refetchKey, this will trigger useEffect to refetch data
    setRefetchKey((prevKey) => prevKey + 1);
  };

  return (
    <Card className="w-[500px] bg-black rounded-none">
      <List>
        {response && (
          <>
            {response.map((document) => (
              isViewer = document.viewers.find(user => user.username == localStorage.getItem("username")),
              <ListItem
                key={document._id}
                ripple={false}
                className="!hover:bg-black py-1 pr-1 pl-4 text-white  "
               
                  onClick={() => {
                   navigate(`/document/${document._id}`);
                 }}
                >
                
                  {document.filename}
                
                <ListItemSuffix>
                  <IconButton variant="text" onClick={(e) => e.stopPropagation()}>
                    <DropDown
                      documentId={document._id}
                      refetch={triggerRefetch}
                      owned={!isViewer}
                    />
                  </IconButton>
                </ListItemSuffix>
              </ListItem>
            ))}
          </>
        )}
      </List>
    </Card>
  );
}
