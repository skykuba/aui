export interface CityInterface {
  id: string;
  city: string;
  state: string;
  country: string;
}

export interface AddressInterface {
  id: string;
  street: string;
  buildingNumber: string;
  city: CityInterface;
}

export interface ClientInterface {
  id: string;
  name: string;
  nip: string;
  address: AddressInterface;
}

export interface ClientListResponse {
  clients: ClientInterface[];
}

export interface CreateOrUpdateClientInterface {
  name: string;
  nip: string;
  address: {
    street: string;
    buildingNumber: string;
    city: {
      city: string;
      state: string;
      country: string;
    }
  }
}

export interface SimpleClientInterface {
  id: string;
  name: string;
  city: string;
  country: string;
}

export class Client implements ClientInterface {
  id: string;
  name: string;
  nip: string;
  address: AddressInterface;

  constructor(data: ClientInterface) {
    this.id = data.id;
    this.name = data.name;
    this.nip = data.nip;
    this.address = data.address;
  }

  static fromApi(client: ClientInterface): Client {
    return new Client(client);
  }

  static toSimple(client: Client): SimpleClientInterface {
    return {
      id: client.id,
      name: client.name,
      city: client.address.city.city,
      country: client.address.city.country
    };
  }
}
