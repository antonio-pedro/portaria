import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MilitarComponent } from './list/militar.component';
import { MilitarDetailComponent } from './detail/militar-detail.component';
import { MilitarUpdateComponent } from './update/militar-update.component';
import MilitarResolve from './route/militar-routing-resolve.service';

const militarRoute: Routes = [
  {
    path: '',
    component: MilitarComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MilitarDetailComponent,
    resolve: {
      militar: MilitarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MilitarUpdateComponent,
    resolve: {
      militar: MilitarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MilitarUpdateComponent,
    resolve: {
      militar: MilitarResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default militarRoute;
