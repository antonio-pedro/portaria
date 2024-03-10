import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOM, NewOM } from '../om.model';

export type PartialUpdateOM = Partial<IOM> & Pick<IOM, 'id'>;

export type EntityResponseType = HttpResponse<IOM>;
export type EntityArrayResponseType = HttpResponse<IOM[]>;

@Injectable({ providedIn: 'root' })
export class OMService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/oms');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(oM: NewOM): Observable<EntityResponseType> {
    return this.http.post<IOM>(this.resourceUrl, oM, { observe: 'response' });
  }

  update(oM: IOM): Observable<EntityResponseType> {
    return this.http.put<IOM>(`${this.resourceUrl}/${this.getOMIdentifier(oM)}`, oM, { observe: 'response' });
  }

  partialUpdate(oM: PartialUpdateOM): Observable<EntityResponseType> {
    return this.http.patch<IOM>(`${this.resourceUrl}/${this.getOMIdentifier(oM)}`, oM, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOM>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOM[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOMIdentifier(oM: Pick<IOM, 'id'>): number {
    return oM.id;
  }

  compareOM(o1: Pick<IOM, 'id'> | null, o2: Pick<IOM, 'id'> | null): boolean {
    return o1 && o2 ? this.getOMIdentifier(o1) === this.getOMIdentifier(o2) : o1 === o2;
  }

  addOMToCollectionIfMissing<Type extends Pick<IOM, 'id'>>(oMCollection: Type[], ...oMSToCheck: (Type | null | undefined)[]): Type[] {
    const oMS: Type[] = oMSToCheck.filter(isPresent);
    if (oMS.length > 0) {
      const oMCollectionIdentifiers = oMCollection.map(oMItem => this.getOMIdentifier(oMItem)!);
      const oMSToAdd = oMS.filter(oMItem => {
        const oMIdentifier = this.getOMIdentifier(oMItem);
        if (oMCollectionIdentifiers.includes(oMIdentifier)) {
          return false;
        }
        oMCollectionIdentifiers.push(oMIdentifier);
        return true;
      });
      return [...oMSToAdd, ...oMCollection];
    }
    return oMCollection;
  }
}
