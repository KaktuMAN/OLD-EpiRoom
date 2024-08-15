export interface Floor {
  floor: number;
  campusCode: string;
  name: string;
  mainFloor: boolean;
}

export interface Room {
  floor: number;
  campusCode: string;
  type: string;
  name: string;
  code: string;
  displayName: string;
  seats: number;
  displayStatus: boolean;
}

export interface LinkedRoom {
  mainRoomCode: string;
  linkedRoomCodes: string[];
}