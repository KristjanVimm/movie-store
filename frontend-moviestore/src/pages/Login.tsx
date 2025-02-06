import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";

function Login({userId, setUserId}) {

  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  function handleClick () {
    fetch(`http://localhost:8080/password?personId=${userId}&password=${password}`)
    .then(res => res.json())
    .then(json => {
      if (json === true) {
        routeChange();
      } else {
        toast.error("Password is incorrect");
      }
    })
  }

  const routeChange = () =>{
    const path = "/home";
    navigate(path);
  }


  return (
    <>
      <div>Login</div>
      <label>User id:</label>
      <input value={userId} onChange={e => setUserId(e.target.value)} type="text" /> <br />
      <label>Password:</label>
      <input value={password} onChange={e => setPassword(e.target.value)} type="password" /> <br />
      <button onClick={handleClick}>Enter</button>

      <ToastContainer
        position="bottom-right"
        theme="dark"
        autoClose={4000}/>
    </>
  )
}

export default Login