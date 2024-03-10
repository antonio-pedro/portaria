import { IMilitar } from 'app/entities/militar/militar.model';

export interface IOM {
  id: number;
  nome?: string | null;
  sigla?: string | null;
  codom?: number | null;
  militars?: IMilitar[] | null;
}

export type NewOM = Omit<IOM, 'id'> & { id: null };
