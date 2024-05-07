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
export function DocumentList({ endpoint }) {
  const fetchReq = useMutation(fetchRequest);
  const [response, setResponse] = useState();
  const navigate = useNavigate();
  useEffect(() => {
    fetchReq.mutate(endpoint, {
      onSuccess: (data) => {
        console.log("data", data.data);
        setResponse(data.data);
      },
      onError: (err) => {},
    });
  }, []);
  
  return (
    <Card className="w-96">
      <List>
        {response && (
          <>
            {response.map((document) => (
              <ListItem
                key={document._id}
                ripple={false}
                className="py-1 pr-1 pl-4"
                onClick={() => navigate(`/document/${document._id}`)}
              >
                {document.filename}
                <ListItemSuffix>
                  <IconButton variant="text" color="blue-gray">
                    <DropDown />
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
// {
//   response && (
//     <>
//       {response.map((comment: CommentType) => (
//         <Comment key={comment._id} comment={comment} showButton={true} />
//       ))}
//     </>
//   );
// }
