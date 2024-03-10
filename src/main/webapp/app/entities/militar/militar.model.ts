import { IPostoGraduacao } from 'app/entities/posto-graduacao/posto-graduacao.model';
import { IOM } from 'app/entities/om/om.model';

export interface IMilitar {
  id: number;
  identidade?: number | null;
  cpf?: number | null;
  postoGraduacao?: string | null;
  nome?: string | null;
  nomeGuerra?: string | null;
  email?: string | null;
  foto?: string | null;
  fotoContentType?: string | null;
  posto?: IPostoGraduacao | null;
  om?: IOM | null;
}

export type NewMilitar = Omit<IMilitar, 'id'> & { id: null };
