import { useState } from "react"
import { ToastContainer, toast} from "react-toastify";

function AddFilm() {
  
  const [name, setName] = useState("");
  const [type, setType] = useState("");

  const addFilm = () => {
    fetch("http://localhost:8080/films", {
      method: "POST",
      headers: {
        "Content-Type": "Application/JSON"
      },
      body: JSON.stringify({
        "name": name,
        "type": type
      })
    })
    .then(res => res.json())
    .then(json => {
      if (json.timestamp && json.status && json.error) {
        toast.error("Couldn't add film.");
      } else {
        toast.success("Film successfully added.");
      }
    });
  }
  
  return (
    <>
      <label>Film name: </label>
      <input value={name} onChange={e => setName(e.target.value)} type="text"/> <br />
      <label>Film type: </label>
      <select name="filmType" id="filmType" value={type} onChange={e => setType(e.target.value)}>
        <option value=""></option>
        <option value="NEW">NEW</option>
        <option value="REGULAR">REGULAR</option>
        <option value="OLD">OLD</option>
      </select> <br />
      <button onClick={addFilm}>Add</button>

      <ToastContainer
        position="bottom-right"
        theme="dark"
        autoClose={4000}/>
    </>
  )
}

export default AddFilm