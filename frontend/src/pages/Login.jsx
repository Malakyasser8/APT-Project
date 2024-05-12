import { Card, Input, Button, Typography } from "@material-tailwind/react";
import { Link, useNavigate } from "react-router-dom";
import { useMutation } from "react-query";
import { saveToken, saveUsername } from "../utils/tokens_helper";
import { postRequest } from "../API/User";
import { useState } from "react";

function Login() {
  const navigate = useNavigate();
  const [errormessage, setErrorMessage] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [usernameError, setUsernameError] = useState(false);
  const [passwordError, setPasswordError] = useState(false);
  const mutation = useMutation(postRequest, {
    onSuccess: (response) => {
      const { token } = response;
      saveToken(token);
      saveUsername(username)
      navigate(`/homepage`);
    },
    onError: (error) => {
      setErrorMessage(true);
    },
  });
  const handleOnSubmit = () => {
    if (username == "") {
      setUsernameError(true);
    } else if (password == "") {
      setPasswordError(true);
      setUsernameError(false);
    } else {
      setPasswordError(false);
      setUsernameError(false);
      mutation.mutate({
        endPoint: "api/users/login",
        data: { username: username, password: password },
      });
    }
  };
  return (
    <div className="flex justify-center items-center h-screen">
      <Card color="transparent" shadow={false}>
        <Typography variant="h4" color="blue-gray">
          Login
        </Typography>
        <Typography color="gray" className="mt-1 font-normal">
          Nice to meet you! Enter your details to login.
        </Typography>
        <form className="mt-8 mb-2 w-80 max-w-screen-lg sm:w-96">
          <div className="mb-1 flex flex-col gap-6">
            <Typography variant="h6" color="blue-gray" className="-mb-3">
              Your Username
            </Typography>
            <Input
              size="lg"
              placeholder="username"
              className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              labelProps={{
                className: "before:content-none after:content-none",
              }}
              onChange={(e) => setUsername(e.target.value)}
            />
            {usernameError && (
              <Typography color="red" className="mb-3">
                Enter Username
              </Typography>
            )}
            <Typography variant="h6" color="blue-gray" className="mb-3">
              Password
            </Typography>
            <Input
              type="password"
              size="lg"
              placeholder="********"
              className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              labelProps={{
                className: "before:content-none after:content-none",
              }}
              onChange={(e) => setPassword(e.target.value)}
            />
             {passwordError && (
              <Typography color="red" className="mb-3">
                Enter Password
              </Typography>
            )}
          </div>
          <Button
            className="block px-4 py-2 bg-black text-white rounded-md hover:bg-black mt-6 text-center"
            onClick={handleOnSubmit}
          >
            Login
          </Button>
          <Typography color="gray" className="mt-4 text-center font-normal">
            Don't have an account?{" "}
            <Link to="/signup" className="font-medium text-gray-900">
              Sign Up
            </Link>
          </Typography>
          {errormessage && (
            <Typography
              color="red"
              className="mt-4 text-center font-normal text-xl"
            >
              Invalid Username or Password
            </Typography>
          )}
        </form>
      </Card>
    </div>
  );
}

export default Login;
