export const saveToken = (token) => {
    // Store token in local storage
    localStorage.setItem('token', token);
  };
  
export const removeToken = () => {
  // Remove token from local storage
  localStorage.removeItem('token');
};

export const saveUsername=(username)=>{
  localStorage.setItem('username',username);
}



  