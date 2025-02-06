import { useEffect, useState } from "react"
import { ToastContainer, toast} from "react-toastify";
import { Film } from "../models/Film";
import { useNavigate } from "react-router-dom";

function ReturnFilms( {userId}: {userId: string} ) {
  
  const [cart, setCart] = useState<Film[]>([]);
  const [rentedFilms, setRentedFilms] = useState<Film[]>([]);

  useEffect(() => {
      fetchFilms();
    }, [cart]);

  const navigate = useNavigate();

  const fetchFilms = async () => {
    const response = await fetch("http://localhost:8080/rented-films?personId=" + userId);
    const data = await response.json();
    const dataFiltered = data.filter((f: Film) => !cart.some((c: Film) => c.id === f.id));
    setRentedFilms(dataFiltered);
  };

  function removeFromCart(film: Film) {
    const newCart = cart.filter(f => f.id != film.id);
    setCart(newCart);
  }

  const returnFilms = () => {

    const requestBody = cart.map(cartFilm => {
      return {
        "id": cartFilm.id,
        "days": 5
      }
    });

    fetch(`http://localhost:8080/end-rental?personId=${userId}`, {
      method: "POST",
      headers: {
        "Content-Type": "Application/JSON"
      },
      body: JSON.stringify(requestBody)
    })
    .then(res => res.json())
    .then(json => {
      if (json.timestamp && json.status && json.error) {
        toast.error("Couldn't add film.");
      } else {
        toast.success("Films successfully returned.");
        navigate("/home")
      }
    });
  }

  function addToCart(film: Film) {
    if (cart.indexOf(film) > -1) {
      toast.error("Film is already in cart.")
      return ;
    }
    setCart(c => [...c, film]);
  }

  if (userId === "" || userId === undefined) {
    navigate("/")
    return ;
  }
  
  return (
    <>

      <div>{cart.length ? "Your selected films:" : "Cart is empty"}</div>
        <div>
          {cart?.map(film =>
            <div key={film.id}>
              <span >{film.name}; </span>
              <span>{film.daysRented} days</span>
              <button onClick={() => removeFromCart(film)}>-</button>
            </div>
          )}
      </div> <br />
      <button onClick={returnFilms}>End rentals</button>
      <br /> <br />

      <div>Rented films:</div>
      <div key={"films-list"}>
        {rentedFilms?.map(film => {
          return (
          <div key={film.id}>
            <span>{film.name}</span>
            <button onClick={() => addToCart(film)}>+</button>
          </div>
      )})}
      </div>

      <ToastContainer
        position="bottom-right"
        theme="dark"
        autoClose={4000}/>
    </>
  )
}

export default ReturnFilms