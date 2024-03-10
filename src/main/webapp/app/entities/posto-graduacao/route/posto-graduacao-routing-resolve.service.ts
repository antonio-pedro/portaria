import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPostoGraduacao } from '../posto-graduacao.model';
import { PostoGraduacaoService } from '../service/posto-graduacao.service';

export const postoGraduacaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IPostoGraduacao> => {
  const id = route.params['id'];
  if (id) {
    return inject(PostoGraduacaoService)
      .find(id)
      .pipe(
        mergeMap((postoGraduacao: HttpResponse<IPostoGraduacao>) => {
          if (postoGraduacao.body) {
            return of(postoGraduacao.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default postoGraduacaoResolve;
