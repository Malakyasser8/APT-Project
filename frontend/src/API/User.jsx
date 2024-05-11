import axios from "axios";

const baseUrl = "http://192.168.8.102:3002/";

//const baseUrl = String(process.env.VITE_BASE_URL);
console.log("baseUrl ", baseUrl);

axios.defaults.baseURL = baseUrl;

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = `${token}`;
    return config;
  },
  (error) => {
    console.log("Error interceptor", error);
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  (response) => {
    let data = response.data;
    let noStatus = false;
    
    if (noStatus) data = Object.values(data)[0]; // Last object in the response
    if (data == undefined) data = "ok";
    response.data = data;
    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);

const fetchRequest = async (endPoint) => {
  try {
    const response = await axios.get(baseUrl + endPoint, {
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("token"),
      },
      withCredentials: false,
    });
    // console.log(response);

    return response;
  } catch (error) {
    const errorMessage =
      error.response?.data?.err?.message ||
      error.response?.data?.error?.message ||
      "Unknown error";
    return Promise.reject(errorMessage);

    // throw new Error(errorMessage);
  }
};

const patchRequest = async ({ newSettings, endPoint }) => {
  try {
    const response = await axios.patch(endPoint, newSettings, {
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("token"),
      },
      withCredentials: false,
    });
    // console.log(response);

    return response.data;
  } catch (error) {
    const errorMessage =
      error.response?.data?.err?.message ||
      error.response?.data?.error?.message ||
      "Unknown error";
    return Promise.reject(errorMessage);
  }
};

const postRequest = async ({
  endPoint,
  data,
}) => {
  try {
    const response = await axios.post(endPoint, data, {
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("token"),
      },
      withCredentials: false,
    });

    return {
      ...response.data,
      token: response.headers["authorization"],
    };
  } catch (error) {
    const errorMessage =
      error.response?.data?.err?.message ||
      error.response?.data?.error?.message ||
      "Unknown error";
    return Promise.reject(errorMessage);
  }
};

export { fetchRequest, patchRequest, postRequest };
