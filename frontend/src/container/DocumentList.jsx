import {
  List,
  ListItem,
  ListItemSuffix,
  Card,
  IconButton,
} from "@material-tailwind/react";
import { DropDown } from "./DropDown";
import { useMutation } from "react-query";
import { useEffect, useState } from "react";
import { fetchRequest } from "../API/User";
import { useNavigate } from "react-router-dom";
export function DocumentList({ endpoint ,owned }) {
  const fetchReq = useMutation(fetchRequest);
  const [response, setResponse] = useState();
  const [refetchKey, setRefetchKey] = useState(0);
  const navigate = useNavigate();
  useEffect(() => {
    fetchReq.mutate(endpoint, {
      onSuccess: (data) => {
        console.log("data", data.data);
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
              <ListItem
                key={document._id}
                ripple={false}
                className="py-1 pr-1 pl-4 text-white hover:bg-black"
              >
                <div onClick={() => navigate(`/document/${document._id}`)}>
                  {document.filename}
                </div>
                <ListItemSuffix>
                  <IconButton variant="text" color="blue-gray">
                    <DropDown documentId={document._id} refetch={triggerRefetch}  owned={owned}/>
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

