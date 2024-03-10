import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPostoGraduacao, NewPostoGraduacao } from '../posto-graduacao.model';

export type PartialUpdatePostoGraduacao = Partial<IPostoGraduacao> & Pick<IPostoGraduacao, 'id'>;

export type EntityResponseType = HttpResponse<IPostoGraduacao>;
export type EntityArrayResponseType = HttpResponse<IPostoGraduacao[]>;

@Injectable({ providedIn: 'root' })
export class PostoGraduacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/posto-graduacaos');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(postoGraduacao: NewPostoGraduacao): Observable<EntityResponseType> {
    return this.http.post<IPostoGraduacao>(this.resourceUrl, postoGraduacao, { observe: 'response' });
  }

  update(postoGraduacao: IPostoGraduacao): Observable<EntityResponseType> {
    return this.http.put<IPostoGraduacao>(`${this.resourceUrl}/${this.getPostoGraduacaoIdentifier(postoGraduacao)}`, postoGraduacao, {
      observe: 'response',
    });
  }

  partialUpdate(postoGraduacao: PartialUpdatePostoGraduacao): Observable<EntityResponseType> {
    return this.http.patch<IPostoGraduacao>(`${this.resourceUrl}/${this.getPostoGraduacaoIdentifier(postoGraduacao)}`, postoGraduacao, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPostoGraduacao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPostoGraduacao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPostoGraduacaoIdentifier(postoGraduacao: Pick<IPostoGraduacao, 'id'>): number {
    return postoGraduacao.id;
  }

  comparePostoGraduacao(o1: Pick<IPostoGraduacao, 'id'> | null, o2: Pick<IPostoGraduacao, 'id'> | null): boolean {
    return o1 && o2 ? this.getPostoGraduacaoIdentifier(o1) === this.getPostoGraduacaoIdentifier(o2) : o1 === o2;
  }

  addPostoGraduacaoToCollectionIfMissing<Type extends Pick<IPostoGraduacao, 'id'>>(
    postoGraduacaoCollection: Type[],
    ...postoGraduacaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const postoGraduacaos: Type[] = postoGraduacaosToCheck.filter(isPresent);
    if (postoGraduacaos.length > 0) {
      const postoGraduacaoCollectionIdentifiers = postoGraduacaoCollection.map(
        postoGraduacaoItem => this.getPostoGraduacaoIdentifier(postoGraduacaoItem)!,
      );
      const postoGraduacaosToAdd = postoGraduacaos.filter(postoGraduacaoItem => {
        const postoGraduacaoIdentifier = this.getPostoGraduacaoIdentifier(postoGraduacaoItem);
        if (postoGraduacaoCollectionIdentifiers.includes(postoGraduacaoIdentifier)) {
          return false;
        }
        postoGraduacaoCollectionIdentifiers.push(postoGraduacaoIdentifier);
        return true;
      });
      return [...postoGraduacaosToAdd, ...postoGraduacaoCollection];
    }
    return postoGraduacaoCollection;
  }
}
