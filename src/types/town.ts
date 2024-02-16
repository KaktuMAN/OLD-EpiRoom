import {Room} from "@customTypes/room";

export interface TypeFloor {
  floor: number,
  name: string,
}

export interface MultipleRooms {
  room: string,
  linkedRooms: string[]
}

export interface Town {
  name: string,
  code: string,
  mainFloor: number,
  multipleRooms: MultipleRooms[],
  floors: [TypeFloor],
  rooms: [Room],
}