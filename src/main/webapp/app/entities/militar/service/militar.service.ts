import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMilitar, NewMilitar } from '../militar.model';

export type PartialUpdateMilitar = Partial<IMilitar> & Pick<IMilitar, 'id'>;

export type EntityResponseType = HttpResponse<IMilitar>;
export type EntityArrayResponseType = HttpResponse<IMilitar[]>;

@Injectable({ providedIn: 'root' })
export class MilitarService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/militars');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(militar: NewMilitar): Observable<EntityResponseType> {
    return this.http.post<IMilitar>(this.resourceUrl, militar, { observe: 'response' });
  }

  update(militar: IMilitar): Observable<EntityResponseType> {
    return this.http.put<IMilitar>(`${this.resourceUrl}/${this.getMilitarIdentifier(militar)}`, militar, { observe: 'response' });
  }

  partialUpdate(militar: PartialUpdateMilitar): Observable<EntityResponseType> {
    return this.http.patch<IMilitar>(`${this.resourceUrl}/${this.getMilitarIdentifier(militar)}`, militar, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMilitar>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMilitar[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMilitarIdentifier(militar: Pick<IMilitar, 'id'>): number {
    return militar.id;
  }

  compareMilitar(o1: Pick<IMilitar, 'id'> | null, o2: Pick<IMilitar, 'id'> | null): boolean {
    return o1 && o2 ? this.getMilitarIdentifier(o1) === this.getMilitarIdentifier(o2) : o1 === o2;
  }

  addMilitarToCollectionIfMissing<Type extends Pick<IMilitar, 'id'>>(
    militarCollection: Type[],
    ...militarsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const militars: Type[] = militarsToCheck.filter(isPresent);
    if (militars.length > 0) {
      const militarCollectionIdentifiers = militarCollection.map(militarItem => this.getMilitarIdentifier(militarItem)!);
      const militarsToAdd = militars.filter(militarItem => {
        const militarIdentifier = this.getMilitarIdentifier(militarItem);
        if (militarCollectionIdentifiers.includes(militarIdentifier)) {
          return false;
        }
        militarCollectionIdentifiers.push(militarIdentifier);
        return true;
      });
      return [...militarsToAdd, ...militarCollection];
    }
    return militarCollection;
  }
}
