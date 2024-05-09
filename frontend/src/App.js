import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import SignUp from "./pages/SignUp";
import Login from "./pages/Login";
import Homepage from "./pages/HomePage";
import Document from "./pages/Document";
import {
  // MutationCache,
  // QueryCache,
  QueryClient,
  QueryClientProvider,
} from 'react-query';
function App() {
  const queryClient = new QueryClient({
    
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false,
        refetchOnMount: false,
      },
      mutations: {
        onError: (error) => {
          console.log('hiiii mutation');

        },
      },
    },
  });
  return (
    <div className="App">
      <QueryClientProvider client={queryClient}>
      <Router>
        <Routes>
          <Route path="/signup" element={<SignUp />} />
          <Route path="/" element={<Login />} />
          <Route path="/homepage" element={<Homepage />} />
          <Route path="/document/:id" element={<Document />} />
        </Routes>
      </Router>
      </QueryClientProvider>
    </div>
  );
}

export default App;
