import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOM } from '../om.model';
import { OMService } from '../service/om.service';

export const oMResolve = (route: ActivatedRouteSnapshot): Observable<null | IOM> => {
  const id = route.params['id'];
  if (id) {
    return inject(OMService)
      .find(id)
      .pipe(
        mergeMap((oM: HttpResponse<IOM>) => {
          if (oM.body) {
            return of(oM.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default oMResolve;
