import { Card, Input, Button, Typography } from "@material-tailwind/react";
import { Link, useNavigate } from "react-router-dom";
import { useMutation } from "react-query";
import { saveToken } from "../utils/tokens_helper";
import { postRequest } from "../API/User";
import { useState } from "react";

function SignUp() {
  const navigate = useNavigate();
  const [errormessage, setErrorMessage] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [usernameError, setUsernameError] = useState(true);
  const [passwordError, setPasswordError] = useState(true);
  
  const [isValidEmail, setIsValidEmail] = useState(false);

  const handleEmailChange = (e) => {
    const inputEmail = e.target.value;
    setEmail(inputEmail);

    // Regular expression to validate email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    setIsValidEmail(emailRegex.test(inputEmail));
  };
  const handlePasswordChange = (e) => {
    const inputPassword = e.target.value;
    setPassword(inputPassword);

    // Check if password length is at least 8 characters
    setPasswordError(inputPassword.length < 8);
  };
  const handleUsernameChange = (e) => {
    const inputUsername = e.target.value;
    setUsername(inputUsername);

    // Check if password length is at least 8 characters
    setUsernameError(inputUsername.length < 3);
  };
  const mutation = useMutation(postRequest, {
    onSuccess: (response) => {
      const { token } = response;
      saveToken(token);
      navigate(`/`);
    },
    onError: (error) => {
      setErrorMessage(true);
    },
  });
  const handleOnSubmit = () => {
    if (isValidEmail && !passwordError && !usernameError) {
      mutation.mutate({
        endPoint: "api/users/signup",
        data: { username: username, password: password, email: email },
      });
    } 
  };
  return (
    <div class="flex justify-center items-center h-screen">
      <Card color="transparent" shadow={false}>
        <Typography variant="h4" color="blue-gray">
          Sign Up
        </Typography>
        <Typography color="gray" className="mt-1 font-normal">
          Nice to meet you! Enter your details to register.
        </Typography>
        <form className="mt-8 mb-2 w-80 max-w-screen-lg sm:w-96">
          <div className="mb-1 flex flex-col gap-6">
            <Typography variant="h6" color="blue-gray" className="-mb-3">
              Username
            </Typography>
            <Input
              size="lg"
              placeholder="username"
              className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              labelProps={{
                className: "before:content-none after:content-none",
              }}
              onChange={handleUsernameChange}
            />
            {usernameError && (
              <Typography color="gray" className="mb-3">
                Username must be more than 2 characters
              </Typography>
            )}
            <Typography variant="h6" color="blue-gray" className="-mb-3">
              Your Email
            </Typography>
            <Input
              size="lg"
              placeholder="name@mail.com"
              className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
              labelProps={{
                className: "before:content-none after:content-none",
              }}
              onChange={handleEmailChange}
            />
            {!isValidEmail && (
              <Typography color="gray" className="mb-3">
               Enter a valid email address
            </Typography>
            )}
            <Typography variant="h6" color="blue-gray" className="-mb-3">
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
              onChange={handlePasswordChange}
            />
            {passwordError && (
              <Typography color="gray" className="mb-3">
                Password must be min 8 characters
              </Typography>
            )}
          </div>
          <Button
            to="/"
            className="block px-4 py-2 bg-black text-white rounded-md hover:bg-black mt-6 text-center w-full"
            onClick={handleOnSubmit}
          >
            Sign Up
          </Button>
          <Typography color="gray" className="mt-4 text-center font-normal">
            Already have an account?{" "}
            <a href="/" className="font-medium text-gray-900">
              Login
            </a>
          </Typography>
        </form>
      </Card>
    </div>
  );
}

export default SignUp;
