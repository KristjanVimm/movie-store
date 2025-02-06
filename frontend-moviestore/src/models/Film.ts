
export type Film = {
  "id": number,
  "name": string,
  "type": string,
  "daysRented": number,
  "cart": {
    "id": number,
    "status": string
  }
}