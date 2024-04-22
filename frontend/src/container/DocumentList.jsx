import {
  List,
  ListItem,
  ListItemSuffix,
  Card,
  IconButton,
} from "@material-tailwind/react";
import { DropDown } from "./DropDown";

export function DocumentList() {
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
        <ListItem ripple={false} className="py-1 pr-1 pl-4">
          Doc 2
          <ListItemSuffix>
            <IconButton variant="text" color="blue-gray">
              <DropDown />
            </IconButton>
          </ListItemSuffix>
        </ListItem>
        <ListItem ripple={false} className="py-1 pr-1 pl-4">
          Doc 3
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
