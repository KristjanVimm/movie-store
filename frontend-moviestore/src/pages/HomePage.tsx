import { createRef, useEffect, useState } from "react"
import { Film } from "../models/Film";
import { toast, ToastContainer } from "react-toastify";
import { useNavigate } from "react-router-dom";

function HomePage({userId}) {

  const [cart, setCart] = useState<Film[]>([]);
  const [films, setFilms] = useState<Film[]>([]);

  useEffect(() => {
    fetchFilms();
    fetchCart();
  }, []);

  const navigate = useNavigate();


  async function fetchCart() {
    const response = await fetch(`http://localhost:8080/cart-films?personId=${userId}`);
    const data = await response.json();
    setCart(data);
  };

  async function fetchFilms() {
    const response = await fetch("http://localhost:8080/available-films");
    const data = await response.json();
    setFilms(data);
  };

  async function startRental () {

    const requestBody = cart.map(cartFilm => {
      return {
        "id": cartFilm.id,
        "days": cartFilm.daysRented
      }
    });

    const response = await fetch(`http://localhost:8080/start-rental?personId=${userId}`, {
      method: "POST",
      headers: {
        "Content-Type": "Application/JSON"
      },
      body: JSON.stringify(requestBody)
    });
    const data = await response.json();
    if (data.timestamp && data.status) {
      toast.error("Couldn't start rental.");
    } else {
      toast.success("Rental started!");
    }
    await fetchCart();
    await fetchFilms();
  }

  const addToCart = async (film: Film, daysRented: number) => {
    await fetch(`http://localhost:8080/carts?personId=${userId}&filmId=${film.id}&daysRented=${daysRented}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });
    await fetchCart();
    await fetchFilms();
  };

  const removeFromCart = async (film: Film) => {
    await fetch(`http://localhost:8080/carts?filmId=${film.id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
    });
    await fetchCart();
    await fetchFilms();
  };

  if (userId === "") {
    navigate("/");
    return ;
  }

  return (
    <>

      <div>Logged in as: {userId}</div> <br /> <br />

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
      
        <button onClick={startRental}>Start rental</button>

      <br /> <br />

      <div>All films:</div>
      <div key={"films-list"}>
        {films?.map(film => {
          const inputRef = createRef<HTMLInputElement>();
          return (
          <div key={film.id}>
            <span>{film.name}</span>
            <button disabled={film.cart !== null} onClick={() => {
              const daysRented = inputRef.current?.value;
              addToCart(film, parseInt(daysRented === undefined? "1" : daysRented));
            }}>+</button>
            <input ref={inputRef} type="number" defaultValue={1} min={1} max={14} />
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

export default HomePage