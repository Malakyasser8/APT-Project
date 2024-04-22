import { DocumentPlusIcon } from "@heroicons/react/24/outline";
import { Button } from "@material-tailwind/react";
import React from "react";
import { Link } from "react-router-dom";
function CreateDocumentButton() {
  return (
    <Link
      to="/document"
      className="block px-4 py-2 bg-white text-white rounded-md hover:bg-white mt-6 text-center"
    >
      <DocumentPlusIcon color="black" strokeWidth={2.5} className="h-14 w-14" />
    </Link>
  );
}

export default CreateDocumentButton;
