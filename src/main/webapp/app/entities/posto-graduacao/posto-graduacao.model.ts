import { IMilitar } from 'app/entities/militar/militar.model';

export interface IPostoGraduacao {
  id: number;
  descricao?: string | null;
  sigla?: string | null;
  militars?: IMilitar[] | null;
}

export type NewPostoGraduacao = Omit<IPostoGraduacao, 'id'> & { id: null };
