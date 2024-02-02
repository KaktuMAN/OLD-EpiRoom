import {Room} from "@customTypes/room";

export interface TypeFloor {
  floor: number,
  name: string,
}

export interface Town {
  name: string,
  code: string,
  mainFloor: number,
  floors: [TypeFloor],
  rooms: [Room],
}