import { useState } from "react"
import { ToastContainer, toast} from "react-toastify";

function AddFilm() {
  
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");

  const addUser = () => {
    fetch("http://localhost:8080/persons", {
      method: "POST",
      headers: {
        "Content-Type": "Application/JSON"
      },
      body: JSON.stringify({
        "name": name,
        "password": password
      })
    })
    .then(res => res.json())
    .then(json => {
      if (json.timestamp && json.status && json.error) {
        toast.error("Couldn't add user.");
      } else {
        toast.success("User successfully added.");
      }
    });
  }
  
  return (
    <>
      <label>Username: </label>
      <input value={name} onChange={e => setName(e.target.value)} type="text"/> <br />
      <label>Password: </label>
      <input value={password} onChange={e => setPassword(e.target.value)} type="password" /> <br />
      <button onClick={addUser}>Add</button>

      <ToastContainer
        position="bottom-right"
        theme="dark"
        autoClose={4000}/>
    </>
  )
}

export default AddFilm