import {
  List,
  ListItem,
  ListItemSuffix,
  Card,
  IconButton,
} from "@material-tailwind/react";
import { DropDown } from "./DropDown";
import { useMutation } from 'react-query';
import { useEffect, useState } from 'react';
import { fetchRequest } from "../API/User";

export function DocumentList() {
  const fetchReq = useMutation(fetchRequest);
  // useEffect(() => {
  //   fetchReq.mutate('', {
  //     onSuccess: (data) => {
        
  //     },
  //     onError: (err) => {
        
  //     },
  //   });
  // }, []);
  return (
    <Card className="w-96">
      <List>
        <ListItem ripple={false} className="py-1 pr-1 pl-4">
          Doc 1
          <ListItemSuffix>
            <IconButton variant="text" color="blue-gray">
              <DropDown />
            </IconButton>
          </ListItemSuffix>
        </ListItem>
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
