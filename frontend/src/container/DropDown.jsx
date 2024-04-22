import {
  Menu,
  MenuHandler,
  MenuList,
  MenuItem,
  Button,
} from "@material-tailwind/react";

export function DropDown() {
  return (
    <Menu>
      <MenuHandler>
        <Button className="bg-white text-black px-4 py-2 rounded hover:bg-gray-200 focus:outline-none focus:ring focus:border-blue-300">
          ...
        </Button>
      </MenuHandler>
      <MenuList>
        <MenuItem>Rename</MenuItem>
        <MenuItem>Delete</MenuItem>
        <MenuItem>Share</MenuItem>
      </MenuList>
    </Menu>
  );
}
