import React from "react";
import { DocumentList } from "../container/DocumentList";
import CreateDocumentButton from "../container/CreateDocumentButton";
function Homepage() {
  return (
    <div class="flex justify-center items-center h-screen">
      <CreateDocumentButton />
      <DocumentList />
    </div>
  );
}

export default Homepage;
