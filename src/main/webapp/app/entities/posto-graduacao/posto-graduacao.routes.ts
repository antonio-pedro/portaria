import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PostoGraduacaoComponent } from './list/posto-graduacao.component';
import { PostoGraduacaoDetailComponent } from './detail/posto-graduacao-detail.component';
import { PostoGraduacaoUpdateComponent } from './update/posto-graduacao-update.component';
import PostoGraduacaoResolve from './route/posto-graduacao-routing-resolve.service';

const postoGraduacaoRoute: Routes = [
  {
    path: '',
    component: PostoGraduacaoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PostoGraduacaoDetailComponent,
    resolve: {
      postoGraduacao: PostoGraduacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PostoGraduacaoUpdateComponent,
    resolve: {
      postoGraduacao: PostoGraduacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PostoGraduacaoUpdateComponent,
    resolve: {
      postoGraduacao: PostoGraduacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default postoGraduacaoRoute;
